package GUI;

import GameLogic.State;
import constants.Constants;
import javax.swing.*;
import java.awt.*;

import static constants.Constants.GameState.*;

/**
 * This class draws the game board based on the state of the game
 * @author NkolasN
 */
class GameBoard extends JPanel {

    private State state;
    private JLabel gameStatus;


    public GameBoard(State state, JLabel gameStatus){
        this.state = state;
        this.gameStatus = gameStatus;
    }


    /**
     * Draws the game cells and updates the game status label with the current status
     * of the game.
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        setBackground(Color.BLACK);
        g.setColor(Color.WHITE);
        g.drawLine(0,100,300,100);
        g.drawLine(0,200,300,200);
        g.drawLine(100,300,100,0);
        g.drawLine(200,300,200,0);

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));


        state.getCoordinates().stream().filter(c -> c.isMarked()).forEach(c -> {

            if (c.markedWith().equals("X")) {

                if (state.getState() == X_WINS && state.getWinningPattern().contains(c)) {
                    g2.setColor(Color.RED);
                } else {
                    g2.setColor(Color.WHITE);

                }

                int x0 = c.getY() * 100 + 16;
                int y0 = c.getX() * 100 + 16;

                int x1 = (c.getY() + 1) * 100 - 16;
                int y1 = (c.getX() + 1) * 100 - 16;
                g2.drawLine(x0, y0, x1, y1);
                g2.drawLine(x1, y0, x0, y1);


            } else if (c.markedWith().equals("O")) {
                if (state.getState() == O_WINS && state.getWinningPattern().contains(c)) {
                    g2.setColor(Color.RED);
                } else {
                    g2.setColor(Color.WHITE);
                }

                int x0 = c.getY() * 100 + (100 / 6);
                int y0 = c.getX() * 100 + (100 / 6);

                g2.drawOval(x0, y0, 68, 68);

            }
        });

          gameStatus.setText("Status: " + prettyFormatState(state.getState()));

    }


    /**
     * Updates the stored state of the game (when a move is made)
     * @param state state of the game
     */
    public void setState(State state) {
        this.state = state;
    }

    /**
     * Maps the game status to a string
     * @param state the internal game state
     * @return Draw! when state = DRAW
     *         Playing when state = ONGOING
     *         X Won! when state = X_WINS
     *         O Won! when state = O_WINS
     */
    public String prettyFormatState(Constants.GameState state){

        switch(state){

            case ONGOING :  return "Playing";

            case DRAW    :  return "Draw!";

            case X_WINS  :  return "X Won!";

            case O_WINS  :  return  "O Won!";

            default : throw new IllegalArgumentException("Unknown state: " + state);
        }

    }

}

