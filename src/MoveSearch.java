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

import java.util.HashSet;

public class MoveSearch implements Runnable{

    private static final double FIVE_IN_A_ROW=Double.POSITIVE_INFINITY;
    private static final double STRAIGHT_FOUR_POINTS=1000;
    private static final double FOURS_POINTS=100;
    private static final double THREES_POINTS=25;
    private static final double TWOS_POINTS=5;
    private static final double ONES_POINTS=1;

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


    private boolean isFour(){


        return false;
    }

    private boolean isStraightFour(String in, char player){
        String straightFour = " "+player+player+player+player+" ";
        if(DEBUG)System.out.println("IsStraightFour? "+in.replaceAll(" ", "-"));
        return in.equals(straightFour);
    }




    //Single: 1pt
    //Double: 5pts
    //Threes: 25pts
    //The Four: 100pts
    //Straight Four: 1000pts
    //5 in a row: INFINITY

    //    0 1 2 3 4 5 6 7 8 9
    // 0  0 0 X X X X 0 0 0 0
    // 1  0 0 X 0 X X 0 0 0 0
    // 2  0 0 X X X 0 X X X 0
    // 3  0 0 X X X 0 X X X 0
    // 4  0 0 X X X 0 X X X 0
    // 5  0 0 X X X 0 X X X 0
    // 6  0 0 X X X 0 X X X 0
    // 7  0 0 X X X 0 X X X 0
    // 8  0 0 X X X 0 X X X 0
    // 9  0 0 X X X 0 X X X 0



    //Returns how much to increase the evaluation based on the number of items in a row
    private double getPointsToAdd(int val){
        switch(val){
            case 1:
                return ONES_POINTS;
            case 2:
                return TWOS_POINTS;
            case 3:
                return THREES_POINTS;
            case 5:
                return FIVE_IN_A_ROW;
            default:
                return 0;
        }
    }

    public double getStateUtility(char[][] board, char player){
        char enemy = (player == 'x')? 'o':'x';
        double evaluation = 0.0;
        int boardLength = board.length;
        int count;
        int lastEnemyEncountered;
        boolean encounteredEnemy;

        for(int row=0; row<boardLength; row++){
            lastEnemyEncountered = -1;
            for(int col=0; col<boardLength; col++){

                if(board[row][col] == enemy) lastEnemyEncountered = col;//keep track of the last encountered enemy

                //If we find the string contains the player
                if(board[row][col] == player){

                    encounteredEnemy = false;
                    //CHECK TO THE RIGHT
                    if(col <= boardLength-5){//to be sure there can actually be a 5-in-a-row to this direction

                        count = 1;
                        for(int x=col+1; x<col+5; x++){//Sum of how many of our players we encounter in the next 4 spaces
                            if(board[row][x] == player){
                                count++;
                            }else if(board[row][x] == enemy){
                                encounteredEnemy = true;
                                break;
                            }
                        }

                        //
                        if(count < 3 || count == 5){
                            evaluation += getPointsToAdd(count);
                            System.out.println("BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+getPointsToAdd(count));
                        }

                        else if(count == 3){
                          if(!encounteredEnemy){
                              evaluation += THREES_POINTS;
                              System.out.println("(1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
                          }else if(col-2 >= 0 && lastEnemyEncountered > -1){//we encountered an enemy before seeing our player & 2 spaces to the right is not off the board.
                              if(board[row][col-1] != enemy && board[row][col-2] != enemy){ //If the spot before AND two spots before are not enemies, add the point value.
                                  evaluation += THREES_POINTS;
                                  System.out.println("(2)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
                              }
                          }
                        }


                        else if(count == 4 && (col-1 < 0 || col+5 >= boardLength) ){
                            System.out.println("(1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
                            evaluation += FOURS_POINTS;
                        }
                        else { //check for the straight four
                            String rowString = new String(board[row], col-1, 6); //Create string representation to check for straight 4
                            if(isStraightFour(rowString, player)){//If it is a straight 4
                                System.out.println("(1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+STRAIGHT_FOUR_POINTS);
                                evaluation += STRAIGHT_FOUR_POINTS;
                            }
                            else if(!encounteredEnemy){//If it is possible to have a straight 4, and we have not encountered an enemy while searching
                                evaluation += FOURS_POINTS;
                                System.out.println("(2)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
                            }else{ //If it is possible to have a straight 4, but we have encountered an enemy while searching
                                if(board[row][col-1] != enemy){
                                    evaluation += FOURS_POINTS;
                                    System.out.println("(3)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
                                }
                            }
                        }

                    }//FINISH CHECKING TO THE RIGHT



                    //Check below
                    if(row <= boardLength-5){//to be sure there can actually be a 5-in-a-row to this direction

                    }


                    //Check to the top
                    if(row >= 4){//to be sure there can actually be a 5-in-a-row to this direction

                    }

                    //Check bottom-right diagonal
                    if(col+4 < boardLength && row+4 < boardLength){//to be sure there can actually be a 5-in-a-row to this direction

                    }


                    //Check top-right diagonal
                    if(col+4 < boardLength && row-4 >= 0){//to be sure there can actually be a 5-in-a-row to this direction

                    }

                }
            }
        }

        return evaluation;
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
