/*
    Southern Oregon University - CS455 Artificial Intelligence - Lab 2 - Gomoku

    Authors: Chandler Severson, Janelle Bakey, Gabriela Navarrete
    Date: 2/10/2017
    Class: GameState.java
        Desc: Object that represents the current state of the game.
 */

import java.io.*;
import java.util.Arrays;

public class GameState {
    private String status;
    private String serverData;
    private int boardSize;
    private char player;
    private char[][] board;
    private boolean reEval;
    private boolean DEBUG = false;
    private GomokuConnector connector;
    private PrintWriter output = null;
    private int moveCount;


    public GameState() {
        connector = GomokuConnector.getInstance();
        update();
    }

    public GameState(int boardSize, char[][] board, char player){
        this.boardSize = boardSize;
        this.board = board;
        this.player = player;
    }

    /**
     * Update the GameState object with the most recent data
     * @return the updated GameState object
     */
    public GameState update() {
        parseGameState(connector.getInputReader());
        return this;
    }

    /**
     * Parse Game state from input
     * @param input reader for the gomoku socket
     */
    private void parseGameState(BufferedReader input) //get data from server
    {
        int i = 0, k = 0, count = 0;
        char[] temp;

        try {
            if(input.ready()) {
                if(DEBUG)System.out.println("start reading");
                do{
                    serverData = input.readLine();//read data from server
                    if(DEBUG)System.out.println("["+i+"] "+serverData);
                    if (i == 0) //reading the first line for game status 'continuing, 'win, 'lose, 'draw, 'forfeit-time, 'forfeit-move'
                    {
                        status = serverData;
                        reEvaluate(serverData);
                    } else if (i == 1 || i <= boardSize) { //series of lines of characters representing each row of the current SQUARE-board; characters one of: "x", "o", or " " (space)
                        if(i == 1){
                            boardSize = serverData.length();
                            board = new char[boardSize][boardSize];
                        }
                        temp = serverData.toCharArray();
                       if (DEBUG) {for (int b = 0; b < temp.length; b++) {System.out.println("[" + temp[b] + "](" + b + ")");}}
                        for (int j = 0; j < boardSize; j++) {
                            board[k][j] = temp[j];
                            if(temp[j] != ' ') count++;
                        }
                        k++;

                    } else if (i == (boardSize + 1)) {
                        moveCount = count; //set move count after going through all spaces.
                        player = serverData.charAt(0); //save player as x (black) or o (white)
                        if(DEBUG) System.out.println("Player: " + player);
                    }

                    i++;

                }while(input.ready());
            }
        } catch (IOException e) {
            System.err.println("Couldn't get I/O!");
            e.printStackTrace();
            System.exit(1);
        }
        if(DEBUG)System.out.println("finish reading");
    }


    private void reEvaluate(String s) //game-status as one of 'continuing, 'win, 'lose, 'draw, 'forfeit-time, 'forfeit-move
    {
        if (status.equals("continuing")) {
            reEval = true;
        } //need to read board placement again
        else if (status.equals("forfeit-time")) {
            reEval = false;
        } //does not need to read board placement, can use last saved
        else if (status.equals("forfeit-time")) {
            reEval = false;
        } //does not need to read board placement, can use last saved
    }

    /**
     * send a move to the gomoku server
     * @param play the desired play, 'column row' separated by space
     */
    public void makePlay(String play) {
        if (output == null) {
            output = connector.getOutputWriter();
        }

        System.out.println("MakePlay called: "+play);
        String s = play+"\n";
        output.print(s);
        output.flush();

        moveCount++;
    }

    /**
     *
     * @return the number of moves that have been played by both players combined
     */
    public int getMoveCount() {
        return moveCount;
    }

    /**
     *
     * @return the character-array gameboard
     */
    public char[][] getBoard() {
        return this.board;
    }

    /**
     *
     * @return the size of the gameboard
     */
    public int getBoardSize() {
        return this.boardSize;
    }

    /**
     *
     * @return the 'color' of the player (x/o)
     */
    public char getPlayer() {
        return this.player;
    }


    /**
     * If our player is 'o', we will move on odd turns,
     * if it is 'x', we will move on even turns.
     *
     * @return whether it is the AI's turn.
     */
    public boolean myTurn(){
        switch(player){
            case 'o':
                if(moveCount == 1 || moveCount % 2 == 1) return true;
                else return false;

            case 'x':
                if(moveCount == 0 || moveCount % 2 == 0) return true;
                else return false;
        }

        return false;
    }

    public boolean equalsBoard(char[][] otherBoard){
       return Arrays.deepEquals(board, otherBoard);
    }

    /**
     *
     * @return the current game status
     */
    public String getStatus() {
        return this.status;
    }

}
