package gamelogic;

import constants.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static constants.Constants.Player.O;
import static constants.Constants.Player.X;

/**
 * This class implements a minimax player for a tic-tac toe game
 * <p>It returns a move based on the game state</p>
 * @author NkolasN
 */
public class Minimax {

    private State state;


    public Minimax(State state) {

        this.state = state;
    }


    /**
     * The implementation of the minimax AI.
     * <p> The AI player attempts to choose a move which minimises the other player's max score
     * by assigning a numerical value to each possible future state of the game</p>
     * <p>The numerical value of the score is determined by a heuristic evaluation function returning the
     * highest score if the minimising player (the AI) is going to win or if it is to
     * lose if a specific move is not played</p>
     * @param depthOfSearch the depth of the search
     * @param player whose turn it is
     * @return a coordinate representing the move of the AI player
     */
    public List<Object> minimax(int depthOfSearch, Constants.Player player) {

        List<Coordinate> possibleMoves = getMoves();
        int maxScore = (player == O) ? -999999999 : 999999999;
        int currentScore;
        Coordinate bestMove = new Coordinate(0,0);


        if (possibleMoves.isEmpty() || depthOfSearch == 0) {
            maxScore = calculateScore();

        } else {

            for (Coordinate c : possibleMoves) {

                for(Coordinate c2 : state.getCoordinates()){
                    if(c.equals(c2)){
                        c2.setMarked(player);
                        break;
                    }
                }

                if (player == O) {
                    currentScore = (int) minimax(depthOfSearch - 1, X).get(0);
                    if (currentScore > maxScore) {
                        maxScore = currentScore;
                        bestMove = c;
                    }
                } else {
                    currentScore = (int) minimax(depthOfSearch - 1, O).get(0);
                    if (currentScore < maxScore) {
                        maxScore = currentScore;
                        bestMove = c;
                    }
                }

                for(Coordinate c2 : state.getCoordinates()){
                    if(c.equals(c2)){
                        c2.setUnmarked();
                        break;
                    }
                }
            }
        }

        List<Object> ar = new ArrayList();
        ar.add(new Integer(maxScore));
        ar.add(bestMove);


        return ar;
    }


    /**
     * Calculates the total score, given a game state.
     * <p> The method first creates lists representing the rows,
     * columns and diagonals and then returns a total score out
     * of individual scores calculated from every line</p>
     * @return the total score
     */
    private int calculateScore() {

         /* Create three lists representing the  rows */

        List<Coordinate> lineRow0 = new ArrayList<>();
        lineRow0.addAll(IntStream.range(0,3)
                        .mapToObj(i->state.getCoordinates().get(i))
                        .collect(Collectors.toList())
        );

        List<Coordinate> lineRow1 = new ArrayList<>();
        lineRow1.addAll(IntStream.range(3,6)
                        .mapToObj(i->state.getCoordinates().get(i))
                        .collect(Collectors.toList())
        );


        List<Coordinate> lineRow2 = new ArrayList<>();
        lineRow2.addAll(IntStream.range(6,9)
                        .mapToObj(i->state.getCoordinates().get(i))
                        .collect(Collectors.toList())
        );

        /* Create three lists representing the  columns */

        List<Coordinate> lineColumn0 = new ArrayList<>();
        lineColumn0.addAll(IntStream.iterate(0,i->i+3).limit(3)
                           .mapToObj(i->state.getCoordinates().get(i))
                           .collect(Collectors.toList())
        );

        List<Coordinate> lineColumn1 = new ArrayList<>();
        lineColumn1.addAll(IntStream.iterate(1,i->i+3).limit(3)
                           .mapToObj(i->state.getCoordinates().get(i))
                           .collect(Collectors.toList())
        );

        List<Coordinate> lineColumn2 = new ArrayList<>();
        lineColumn2.addAll(IntStream.iterate(2,i->i+3).limit(3)
                           .mapToObj(i->state.getCoordinates().get(i))
                           .collect(Collectors.toList())
        );


        /* Create two lists representing the diagonals */

        List<Coordinate> diag0 = new ArrayList<>();

        diag0.addAll(IntStream.iterate(0,i->i+4).limit(3)
                     .mapToObj(i->state.getCoordinates().get(i))
                     .collect(Collectors.toList())
        );

        List<Coordinate> diag1 = new ArrayList<>();
        diag1.addAll(IntStream.iterate(2,i->i+2).limit(3)
                     .mapToObj(i->state.getCoordinates().get(i))
                     .collect(Collectors.toList())
        );


        int score;

        score  = getSequenceScore(lineRow0)
               + getSequenceScore(lineRow1)
               + getSequenceScore(lineRow2)
               + getSequenceScore(lineColumn0)
               + getSequenceScore(lineColumn1)
               + getSequenceScore(lineColumn2)
               + getSequenceScore(diag0)
               + getSequenceScore(diag1);

        return score;
    }

    /**
     * Given a sequence of cells, this method returns a numerical score
     * @param c the sequence of cells
     * @return a score drawn from the contents of the sequence
     *         the score is the highest if a player is to win if a winning move is played
     *         or if the player loses if a move is not played
     */
    private int getSequenceScore(List<Coordinate> c) {
        int score = 0;

        score = c.get(0).markedWith("O") ? 1 : -1;


        if (c.get(1).markedWith("O")) {
            if (score == 1) {
                score += 10;
            } else if (score == -1) {
                return 0;
            } else {
                score = 1;
            }
        } else if (c.get(1).markedWith("X")) {
            if (score == -1) {
                score -= 10;
            } else if (score == 1) {
                return 0;
            } else {
                score = -1;
            }
        }


        if (c.get(2).markedWith("O")) {
            if (score > 0) {
                score += 100;
            } else if (score < 0) {
                return 0;
            } else {
                score = 1;
            }
        } else if (c.get(2).markedWith("X")) {
            if (score < 0) {
                score *= 100;
            } else if (score > 1) {
                return 0;
            } else {
                score = -1;
            }
        }
        return score;
    }

    /**
     * Returns all the possible moves
     * @return an empty list if the game is over,
     *         a list of non played game board cells otherwise
     */
    private List<Coordinate> getMoves() {

        List<Coordinate> moves = new ArrayList<>();

        if(state.getCoordinates().get(0).markedWith("X") && state.getCoordinates().get(1).markedWith("X") && state.getCoordinates().get(2).markedWith("X") ||
           state.getCoordinates().get(0).markedWith("O") && state.getCoordinates().get(1).markedWith("O") && state.getCoordinates().get(2).markedWith("O") ||
           state.getCoordinates().get(3).markedWith("X") && state.getCoordinates().get(4).markedWith("X") && state.getCoordinates().get(5).markedWith("X") ||
           state.getCoordinates().get(3).markedWith("O") && state.getCoordinates().get(4).markedWith("O") && state.getCoordinates().get(5).markedWith("O") ||
           state.getCoordinates().get(6).markedWith("X") && state.getCoordinates().get(7).markedWith("X") && state.getCoordinates().get(8).markedWith("X") ||
           state.getCoordinates().get(6).markedWith("O") && state.getCoordinates().get(7).markedWith("O") && state.getCoordinates().get(8).markedWith("O") ||
           state.getCoordinates().get(0).markedWith("X") && state.getCoordinates().get(3).markedWith("X") && state.getCoordinates().get(6).markedWith("X") ||
           state.getCoordinates().get(0).markedWith("O") && state.getCoordinates().get(3).markedWith("O") && state.getCoordinates().get(6).markedWith("O") ||
           state.getCoordinates().get(1).markedWith("X") && state.getCoordinates().get(4).markedWith("X") && state.getCoordinates().get(7).markedWith("X") ||
           state.getCoordinates().get(1).markedWith("O") && state.getCoordinates().get(4).markedWith("O") && state.getCoordinates().get(7).markedWith("O") ||
           state.getCoordinates().get(2).markedWith("X") && state.getCoordinates().get(5).markedWith("X") && state.getCoordinates().get(8).markedWith("X") ||
           state.getCoordinates().get(2).markedWith("O") && state.getCoordinates().get(5).markedWith("O") && state.getCoordinates().get(8).markedWith("O") ||
           state.getCoordinates().get(0).markedWith("X") && state.getCoordinates().get(4).markedWith("X") && state.getCoordinates().get(8).markedWith("X") ||
           state.getCoordinates().get(0).markedWith("O") && state.getCoordinates().get(4).markedWith("O") && state.getCoordinates().get(8).markedWith("O") ||
           state.getCoordinates().get(2).markedWith("X") && state.getCoordinates().get(4).markedWith("X") && state.getCoordinates().get(6).markedWith("X") ||
           state.getCoordinates().get(2).markedWith("O") && state.getCoordinates().get(4).markedWith("O") && state.getCoordinates().get(6).markedWith("O")){

            return moves;
        }


        moves.addAll(state.getCoordinates().stream().filter(c -> !c.isMarked()).collect(Collectors.toList()));

        return moves;
    }


}
