/**
 * Created by chandler on 2/12/17.
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

        GameState g = new GameState(11, testBoard, 'x');

        System.out.println(g.getStateUtility());
    }
}
