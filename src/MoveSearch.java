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

public class MoveSearch implements Runnable{
    private int minRow;
    private GameState gameState;
    private long time;
    private boolean maxDepthReached;
    private int maxDepth;
    protected int currentDepth;
    private String[] moves;
    private boolean DEBUG = true;


    public MoveSearch(GameState gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {

        if(DEBUG)System.out.println("MOVE SEARCH THREAD STARTED");
        String[] moves = generateMoves(gameState.getBoard(), gameState.getBoardSize(), gameState.getMoveCount());

    }


    public int getCurrentDepth() {
        return currentDepth;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public String[] getMoves(){
        return moves;
    }

    public String[] generateMoves(char[][] board, int boardSize, int moveCount){
        String[] moves = new String[(boardSize*boardSize)-moveCount];
        if(DEBUG)System.out.println("CREATING MOVE ARRAY, SIZE: "+(boardSize*boardSize-moveCount));
        //loop through everything in board, add moves where it is
        if(DEBUG)System.out.println("Board length: "+board.length);
        for(int i=0, count=0; i<board.length; i++){
            for(int j=0; j<board.length; j++){
                if(board[i][j] == ' '){ //success!
                    moves[count++] = i+" "+j;

                }
            }
            if(DEBUG)System.out.println("\nMoves Available: "+count);
            if(DEBUG)System.out.println("Moves Taken: "+moveCount);
        }


        this.moves = moves;
        return moves;
    }


}
