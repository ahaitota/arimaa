package com.pjv.Model;

import com.pjv.Model.Piece.Pieces;

/**
 * The Board class represents the game board containing pieces.
 */

public class Board {
    /** 2D array representing the game board */
    public Piece[][] board;
    private int boardsize = 8;
    /** Array containing the initial piece types for filling the board */
    private Pieces[] types = {Pieces.DOG, Pieces.HORSE, Pieces.CAT, Pieces.ELEPHANT, Pieces.CAMEL, Pieces.CAT, Pieces.HORSE, Pieces.DOG};

    /**
     * Constructs a Board object with an empty board.
     */

    public Board() {
        board = new Piece [boardsize][boardsize];
         
    }

    /**
     * Cleans the board by removing all pieces.
     */

    public void cleanBoard() {
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                board [i][j] = null;
                
            }
        }  
    }

    /**
     * Fills the board with initial pieces according to Arimaa game rules.
     */

    public void fillBoard() {
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                
                if (i == 0) {
                    
                    board [i][j] = new Piece(Piece.Pieces.RABBIT, Piece.Colors.SILVER);
                }
                if (i == 1) {
                    
                    board [i][j] = new Piece(types[7 - j], Piece.Colors.SILVER);
                }
                if (i == boardsize - 2) {
                    
                    board [i][j] = new Piece(types[j], Piece.Colors.GOLD);
                }
                if (i == boardsize - 1) {
                    
                    board [i][j] = new Piece(Piece.Pieces.RABBIT, Piece.Colors.GOLD);
                }
                
            }
        }  
    }

     /**
     * Gets the picture name of the piece at the specified position.
     *
     * @param row the row index of the piece
     * @param col the column index of the piece
     * @return the picture name of the piece, or null if no piece exists at the specified position
     */

    public String getPicName(int row, int col) {
        if (board[row][col] != null) {
            return board[row][col].getColor().toString() + board[row][col].getType().toString(); 
        } 
        return null; 
    }

    /**
     * Gets the color of the piece at the specified position.
     *
     * @param row the row index of the piece
     * @param col the column index of the piece
     * @return the color of the piece, or null if no piece exists at the specified position
     */

    public Piece.Colors returnColor(int row, int col) {
        if (board[row][col] != null) {
            return board[row][col].getColor();
        } 
        return null;
        
    }

     /**
     * Gets the type of the piece at the specified position.
     *
     * @param row the row index of the piece
     * @param col the column index of the piece
     * @return the type of the piece, or null if no piece exists at the specified position
     */

    public Piece.Pieces returnType(int row, int col) {
        if (board[row][col] != null) {
            return board[row][col].getType();
        } 
        return null;
         
    }

     /**
     * Gets the piece at the specified position.
     *
     * @param row the row index of the piece
     * @param col the column index of the piece
     * @return the piece at the specified position, or null if no piece exists at the specified position
     */

    public Piece returnPiece(int row, int col) {
        return board[row][col];
    }
    
}
