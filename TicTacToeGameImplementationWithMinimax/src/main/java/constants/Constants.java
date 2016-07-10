package constants;

/**
 * Contains constants such JFrame and JButton dimensions as well
 * as a few enums
 * @author NkolasN
 */
public class Constants {

    public static final int BOARD_LENGTH_HORIZONTAL = 300;
    public static final int BOARD_LENGTH_VERTICAL = 370;
    public static final int RESTART_BUTTON_WIDTH = 70;
    public static final int RESTART_BUTTON_HEIGHT = 50;
    public static final int MINIMAX_DEPTH = 9;

    /**
     * An enum representing the game status.
     */
    public enum GameState {
        ONGOING, DRAW,O_WINS, X_WINS
    }


    /**
     * An enum representing the game's two players X and O
     */
    public enum Player{
        X,O
    }

}
