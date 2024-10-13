package com.pjv.Helpers;

import com.pjv.Controller.GameController;
import com.pjv.Model.Move;
import com.pjv.Model.Player;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.util.List;

/**
 * The MoveHistory class manages the history of all moves made during the game.
 * It keeps track of the moves and provides methods to add, remove, and retrieve moves.
 */

public class MoveHistory {
    GameController gameController;
    public List<Move> moves;

    /**
     * Constructs a MoveHistory instance.
     *
     * @param gameController the GameController instance
     */

    public MoveHistory(GameController gameController) {
        this.gameController = gameController;
        moves = new ArrayList<>();
    }

    /**
     * Adds a move to the history.
     *
     * @param move the Move to be added
     */

    public void addMove(Move move) {
        moves.add(move);
    }

    /**
     * Removes a move from the history at the specified index.
     *
     * @param index the index of the move to be removed
     */

    public void removeMove(int index) {
        moves.remove(index);
    }

    /**
     * Retrieves the last move in the history.
     *
     * @return the last Move in the history
     */

    public Move getLast() {
        return moves.get(moves.size() - 1);
    }

    /**
     * Retrieves all moves in the history.
     *
     * @return a List of all moves
     */

    public List<Move> getMoves() {
        return moves;
    }

     /**
     * Gets the number of moves in the history.
     *
     * @return the size of the move history
     */

    public int getsize() {
        return moves.size();
    }

     /**
     * Adds a turn marker to the move history.
     * The turn marker indicates which player's turn it is.
     *
     * @return the Move representing the turn marker
     */

    public Move addTurnMarker() {
        Move turnMarker;
        if (gameController.getCurrPlayer().getColor() == Player.Color.GOLD) {
        turnMarker = new Move(gameController.goldTurns, 1);
        } else {
            turnMarker = new Move(gameController.silverTurns, 2);
        }
        return turnMarker;
    }

    /**
     * Returns a string representation of the move history.
     * A newline is inserted before moves that match a specific pattern.
     *
     * @return the string representation of the move history
     */

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Move move : moves) {
            String regex = "\\d+s ";
            Pattern pattern = Pattern.compile(regex);
            String m = move.toString();
            Matcher matcher = pattern.matcher(m);
            if (matcher.find()) {
                sb.append("\n");
            }

            sb.append(move).append(" ");
        }
        return sb.toString().trim();
    }
}

