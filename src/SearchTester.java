/**
 * Created by chandler on 2/12/17.
 */
public class SearchTester {

    public static void main(String[] args){
        char[][] testBoard = {
                {' ','x','x','x','x','o',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' ',' ',' '}};

        GameState g = new GameState(11, testBoard, 'x');
        MoveSearch ms = new MoveSearch(g);

        System.out.println(ms.getStateUtility(g.getBoard(), g.getPlayer()));
    }
}
