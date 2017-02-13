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
        int lastEnemyEncounteredCol, lastEnemyEncounteredRow;
        int encounteredEnemy;


        for(int row=0; row<boardLength; row++){
            lastEnemyEncounteredCol = -1;
            lastEnemyEncounteredRow = -1;
            for(int col=0; col<boardLength; col++){

                if(board[row][col] == enemy) {
                    lastEnemyEncounteredCol = col;//keep track of the last encountered enemy
                    lastEnemyEncounteredRow = row;
                }


                //If we find the string contains the player
                if(board[row][col] == player){


//                    encounteredEnemy = -1;
//                    //====================CHECK TO THE RIGHT====================
//                    if(col <= boardLength-5){//to be sure there can actually be a 5-in-a-row to this direction
//
//                        count = 1; //Sum of how many of our players we encounter in the next 4 spaces
//                        for(int x=col+1; x<col+5; x++){
//                            if(board[row][x] == player){
//                                count++;
//                            }else if(board[row][x] == enemy){
//                                encounteredEnemy = x;
//                                break;
//                            }
//                        }
//
//                        if(count < 3 || count == 5){
//                            evaluation += getPointsToAdd(count);
//                            System.out.println("[horiz]BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+getPointsToAdd(count));
//                        }
//
//                        else if(count == 3){
//                          if(encounteredEnemy == -1){
//                              evaluation += THREES_POINTS;
//                              System.out.println("[horiz(1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
//                          }else if(lastEnemyEncounteredCol > -1){//we encountered an enemy before seeing our player
//                              if(col-1 >= 0 && encounteredEnemy == col+4){//we have enough room to make a 4, check to the left one to see if we can make a 5 (-O-X-XXO--)
//                                  if(board[row][col-1] != enemy){
//                                      evaluation += THREES_POINTS;
//                                      System.out.println("[horiz](2)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
//                                  }
//                              }else if(col-2 >= 0 && encounteredEnemy == col+3){//we are stuck on 3, check to the left 2 to see if we can make it a 5
//                                  evaluation += THREES_POINTS;
//                                  System.out.println("[horiz](3)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
//                              }
//                          }
//                        }
//
//
//                        else if( count == 4 && col-1 < 0 && encounteredEnemy == -1){//havent encountered an enemy before seeing our player
//                            System.out.println("[horiz](1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
//                            evaluation += FOURS_POINTS;
//                        }else if( encounteredEnemy > -1 && (col+5 >= boardLength || col-1 < 0 ))  {
//                            //enemy is blocking us at the edge of the board (OXXXX)
//                            System.out.println("[horiz]BLOCKING ON EDGE!!!!!!");
//                        }
//                        else { //check for the straight four
//                            String rowString = new String(board[row], col-1, 6); //Create string representation to check for straight 4
//                            if(isStraightFour(rowString, player)){//If it is a straight 4
//                                System.out.println("[horiz](1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+STRAIGHT_FOUR_POINTS);
//                                evaluation += STRAIGHT_FOUR_POINTS;
//                            }
//                            else if(encounteredEnemy == -1){//If it is possible to have a straight 4, and we have not encountered an enemy while searching
//                                evaluation += FOURS_POINTS;
//                                System.out.println("[horiz](2)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
//                            }else{ //If it is possible to have a straight 4, but we have encountered an enemy while searching, check if there is room on left
//                                if(board[row][col-1] != enemy){
//                                    evaluation += FOURS_POINTS;
//                                    System.out.println("[horiz](3)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
//                                }
//                            }
//                        }
//
//                    }//FINISH CHECKING TO THE RIGHT




                    encounteredEnemy = -1;
                    //====================CHECK BELOW====================
                    if(row <= boardLength-5){//to be sure there can actually be a 5-in-a-row to this direction

                        count = 1; //Sum of how many of our players we encounter in the next 4 spaces
                        for(int x=row+1; x<row+5; x++){
                            if(board[x][col] == player){
                                count++;
                            }else if(board[x][col] == enemy){
                                encounteredEnemy = x;
                                break;
                            }
                        }

                        if(count < 3 || count == 5){
                            evaluation += getPointsToAdd(count);
                            System.out.println("[down]BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+getPointsToAdd(count));
                        }
                        else if(count == 3){
                            if(encounteredEnemy == -1){
                                evaluation += THREES_POINTS;
                                System.out.println("[down](1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
                            }else if(lastEnemyEncounteredRow > -1){//we encountered an enemy before seeing our player
                                if(row-1 >= 0 && encounteredEnemy == row+4){//we have enough room to make a 4, check above to see if we can make a 5 (-O-X-XXO--)
                                    if(board[row-1][col] != enemy){
                                        evaluation += THREES_POINTS;
                                        System.out.println("[down](2)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
                                    }
                                }else if(row-2 >= 0 && encounteredEnemy == row+3 ){//we are stuck on 3, check to the left 2 to see if we can make it a 5
                                    evaluation += THREES_POINTS;
                                    System.out.println("[down](3)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+THREES_POINTS);
                                }
                            }
                        }



                        else if( count == 4 && row-1 < 0 && encounteredEnemy == -1){//havent encountered an enemy before seeing our player
                            System.out.println("[down](0)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
                            evaluation += FOURS_POINTS;
                        }else if( encounteredEnemy > -1 && (row+5 >= boardLength || row-1 < 0 ))  {
                            //enemy is blocking us at the edge of the board (OXXXX)
                            System.out.println("[down]BLOCKING ON EDGE!!!!!!");
                        }
                        else { //check for the straight four
                            char[] newChars = new char[6];

                            for(int b=row-1, i=0; b<boardLength && b<row+5; b++, i++){
                                newChars[i] = board[b][col];
                            }
                            String rowString = new String(newChars);

                            if(isStraightFour(rowString, player)){//If it is a straight 4
                                System.out.println("[down](1)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+STRAIGHT_FOUR_POINTS);
                                evaluation += STRAIGHT_FOUR_POINTS;
                            }
                            else if(encounteredEnemy == -1){//If it is possible to have a straight 4, and we have not encountered an enemy while searching
                                evaluation += FOURS_POINTS;
                                System.out.println("[down](2)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
                            }else{ //If it is possible to have a straight 4, but we have encountered an enemy while searching, check if there is room on left
                                if(board[row-1][col] != enemy){
                                    evaluation += FOURS_POINTS;
                                    System.out.println("[down](3)BOARD["+row+"]["+col+"]: ADDED UTILITY VALUE OF: "+FOURS_POINTS);
                                }
                            }
                        }

                    }//FINISH CHECKING BELOW


                    //====================CHECK ABOVE====================
                    if(row >= 4){//to be sure there can actually be a 5-in-a-row to this direction

                    }

                    //====================CHECK BOTTOM-RIGHT DIAGONALLY====================
                    if(col+4 < boardLength && row+4 < boardLength){//to be sure there can actually be a 5-in-a-row to this direction

                    }


                    //====================CHECK TOP-RIGHT DIAGONALLY====================
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
