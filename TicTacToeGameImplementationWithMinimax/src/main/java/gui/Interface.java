package gui;

import gamelogic.Coordinate;
import gamelogic.Minimax;
import gamelogic.State;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.util.stream.Collectors;

import static constants.Constants.*;
import static constants.Constants.GameState.*;
import static constants.Constants.Player.O;
import static constants.Constants.Player.X;


/**
 * This class creates the JFrame, adds the game board, and updates
 * the state when a move is made
 * @author NKolasN
 */
public class Interface{

    private State state;
    private JLabel gameStatus;
    GameBoard gameBoard;
    JFrame gameWindow;
    Container container;

    public Interface(){
        gameWindow = new JFrame();
        state = new State();
        gameStatus = new JLabel();
        gameBoard = new GameBoard(state,gameStatus);
        container = gameWindow.getContentPane();;
        start();
    }

    /**
     * Creates the JFrame, adds the game board and sets up action listeners
     * which update the state of the game when a move is made
     */
    public void start(){

        container.setLayout(new BorderLayout());
        container.add(gameBoard, BorderLayout.CENTER);
        gameStatus.setForeground(Color.WHITE);
        Font serif = new Font("Serif", Font.ITALIC, 15);
        gameStatus.setFont(serif);
        gameStatus.setBorder(BorderFactory.createEmptyBorder(2, 5, 4, 5));
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        p.add(gameStatus,BorderLayout.WEST);
        JButton restartButton = new JButton("Restart");
        restartButton.setBorder(BorderFactory.createEmptyBorder());
        restartButton.setForeground(Color.WHITE);
        restartButton.setOpaque(false);
        restartButton.setContentAreaFilled(false);
        restartButton.setBorderPainted(false);
        restartButton.setFocusPainted(false);
        restartButton.setFont(serif);

        restartButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                restartButton.setForeground(Color.red);
            }
            public void mouseExited(MouseEvent evt) {
                restartButton.setForeground(Color.WHITE);
            }

        });

        restartButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                state.reset();
                gameWindow.repaint();
            }
        });

        restartButton.setPreferredSize(new Dimension(RESTART_BUTTON_WIDTH, RESTART_BUTTON_HEIGHT));
        p.add(restartButton,BorderLayout.EAST);
        p.setBackground(Color.black);
        container.add(p,BorderLayout.PAGE_END);
        gameWindow.pack();
        gameWindow.setTitle("Tic-Tac-Toe");
        gameWindow.setSize(new Dimension(BOARD_LENGTH_HORIZONTAL ,BOARD_LENGTH_VERTICAL));
        gameWindow.setVisible(true);
        gameWindow.setResizable(false);
        gameBoard.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                int selectedRow = e.getY() / 100;
                int selectedColumn = e.getX() / 100;

                try {
                    if (state.getState() == ONGOING &&
                            selectedRow >= 0 && selectedColumn >= 0 &&
                            !clickedOnMarked(new Coordinate(selectedRow,selectedColumn))) {

                        if (state.getTurn() == X) {

                            InputStream in = getClass().getClassLoader().getResourceAsStream("moveSound.wav");
                            AudioStream audioStream = new AudioStream(in);
                            AudioPlayer.player.start(audioStream);
                            AudioPlayer.player.stop(audioStream);
                            state.update(new Coordinate(selectedRow, selectedColumn),X, O);
                            AudioPlayer.player.start(audioStream);

                            GameState newState = state.gameEnded(new Coordinate(selectedRow,selectedColumn),X);
                            switch(newState){
                                case  X_WINS : state.setEnded(X_WINS); break;
                                case DRAW : state.setEnded(DRAW);

                            }

                            if(!state.hasEnded()){
                                Coordinate aiMove=(Coordinate)new Minimax(state).minimax(MINIMAX_DEPTH,O).get(1);
                                state.update(aiMove, O, X);
                                if(state.gameEnded(aiMove,O) == O_WINS) {
                                    state.setEnded(O_WINS);
                                } else if (state.gameEnded(aiMove,O) == DRAW) {
                                    state.setEnded(DRAW);
                                }
                            }


                        }
                    }

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                gameBoard.setState(state);
                gameBoard.repaint();
                gameWindow.revalidate();

            }
        });
    }

    /**
     * Checks if the game board cell which was clicked has already been marked with 'X' or 'O'
     * @param  coordinateClicked the game board cell which was clicked
     * @return true if it is marked, false otherwise
     */
    public boolean clickedOnMarked(Coordinate coordinateClicked) {
       return state.getCoordinates().stream()
                              .filter(c->c.getX() == coordinateClicked.getX() && c.getY() == coordinateClicked.getY())
                              .collect(Collectors.toList())
                              .get(0)
                              .isMarked();
    }

}