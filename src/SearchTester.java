import java.util.List;

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


        char[][] tb2= {
                {'x',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '},
                {' ',' ',' ',' ',' ',' ',' ',' ',' '}};

        GameState g = new GameState(9, tb2, 'x');

//        System.out.println(GameState.getStateUtility(g.getBoard(), 'x'));
        MoveSearch ms = new MoveSearch(g);

        String[] moves = ms.generateMoves(tb2);
        List<char[][]> boards = ms.generateBoards(tb2, 'o', moves);
        int count = 0;
        for(char[][] b : boards){
            System.out.println("Board "+(count++)+": ");
            GameState.printBoard(b);
        }

    }

}
