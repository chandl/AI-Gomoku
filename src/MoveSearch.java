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
        String theMove = miniMax(currentState);
        lastBestMove = theMove;
        System.out.println("Max Depth Reached: "+currentDepth);
    }

    public String miniMax(GameState game){
        currentDepth = 0;

        String bestMove = null;
        double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
        String[] possibleMoves = generateMoves(game);



        for(int i=0; i<possibleMoves.length; i++){
            GameState newGame = new GameState(game);
//            System.out.println("Evaluating Move: "+possibleMoves[i]);
            GameState toEvaluate = applyMove(newGame, possibleMoves[i]);
            moveUtility = minMove(toEvaluate, maxDepth);
            if(moveUtility > bestMoveUtility){
                bestMove = possibleMoves[i];
            }
        }

        return bestMove;
    }

    public double maxMove(GameState game, int depthLeft){
        GameState backup = new GameState(game.getBoardSize(), game.getBoard(), game.getPlayer());

        System.out.println("maxMove called. depthLeft: "+depthLeft+". Player: "+game.getPlayer());
        game.printBoard();
        if(game.getStateUtility() == game.FIVE_IN_A_ROW || depthLeft == 0 || stopSearch){
            System.out.println("maxMove RETURNING "+game.getStateUtility()+" ON TURN "+(maxDepth-depthLeft));
            return game.getStateUtility();
        }else{
            currentDepth = maxDepth-depthLeft;
            double bestMoveUtility = Double.NEGATIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(game);
            for(String move:moves){
                GameState gameEvaluate = applyMove(backup, move);
                if(generatedMoves.get(gameEvaluate.getBoard()) == null) { //don't continue checking the same game state
                    moveUtility = minMove(gameEvaluate, depthLeft - 1);
                    generatedMoves.put(gameEvaluate.toString(), moveUtility);

                    if (moveUtility > bestMoveUtility) {
                        bestMoveUtility = moveUtility;
                    }
                }else{
                    System.out.println("(MAX)USE EXISTING UTILITY VALUE");
//                    System.out.println("MAXD - BAD. "+ generatedMoves.get(gameEvaluate.getBoard()) + generatedMoves.values() );
                    moveUtility = generatedMoves.get(gameEvaluate.getBoard());
                    if(moveUtility > bestMoveUtility){
                        bestMoveUtility = moveUtility;
                    }
                }
            }
//            for(int i=0; i < moves.length-1; i++){
////                System.out.println("Move "+i+": "+moves[i]);
//
//            }

            return bestMoveUtility;
        }
    }

    //NEED TO CONVERT THIS METHOD TO USE CHAR[][] ARRAYS INSTEAD OF GAME STATES - USING GAME STATES MESSES EVERYTHING UP APPARENTLY.

    public double minMove(GameState game, int depthLeft){


        System.out.println("minMove called. depthLeft: "+depthLeft+". Player: "+game.getPlayer());
        if(game.getStateUtility() == GameState.FIVE_IN_A_ROW || depthLeft == 0 || stopSearch){
            System.out.println("minMove RETURNING "+game.getStateUtility()+" ON TURN "+(maxDepth-depthLeft));
            return game.getStateUtility();
        }else{
            currentDepth = maxDepth-depthLeft;
            double bestMoveUtility = Double.POSITIVE_INFINITY, moveUtility;
            String[] moves = generateMoves(game);
            for(String move:moves){
                GameState newGame = new GameState(game);
                GameState gameEvaluate = applyMove(newGame, move);
                if(generatedMoves.get(gameEvaluate.getBoard()) == null){ //don't continue checking the same game state
                    moveUtility = maxMove(gameEvaluate, depthLeft-1);
                    generatedMoves.put(gameEvaluate.toString(), moveUtility);
                    if(moveUtility < bestMoveUtility){
                        bestMoveUtility = moveUtility;
                    }
                }else{
                    System.out.println("(MIN)USE EXISTING UTILITY VALUE");
                    moveUtility = generatedMoves.get(gameEvaluate.getBoard());
                    if(moveUtility > bestMoveUtility){
                        bestMoveUtility = moveUtility;
                    }
                }
            }
//            for(int i=0; i<moves.length; i++){
//
//            }

            return bestMoveUtility;
        }
    }

    public GameState applyMove(GameState g, String move){
        char[][] board = g.getBoard();
        char player = g.getPlayer();
//        System.out.println("Possible move: "+game.getPlayer()+ " to "+move);

        String[] moves = move.split(" ");
        int x = Integer.parseInt(moves[0]);
        int y = Integer.parseInt(moves[1]);

        board[x][y] = player;
        GameState newGame = new GameState(g.getBoardSize(), board , g.getEnemy());

        newGame.increaseCount();

        System.out.println("New Game Board vs Old Game Board");
        newGame.printBoard();

        return newGame;
    }


    public String getMove() {
        System.out.println("GetMove called. Best move: "+lastBestMove);
        return lastBestMove;
    }

    public String[] generateMoves(GameState gameState) {
        int boardSize = gameState.getBoardSize();
        int moveCount = gameState.getMoveCount();
        char[][] board = gameState.getBoard();
        String[] moves = new String[(boardSize * boardSize) - moveCount];
//        if (DEBUG) System.out.println("CREATING MOVE ARRAY, SIZE: " + (boardSize * boardSize - moveCount));
        //loop through everything in board, add moves where it is
//        if (DEBUG) System.out.println("Board length: " + board.length);
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == ' ') { //success!
                    moves[count++] = i + " " + j;
                }
            }
//           if (DEBUG) System.out.println("\nMoves Available: " + count);
//           if (DEBUG) System.out.println("Moves Taken: " + moveCount);
        }

//        this.moves = moves;
        return moves;
    }

}