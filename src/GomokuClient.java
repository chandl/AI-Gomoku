/*
    Southern Oregon University - CS455 Artificial Intelligence - Lab 2 - Gomoku

    Authors: Chandler Severson, Janelle Bakey, Gabriela Navarrete
    Date: 2/10/2017
    Class: GomokuClient.java
        Desc: The driver program for the Gomoku-playing AI bot. Also times execution of search (Timer subclass)
 */



/*
 * connector (after getting two player connections) sends three groups of data:
;;  a. game-status as one of 'continuing, 'win, 'lose, 'draw, 'forfeit-time, 'forfeit-move
;;  b. series of lines of characters representing each row of the current SQUARE-board; characters one of: "x", "o", or " " (space)
;;  c. color to play: either "x" or "o"
3. client sends a move:
;;  a. a single line consisting of space separated row and column values

Before the 2 seconds are passed, your program must send a move as a single line with two
numbers representing the (zero-based) row and column of its selected move. The two numbers must be separated by a single space.
 */


import java.util.Random;

public class GomokuClient
{
    private static final int GOMOKU_PORT = 17033;
    private static final String GOMOKU_HOST = "localhost";
    private static double TIME_LIMIT = 19.8;
    private static boolean DEBUG = true;
    private static Random rand = new Random();
    private static GomokuConnector connector =  GomokuConnector.newInstance(GOMOKU_HOST, GOMOKU_PORT);
    GameState currentState;

    public GomokuClient() {
        // TODO Auto-generated constructor stub
    }

    public static void main(String[] args)
    {
        GameState currentState = new GameState();
        MoveSearch moveSearch;
        Timer timer;
        int lastMoveCount = 0;

        if(DEBUG)System.out.println("Color: "+currentState.getPlayer() + ". Turn: "+currentState.myTurn());

        while(currentState.getStatus().equals("continuing") ){
            do{
                try{
//                    if(currentState.getMoveCount() == 0) connector.makePlay(currentState.getBoardSize()/2 + " " + currentState.getBoardSize()/2);
                    lastMoveCount = currentState.getMoveCount();
                    if(currentState.myTurn()){ //currently this client's turn
                        moveSearch = new MoveSearch(currentState);
                        Thread searchThread = new Thread(moveSearch, "moveSearch");
                        timer = new Timer(moveSearch);
                        Thread timerThread = new Thread(timer, "searchTimer");

                        searchThread.start(); //search for moves
                        timerThread.start(); //this will stop the search thread after Xsec
                        timerThread.join();

                        //choose a random play (for now)
                        //currentState.makePlay(moveSearch.getMoves()[rand.nextInt(moveSearch.getMoves().length)]);

                        connector.makePlay(moveSearch.getMove());
                    }

                } catch(InterruptedException ie){ie.printStackTrace();}
            } while(currentState.update().getStatus().equals("continuing") && lastMoveCount != currentState.getMoveCount());
        }

    }

    //Timer thread to keep track of runtime of search
    static class Timer implements Runnable{
        Thread searchThread;
        MoveSearch moveSearch;

        public Timer(Thread searchThread){
            this.searchThread = searchThread;
        }

        public Timer(MoveSearch ms){moveSearch=ms;}

        @Override
        public void run() {
            try{
                if(DEBUG)System.out.println("TIMER THREAD STARTED");
                Thread.sleep(((long)TIME_LIMIT * 1000) - 10); //give 10ms to send move
//                searchThread.interrupt();
                moveSearch.setStopSearch(true);
                if(DEBUG)System.out.println("Timer: " + TIME_LIMIT + " SECOND(S) ARE UP!");
            }catch(InterruptedException ex){}
        }
    }


}

