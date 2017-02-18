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
    private int maxDepth = 2;
    private int currentDepth;
    private String[] moves;
    private boolean DEBUG = true;
    private ConcurrentHashMap<String, Double> generatedMoves;
    private boolean stopSearch = false; //set to true when we run out of time.
    private String lastBestMove;

    public void setStopSearch(boolean stopSearch) {
        this.stopSearch = stopSearch;
    }

    public MoveSearch(GameState gameState) {
        this.gameState = gameState;
        generatedMoves = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {

        if (DEBUG) System.out.println("MOVE SEARCH THREAD STARTED");
//        String[] moves = generateMoves(gameState);
        GameState currentState = gameState;
        lastBestMove = null;
        String theMove = miniMax(currentState.getBoard(), currentState.getPlayer());
        lastBestMove = theMove;
        System.out.println("Max Depth Reached: "+currentDepth);
    }

    public String miniMax(char[][] board, char player){
        currentDepth = 0;

        String bestMove = null;
        double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
        String[] possibleMoves = generateMoves(board);

        for(int i=0; i<possibleMoves.length; i++){
//            System.out.println("Evaluating Move: "+possibleMoves[i]);
            char[][] theBoard = applyMove(board, player, possibleMoves[i]);

            moveUtility = minMove(theBoard, GameState.getEnemy(player), maxDepth);

            if(moveUtility > bestMoveUtility){
                bestMove = possibleMoves[i];
            }
        }

        return bestMove;
    }

    public double maxMove(char[][] board, char player, int depthLeft){
//        GameState backup = new GameState(game.getBoardSize(), game.getBoard(), game.getPlayer());
        char[][] backupBoard = board.clone();
        System.out.println("maxMove called. depthLeft: "+depthLeft+". Player: "+player);
        GameState.printBoard(board);

        double currentUtility = GameState.getStateUtility(board, player);
        if(currentUtility == GameState.FIVE_IN_A_ROW || depthLeft == 0 || stopSearch){

            System.out.println("maxMove RETURNING "+currentUtility+" ON TURN "+(maxDepth-depthLeft));
            return currentUtility;
        }else{
            currentDepth = maxDepth-depthLeft;
            double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(board);
            List<char[][]> boards = generateBoards(board, player, moves);

            for(char[][] b : boards){
                moveUtility = minMove(b, GameState.getEnemy(player), depthLeft-1);

                if(moveUtility < bestMoveUtility) bestMoveUtility = moveUtility;
            }

//            for(String move:moves){
//                char[][] toEvaluate = applyMove(board,player,move);
//                moveUtility = minMove(toEvaluate, GameState.getEnemy(player), depthLeft - 1);
//
//                if (moveUtility > bestMoveUtility) {
//                    bestMoveUtility = moveUtility;
//                }
//                board = backupBoard;
//
////                if(generatedMoves.get(board.toString()) == null) { //don't continue checking the same game state
////                    moveUtility = minMove(board, player, depthLeft - 1);
////                    generatedMoves.put(board.toString(), moveUtility);
////
////                    if (moveUtility > bestMoveUtility) {
////                        bestMoveUtility = moveUtility;
////                    }
////                }else{
////                    System.out.println("(MAX)USE EXISTING UTILITY VALUE");
//////                    System.out.println("MAXD - BAD. "+ generatedMoves.get(gameEvaluate.getBoard()) + generatedMoves.values() );
////                    moveUtility = generatedMoves.get(board.toString());
////                    if(moveUtility > bestMoveUtility){
////                        bestMoveUtility = moveUtility;
////                    }
////                }
//            }


            return bestMoveUtility;
        }
    }

    //NEED TO CONVERT THIS METHOD TO USE CHAR[][] ARRAYS INSTEAD OF GAME STATES - USING GAME STATES MESSES EVERYTHING UP APPARENTLY.

    public double minMove(char[][] board, char player, int depthLeft){
        System.out.println("minMove called. depthLeft: "+depthLeft+". Player: "+player);
        GameState.printBoard(board);

        char[][] backupBoard = board.clone();

        double currentUtility = GameState.getStateUtility(board, player);
        if(currentUtility == GameState.FIVE_IN_A_ROW || depthLeft == 0 || stopSearch){
            System.out.println("minMove RETURNING "+currentUtility+" ON TURN "+(maxDepth-depthLeft));
            return currentUtility;
        }else{
            currentDepth = maxDepth-depthLeft;
            double bestMoveUtility = Double.POSITIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(board);

            List<char[][]> boards = generateBoards(board, player, moves);

            for(char[][] b: boards){
                moveUtility = maxMove(b, GameState.getEnemy(player), depthLeft-1);

                if(moveUtility < bestMoveUtility) bestMoveUtility = moveUtility;
            }


//            for(String move:moves){
////                GameState newGame = new GameState(game);
//
//                char[][] toEvaluate = applyMove(board, player, move);
//                moveUtility = maxMove(toEvaluate, GameState.getEnemy(player), depthLeft-1);
//
//                if(moveUtility < bestMoveUtility){
//                    bestMoveUtility = moveUtility;
//                }
//                board = backupBoard;
//
////                if(generatedMoves.get(board.toString()) == null){ //don't continue checking the same game state
////                    moveUtility = maxMove(board, player, depthLeft-1);
////                    generatedMoves.put(board.toString(), moveUtility);
////                    if(moveUtility < bestMoveUtility){
////                        bestMoveUtility = moveUtility;
////                    }
////                }else{
////                    System.out.println("(MIN)USE EXISTING UTILITY VALUE");
////                    moveUtility = generatedMoves.get(board.toString());
////                    if(moveUtility > bestMoveUtility){
////                        bestMoveUtility = moveUtility;
////                    }
////                }
//            }

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