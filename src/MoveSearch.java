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
import java.util.concurrent.ConcurrentHashMap;

public class MoveSearch implements Runnable {



    private GameState gameState;
    private int maxDepth = 3;
    private int currentDepth;
    private String[] moves;
    private boolean DEBUG = true;
    private ConcurrentHashMap<String, Double> seenXMoves;
    private ConcurrentHashMap<String, Double> seenOMoves;
    private boolean stopSearch = false; //set to true when we run out of time.
    private String lastBestMove;

    public void setStopSearch(boolean stopSearch) {
        this.stopSearch = stopSearch;
    }

    public MoveSearch(GameState gameState) {
        this.gameState = gameState;
        seenXMoves = new ConcurrentHashMap<>();
        seenOMoves = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {

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
        System.out.println("MINIMAX STARTED");
        currentDepth = 0;

        String bestMove = null;
        double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility, worstMoveUtility = Double.POSITIVE_INFINITY;
        String[] possibleMoves = generateMoves(board);

        for(int i=0; i<possibleMoves.length; i++){
//            System.out.println("Evaluating Move: "+possibleMoves[i]);
            char[][] theBoard = applyMove(board, player, possibleMoves[i]);

            moveUtility = minMove(theBoard, GameState.getEnemy(player), maxDepth);

            System.out.println("MoveUtility for " + possibleMoves[i] + "= "+moveUtility);

            if(moveUtility > bestMoveUtility){
                this.lastBestMove = possibleMoves[i];
                bestMove = possibleMoves[i];
                bestMoveUtility = moveUtility;
                System.out.println("Best Move Now: "+bestMove);

            }
        }

        return bestMove;
    }

    public double maxMove(char[][] board, char player, int depthLeft){
//        System.out.println("maxMove called. depthLeft: "+depthLeft+". Player: "+player);


        double currentUtility = GameState.getStateUtility(board, player);
        double currentEnemyUtility = GameState.getStateUtility(board, GameState.getEnemy(player));

        if(currentUtility == GameState.FIVE_IN_A_ROW || currentEnemyUtility > currentUtility || depthLeft == 0 || stopSearch){

//            System.out.println("maxMove RETURNING "+currentUtility+" ON TURN "+(maxDepth-depthLeft));
            return currentUtility;
        }else{
            if(currentDepth < maxDepth-depthLeft) currentDepth = maxDepth - depthLeft;
            double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(board);
            List<char[][]> boards = generateBoards(board, player, moves);

            for(char[][] b : boards){
                moveUtility = minMove(b, GameState.getEnemy(player), depthLeft-1);

                if(moveUtility > bestMoveUtility) bestMoveUtility = moveUtility;
            }


            return bestMoveUtility;
        }
    }


    public double minMove(char[][] board, char player, int depthLeft){
//        System.out.println("minMove called. depthLeft: "+depthLeft+". Player: "+player);
//        GameState.printBoard(board);


        double currentUtility = GameState.getStateUtility(board, player);

        if(currentUtility == GameState.FIVE_IN_A_ROW || depthLeft == 0 || stopSearch){
//            System.out.println("minMove RETURNING "+currentUtility+" ON TURN "+(maxDepth-depthLeft));
            return currentUtility;
        }else{
            if(currentDepth < maxDepth-depthLeft) currentDepth = maxDepth - depthLeft;
            double bestMoveUtility = Double.POSITIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(board);

            List<char[][]> boards = generateBoards(board, GameState.getEnemy(player), moves);

            for(char[][] b: boards){
                moveUtility = maxMove(b, GameState.getEnemy(player), depthLeft-1);

                if(moveUtility < bestMoveUtility) bestMoveUtility = moveUtility;
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
        char[][] possible;

        for(int i=0; i<moves.length; i++){

            possible = applyMove(board, player, moves[i]);

            if(player == 'x'){
                if(seenXMoves.get(possible.toString()) == null){
                    possibleBoards.add(possible);
                    seenXMoves.put(possible.toString(), 0.0);
                }
            }else if(player == 'o'){
                if(seenOMoves.get(possible.toString()) == null){
                    possibleBoards.add(possible);
                    seenOMoves.put(possible.toString(), 1.0);
                }
            }

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