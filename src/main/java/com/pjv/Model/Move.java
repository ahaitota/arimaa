package com.pjv.Model;

/**
 * The Move class represents a move made by a player in the game.
 */

public class Move {
    public int oldRow, oldCol;
    private int newRow, newCol;
    private char direction;
    private Piece piece;

     /**
     * Constructs a Move object representing a step without a piece
     * (for turn markers).
     *
     * @param oldRow the row index of the piece's original position
     * @param oldCol the column index of the piece's original position
     */

    public Move(int oldRow, int oldCol) {
        this.piece = null;
        this.oldRow = oldRow;
        this.oldCol = oldCol;
        this.direction = oldCol == 1 ? 'g' : 's';
        this.newRow = -1; 
        this.newCol = -1;
    }

    /**
     * Constructs a Move object representing a step with a piece
     * (for starting placing).
     *
     * @param piece  the piece involved in the move
     * @param oldRow the row index of the piece's original position
     * @param oldCol the column index of the piece's original position
     */

    public Move(Piece piece, int oldRow, int oldCol) {
        this.piece = piece;
        this.oldRow = oldRow;
        this.oldCol = oldCol;
        this.direction = ' ';
        this.newRow = -1; 
        this.newCol = -1;
    }

      /**
     * Constructs a Move object representing a move from one position to another
     * (just usual move).
     *
     * @param piece  the piece involved in the move
     * @param oldRow the row index of the piece's original position
     * @param oldCol the column index of the piece's original position
     * @param newRow the row index of the piece's new position
     * @param newCol the column index of the piece's new position
     */

    public Move(Piece piece, int oldRow, int oldCol, int newRow, int newCol) {
        this.piece = piece;
        this.oldRow = oldRow;
        this.oldCol = oldCol;
        this.newRow = newRow;
        this.newCol = newCol;
        this.direction = getDirection(oldRow, oldCol, newRow, newCol); 
    }

    /**
     * Determines the direction of the move based on the old and new positions.
     *
     * @param oldRow the row index of the piece's original position
     * @param oldCol the column index of the piece's original position
     * @param newRow the row index of the piece's new position
     * @param newCol the column index of the piece's new position
     * @return the direction of the move ('n' for north, 's' for south, 'e' for east, 'w' for west, 'x' for no movement)
     */

    private char getDirection(int oldRow, int oldCol, int newRow, int newCol) {
        if (newRow < oldRow) return 'n';
        if (newRow > oldRow) return 's';
        if (newCol > oldCol) return 'e';
        if (newCol < oldCol) return 'w';
        if (newRow == oldRow) return 'x';
        throw new IllegalArgumentException("Invalid move direction");
    }

    /**
     * Converts row and column coordinates to board notation (e.g., 'a1', 'b3').
     *
     * @param row the row index
     * @param col the column index
     * @return the board notation for the given coordinates
     */

    public static String coordinatesToNotation(int row, int col) {
        //convert column index to letter
        char boardColumn = (char) ('a' + col); 
        //convert row index to board row number
        int boardRow = 8 - row; 
        return "" + boardColumn + boardRow;
    }

     /**
     * Returns a string representation of the move.
     *
     * @return a string representing the move in standard notation
     */
 
    @Override
    public String toString() {
        if (piece == null) {
            //return a custom string representing the turn marker
            return direction == 'g' ? oldRow + "g " : oldRow + "s ";
        } else if (direction == ' ') {
            return piece.getNotation() + coordinatesToNotation(oldRow, oldCol) + direction;

        } else {
            //return the notation for a regular move
            return piece.getNotation() + coordinatesToNotation(oldRow, oldCol) + direction + ' ';
        }
    }
}


