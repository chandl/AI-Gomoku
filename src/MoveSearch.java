/*
    Southern Oregon University - CS455 Artificial Intelligence - Lab 2 - Gomoku

    Authors: Chandler Severson, Janelle Bakey, Gabriela Navarrete
    Date: 2/10/2017
    Class: MoveSearch.java
        Desc: Searches for the most optimal play based on the current game state.

    Heuristics:
        -put info about heuristics used
    Search:
        -put info about search implementation
 */

import java.util.ArrayList;
import java.util.List;

public class MoveSearch implements Runnable {



    private GameState gameState;
    private int currentDepth;
    private String[] moves;
    private boolean stopSearch = false; //set to true when we run out of time.
    private String lastBestMove;

    public void setStopSearch(boolean stopSearch) {
        this.stopSearch = stopSearch;
    }

    public MoveSearch(GameState gameState) {
        this.gameState = gameState;

    }

    @Override
    public void run() {

        boolean DEBUG = true;
        if (DEBUG) System.out.println("MOVE SEARCH THREAD STARTED");
//        String[] moves = generateMoves(gameState);
        GameState currentState = gameState;
        lastBestMove = null;
        String theMove = miniMax(currentState.getBoard(), currentState.getPlayer());
        lastBestMove = theMove;
        System.out.println("Best Move:" +theMove);
        System.out.println("Max Depth Reached: "+currentDepth);
    }

    public String miniMax(char[][] board, char player){
//        System.out.println("MINIMAX STARTED");
        currentDepth = 0;

        String bestMove = null;
        double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
        String[] possibleMoves = generateMoves(board);
        int maxDepth = 4;
        for(int depth = 1; depth<= maxDepth; depth++){
            for(int i=0; i<possibleMoves.length; i++){
//            System.out.println("Evaluating Move: "+possibleMoves[i]);
                char[][] theBoard = applyMove(board, player, possibleMoves[i]);

//            moveUtility = minMove(theBoard, GameState.getEnemy(player), maxDepth);
                moveUtility = minMove(theBoard, GameState.getEnemy(player), depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
//                System.out.println("MoveUtility for " + possibleMoves[i] + "= "+moveUtility);

                if(moveUtility > bestMoveUtility){
                    this.lastBestMove = possibleMoves[i];
                    bestMove = possibleMoves[i];
                    bestMoveUtility = moveUtility;
//                    System.out.println("Best Move Now: "+bestMove);

                }
            }
            currentDepth = depth;
        }


        return bestMove;
    }

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


    public double minMove(char[][] board, char player, int depthLeft, double alpha, double beta){
//        System.out.println("minMove called. depthLeft: "+depthLeft+". Player: "+player);
        double currentUtility = GameState.getStateUtility(board, player);// - GameState.getStateUtility(board, GameState.getEnemy(player));

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


    public String getMove() {
        System.out.println("GetMove called. Best move: "+lastBestMove);
        return lastBestMove;
    }

    public List<char[][]> generateBoards(char[][] board, char player, String[] moves){
        List<char[][]> possibleBoards = new ArrayList<>();

        for(int i=0; i<moves.length; i++){
            possibleBoards.add(applyMove(board, player, moves[i]));
        }

        return possibleBoards;
    }

    public String[] generateMoves(char[][] board) {
        List<String> moves = new ArrayList<>();

//        String[] moves = new String[(boardSize * boardSize) - moveCount];
//        if (DEBUG) System.out.println("CREATING MOVE ARRAY, SIZE: " + (boardSize * boardSize - moveCount));
        //loop through everything in board, add moves where it is
//        if (DEBUG) System.out.println("Board length: " + board.length);

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == ' ') { //success!
                    moves.add( i + " " + j);
                }
            }
//           if (DEBUG) System.out.println("\nMoves Available: " + count);
//           if (DEBUG) System.out.println("Moves Taken: " + moveCount);
        }

        return moves.toArray(new String[moves.size()]);
    }

}