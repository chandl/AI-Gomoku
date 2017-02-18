/*
    Southern Oregon University - CS455 Artificial Intelligence - Lab 2 - Gomoku

    Authors: Chandler Severson, Janelle Bakey
    Date: 2/10/2017
    Class: MoveSearch.java
        Desc: Searches for the most optimal play based on the current game state.

    Heuristics:
        - Used https://www.mimuw.edu.pl/~awojna/SID/referaty/Go-Moku.pdf for reference.
        - Heuristic function is kinda sucky but it works
    Search:
        - Using Minimax search w/ Alpha-Beta pruning
        - Pretty slow, usually around 5sec to find good move :-(.
 */

import java.util.ArrayList;
import java.util.List;

public class MoveSearch implements Runnable {
    private GameState gameState;
    private int currentDepth;
    private boolean stopSearch = false; //set to true when we run out of time.
    private String lastBestMove;
    private static int maxDepth = 4;
    private static boolean DEBUG = false;

    @Override
    public void run() {

        if (DEBUG) System.out.println("MOVE SEARCH THREAD STARTED");

        GameState currentState = gameState;
        lastBestMove = null;
        String theMove = miniMax(currentState.getBoard(), currentState.getPlayer());
        lastBestMove = theMove;
        if(DEBUG)System.out.println("Best Move:" +theMove);
        System.out.println("Max Depth Reached: "+currentDepth);
    }

    /**
     * The MiniMax algorithm for adversarial serach.
     * @param board The current game board.
     * @param player The player that you would like to generate a move for.
     * @return The most promising move.
     */
    public String miniMax(char[][] board, char player){
        currentDepth = 0;

        String bestMove = null;
        double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
        String[] possibleMoves = generateMoves(board);
        for(int depth = 1; depth <= maxDepth; depth++){//Iteratively deepen the search.
            for(int i=0; i<possibleMoves.length; i++){
                char[][] theBoard = applyMove(board, player, possibleMoves[i]);
                moveUtility = minMove(theBoard, GameState.getEnemy(player), depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);//start the search

                if(DEBUG)System.out.println("MoveUtility for " + possibleMoves[i] + "= "+moveUtility);

                if(moveUtility > bestMoveUtility){
                    this.lastBestMove = possibleMoves[i];
                    bestMove = possibleMoves[i];
                    bestMoveUtility = moveUtility;
                    if(DEBUG)System.out.println("Best Move Now: "+bestMove);
                }
            }
            currentDepth = depth;
        }

        return bestMove;
    }

    /**
     * The MAX part of MiniMax algorithm. Maximizes expected outcomes.
     *
     * @param board The game board to be analyzed
     * @param player YOUR player.
     * @param depthLeft Keeps track of the depth of the search.
     * @param alpha The ALPHA part of A-B pruning
     * @param beta The BETA part of A-B pruning
     * @return A utility value for the specified move and player.
     */
    public double maxMove(char[][] board, char player, int depthLeft, double alpha, double beta){


        double currentUtility = GameState.getStateUtility(board, player);// - GameState.getStateUtility(board, GameState.getEnemy(player));

        if(currentUtility == GameState.FIVE_IN_A_ROW  || depthLeft == 0 || stopSearch){

            return currentUtility;
        }else{
            double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(board);
            List<char[][]> boards = generateBoards(board, player, moves);

            for(char[][] b : boards){
                moveUtility = minMove(b, GameState.getEnemy(player), depthLeft-1, alpha, beta);
                if(moveUtility > bestMoveUtility) bestMoveUtility = moveUtility;

                if(moveUtility >= beta) return moveUtility;//Alpha-Beta pruning

                if(moveUtility > alpha) alpha = moveUtility;
            }

            return bestMoveUtility;
        }
    }

    /**
     * The MIN part of MiniMax algoithm. Minimizes expected outcomes.
     *
     * @param board The game board to be analyzed.
     * @param player The ENEMY player.
     * @param depthLeft Keeps track of the depth of the search.
     * @param alpha The ALPHA part of A-B pruning
     * @param beta The BETA part of A-B pruning
     * @return A utility value for the specified move and player.
     */
    public double minMove(char[][] board, char player, int depthLeft, double alpha, double beta){
//        System.out.println("minMove called. depthLeft: "+depthLeft+". Player: "+player);
        double currentUtility = GameState.getStateUtility(board, player);

        if(currentUtility == GameState.FIVE_IN_A_ROW || depthLeft == 0 || stopSearch){
            return currentUtility;
        }else{
            double bestMoveUtility = Double.POSITIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(board);

            List<char[][]> boards = generateBoards(board, GameState.getEnemy(player), moves);

            for(char[][] b: boards){
                moveUtility = maxMove(b, GameState.getEnemy(player), depthLeft-1, alpha, beta);
                if(moveUtility < bestMoveUtility) bestMoveUtility = moveUtility;

                if(moveUtility <= alpha) return moveUtility;
                if(moveUtility < beta) beta = moveUtility;
            }

            return bestMoveUtility;
        }
    }

    /**
     * Makes a 'move' on a game board.
     *
     * @param board The current game board.
     * @param player The player that shall take the move.
     * @param move The move that shall be taken.
     * @return A new game board with the specified move executed.
     */
    public char[][] applyMove(char[][] board, char player, String move){
//        System.out.println("Possible move: "+game.getPlayer()+ " to "+move);

        char[][] newBoard = new char[board.length][board.length];
        for(int i=0; i<board.length; i++){
            newBoard[i] = board[i].clone();
        }

        String[] moves = move.split(" ");
        int x = Integer.parseInt(moves[0]);
        int y = Integer.parseInt(moves[1]);

        newBoard[x][y] = player;
        return newBoard;
    }


    /**
     * This move is set in the MiniMax function.
     * It can be used to get the (last generated) best move when the time runs out.
     *
     * @return The instance variable lastBestMove.
     */
    public String getMove() {
//        System.out.println("GetMove called. Best move: "+lastBestMove);
        return lastBestMove;
    }


    /**
     * Generate possible game boards.
     * Used in conjunction w/ generateMoves() to get the moves array.
     *
     * @param board The current game board
     * @param player The player that will make the move.
     * @param moves An Array of possible moves.
     * @return A List of character matricies (game boards) that are possible.
     */
    public List<char[][]> generateBoards(char[][] board, char player, String[] moves){
        List<char[][]> possibleBoards = new ArrayList<>();

        for(int i=0; i<moves.length; i++){
            possibleBoards.add(applyMove(board, player, moves[i]));
        }

        return possibleBoards;
    }

    /**
     * Generates possible moves based on a game board.
     * The moves are just empty spaces in the game.
     *
     * @param board The current game board.
     * @return A String array of possible moves.
     */
    public String[] generateMoves(char[][] board) {
        List<String> moves = new ArrayList<>();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == ' ') { //success!
                    moves.add( i + " " + j);
                }
            }
        }
        return moves.toArray(new String[moves.size()]);
    }

    /**
     * To be called when you would like to stop searches.
     * Used in the Timer class to stop searching after a specified time.
     *
     * @param stopSearch Set to true to stop search
     */
    public void setStopSearch(boolean stopSearch) {
        this.stopSearch = stopSearch;
    }

    public MoveSearch(GameState gameState) {
        this.gameState = gameState;
    }
}