import java.util.List;

/**
 * Created by chandler on 2/12/17.
 * Used to test Searching Functionality.
 */

public class SearchTester {

    public static void main(String[] args){
        char[][] testBoard = {
                {' ',' ','x',' ',' ',' ',' ',' ',' ',' ','x'},
                {' ',' ','x',' ',' ',' ',' ',' ',' ','x',' '},
                {' ',' ','x',' ',' ',' ',' ',' ','o',' ',' '},
                {' ',' ','x',' ',' ',' ',' ','x',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ','x',' ',' ',' ',' '},
                {' ','x','o','x','x','x','o','x',' ',' ',' '},
                {' ',' ','x',' ','x','x','o',' ','x','o',' '},
                {' ',' ',' ',' ','o',' ',' ',' ',' ','x',' '},
                {' ',' ',' ',' ','x',' ','x',' ',' ',' ',' '},
                {' ',' ',' ',' ','X',' ',' ',' ',' ','x',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}};


        char[][] tb2= {
                {'x',' ',' ',' ',' ',' ','o',' ',' '},
                {' ',' ',' ',' ',' ',' ','o',' ',' '},
                {' ',' ',' ',' ',' ',' ','o',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ','x','o',' ',' '},
                {' ',' ',' ',' ',' ','x',' ',' ',' '},
                {' ',' ',' ',' ',' ','x',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ','x',' ',' ',' '}};

        GameState g = new GameState(9, tb2, 'x');

//        System.out.println(GameState.getStateUtility(g.getBoard(), 'x'));
        MoveSearch ms = new MoveSearch(g);

        String[] moves = ms.generateMoves(tb2);
        System.out.println("Best Move: "+ms.miniMax( tb2, 'o'));


    }

}
