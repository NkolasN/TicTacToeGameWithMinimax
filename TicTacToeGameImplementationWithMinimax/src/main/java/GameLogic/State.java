package GameLogic;

import constants.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static constants.Constants.GameState;
import static constants.Constants.GameState.*;
import static constants.Constants.Player.X;


/**
 * This class implements the logic representing the current state of the game
 * <p>It holds information about what moves have been played, who won if the game
 * has ended and also keeps track of the player turns</p>
 * @author  NKolasN
 */
public class State {

    private List<Coordinate> coordinates;
    private GameState gameState;
    private Constants.Player turn;
    private List<Coordinate> winningPattern;
    private boolean hasEnded;

    public State() {
        coordinates = new ArrayList<>();
        gameState = ONGOING;
        turn = X;
        winningPattern = new ArrayList<>();
        hasEnded = false;
        setUp();
    }


    /**
     * Create a coordinate object representing every cell of the 3x3 board
     */
    public void setUp() {

        Coordinate c = new Coordinate(0, 0);
        coordinates.add(c);
        c = new Coordinate(0, 1);
        coordinates.add(c);
        c = new Coordinate(0, 2);
        coordinates.add(c);
        c = new Coordinate(1, 0);
        coordinates.add(c);
        c = new Coordinate(1, 1);
        coordinates.add(c);
        c = new Coordinate(1, 2);
        coordinates.add(c);
        c = new Coordinate(2, 0);
        coordinates.add(c);
        c = new Coordinate(2, 1);
        coordinates.add(c);
        c = new Coordinate(2, 2);
        coordinates.add(c);

    }


    /**
     * Given a cell played by the player, this method updates its state to marked with 'X' or marked with 'O'
     * @param coordinate the cell played
     * @param current either X or O depending who played
     * @param next the player who will play next (X or O)
     */
    public void update(Coordinate coordinate, Constants.Player current, Constants.Player next){
        coordinates.stream()
                   .filter(c->c.getX() == coordinate.getX() && c.getY() == coordinate.getY())
                   .collect(Collectors.toList())
                   .get(0)
                   .setMarked(current);

        this.turn = next;
    }


    /**
     * Getter method returning the current status of the game i.e if it is still
     * ongoing or who has won if it has ended
     * @return ONGOING if it is still ongoing
     *         DRAW if the game ended as a draw
     *         X_WINS if player X won
     *         O_WINS if player O won
     */
    public GameState getState() {
        return this.gameState;
    }


    /**
     * The player whose turn is next
     * @return player X if X plays next or O if O playes next
     */
    public Constants.Player getTurn() {
        return this.turn;
    }


    /**
     * Given a move, this method Checks whether the game has
     * ended by checking all possible end scenarios.
     * <p> If the game has ended, the winning pattern is stored so that it is displayed
     * in a different colour.</p>
     * @param  coordinate the latest move
     * @param  xo if the player played an X or O
     * @return {X,O}_WINS if the player won
     *         ONGOING if the game is in going
     *         DRAW if there is a draw
     * */
    public GameState gameEnded(Coordinate coordinate, Constants.Player xo) {
        List<Coordinate> ar1 = new ArrayList<>();
        List<Coordinate> ar2 = new ArrayList<>();
        String x_o=xo == X ? "X" : "O"; //string representation of xo
        GameState outcomeToReturn = xo == X ? X_WINS : O_WINS; //returned only in the case where the player has won
        boolean horizontal = true;
        boolean vertical = true;
        hasEnded = true;

        /*
          Checking for a horizontal end of game
         */
        ar1.addAll(coordinates.stream()
                .filter(c-> c.getX() == coordinate.getX())
                .collect(Collectors.toList()));


        for(Coordinate c1 : ar1){
           winningPattern.add(c1);
           if(!c1.markedWith(x_o)){
               horizontal = false;
               break;
           }
        }

        if(horizontal){
            return outcomeToReturn;
        } else{
            winningPattern.clear();
        }


        /*
         * Check for a vertical end of game
         */
        ar2.addAll(coordinates.stream()
                   .filter(c->c.getY() == coordinate.getY())
                   .collect(Collectors.toList()));

        for(Coordinate c1 : ar2){
            winningPattern.add(c1);

            if(!c1.markedWith(x_o)){
                vertical = false;
                break;

            }
        }

        if(vertical) {
            return outcomeToReturn;
        } else{
            winningPattern.clear();
        }


        boolean diag =  coordinates.get(0).markedWith(x_o) && coordinates.get(4).markedWith(x_o)
                        && coordinates.get(8).markedWith(x_o);


        boolean diag2 = coordinates.get(2).markedWith(x_o)
                        && coordinates.get(4).markedWith(x_o)
                        && coordinates.get(6).markedWith(x_o);

        /*
         * Check for a diagonal end of game
         */
        if(diag) {
            winningPattern.add(coordinates.get(0));
            winningPattern.add(coordinates.get(4));
            winningPattern.add(coordinates.get(8));

            return outcomeToReturn;
        }


        if(diag2) {
            winningPattern.add(coordinates.get(2));
            winningPattern.add(coordinates.get(4));
            winningPattern.add(coordinates.get(6));

            return outcomeToReturn;
        }

        boolean draw = true;


        for(Coordinate c1 : coordinates){
            if(!c1.isMarked()){
                draw = false;

            }
        }

        if(draw) {return DRAW;}

       winningPattern.clear();

        hasEnded = false;
        return ONGOING;

     }


    /**
     * Sets the game as won by the player given in the argument
     * or set as a draw
     * @param outcome X_WINS if X won
     *               O_WINS if O won
     *               DRAW if it is a draw
     */
    public void setEnded(GameState outcome){

        if(outcome == DRAW){
            this.gameState = DRAW;
        } else{
          this.gameState=outcome;
        }


    }

    /**
     * Reset the game by unmarking every cell of the board
     * and also setting the game status as ongoing
     */
    public void reset(){
        coordinates.forEach(c->c.setUnmarked());
        gameState=ONGOING;
        turn=X;
    }

    /**
     * Getter method returning every coordinate object
     * i.e every cell representation
     * @return a list of coordinate objects representing the game
     *         board cells
     */
    public List<Coordinate> getCoordinates() {
        return this.coordinates;
    }

    /**
     * Returns the wining pattern
     * @return winning pattern if one exists, an empty list otherwise
     */
    public List<Coordinate> getWinningPattern() {
        return winningPattern;
    }


    /**
     *  Returns a boolean signifying if the game has ended
      * @return true if the game has ended, false otherwise
     */
    public boolean hasEnded() {
        return hasEnded;
    }

    /**
     * Sets the game as ended
     */
    public void setHasEnded() {
        this.hasEnded = hasEnded;
    }

}


