package gamelogic;
import constants.Constants;
import static constants.Constants.Player.O;
import static constants.Constants.Player.X;

/**
 * A class representing each cell in the game, identified as a pair of coordinates (x,y) relative to the board.
 * <p>It holds information about the numerical values of the coordinates and
 * if the coordinate is unmarked  or marked with an 'X' or 'O' </p>
 * @author NkolasN
 */
public class Coordinate{
    private int x;
    private int y;
    private boolean isMarked;
    private String markedWith;


    public Coordinate(int x, int y){
           this.x = x;
           this.y = y;
           isMarked = false;
           markedWith = " ";
    }

    /**
     * Returns the x coordinate which is denoted by the position of the cell
     * relative to the x-axis
     * @return the x coordinate
     */
    public int getX(){
        return this.x;
    }

    /**
     * Returns the y coordinate which is denoted by the position of the cell
     * relative to the y-axis
     * @return the y coordinate
     */
    public int getY(){
        return this.y;
    }

    /**
     * Whether or not the cell is marked with an 'X' or 'O'
     * @return true if the cell is marked with an 'X' or 'O', false otherwise
     */
    public boolean isMarked(){

        return this.isMarked;
    }

    /**
     * Sets the cell as marked with an 'X' or 'O'
     * @param xo whether it should be marked with an 'X' or 'O'
     */
    public void setMarked(Constants.Player xo){

        if (!isMarked) {

            if (!xo.equals(X) && !xo.equals(O)) {
                throw new IllegalArgumentException("Must be marked with X or O, " + "given " + xo);
            }

            isMarked = true;
            markedWith = xo == X ? "X" : "O";
        }
    }

    /**
     * Returns the symbol that the cell is marked with
     * @return "X" if the cell is marked with an 'X' and "O" if the cell is marked with an 'O'.
     *          Returns an empty string otherwise
     */
    public String markedWith(){
        return markedWith;
    }

    /**
     * Checks if the cell has been marked with a specified symbol
     * @param x_o the symbol to check if the cell is marked with
     * @return true if the cell is marked with that symbol, false otherwise
     */
    public boolean markedWith(String x_o){
        return markedWith.equals(x_o);
    }

    /**
     * Sets a cell as unmarked
     */
    public void setUnmarked(){
        isMarked = false;
        markedWith = "";

    }

    /**
     * Defines equality among cells. A cell is equal to another iff they
     * have the same coordinate representation
     * @param o the other cell
     * @return true if the other cell has the same x and y coordinates, false otherwise
     */
    @Override
    public boolean equals(Object o){
        int x = ((Coordinate)o).getX();
        int y = ((Coordinate)o).getY();
        return this.x == x && this.y == y;
    }

}
