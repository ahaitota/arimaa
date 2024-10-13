package com.pjv.Model;

/**
 * The GameState class represents the state of the game at a certain point in time.
 */

public class GameState {
    private Piece[][] board;
    private int steps;
    private int boardsize = 8;
    private Player currPlayer;
    private int playerTurns;
    private boolean pushCanBeDone = false;
    private boolean pullCanBeDone = false;
    private int pushRow = 0;
    private int pushCol = 0;
    private int pushStrength = 0;
    private int pullRow = 0;
    private int pullCol = 0;
    private int pullStrength = 0;

      /**
     * Constructs a GameState object with the specified parameters.
     *
     * @param board         the game board
     * @param steps         the number of steps taken
     * @param currPlayer    the current player
     * @param playerTurns   the number of turns played by the current player
     * @param pushCanBeDone flag indicating if a push move can be performed
     * @param pullCanBeDone flag indicating if a pull move can be performed
     * @param pushRow       the row index of the piece to be pushed
     * @param pushCol       the column index of the piece to be pushed
     * @param pushStrength  the strength of the push move
     * @param pullRow       the row index of the piece to be pulled
     * @param pullCol       the column index of the piece to be pulled
     * @param pullStrength  the strength of the pull move
     */

    public GameState(Piece[][] board, int steps, Player currPlayer, int playerTurns, boolean pushCanBeDone,
    boolean pullCanBeDone, int pushRow, int pushCol, int pushStrength,
    int pullRow, int pullCol, int pullStrength) {
        this.board = new Piece[boardsize][boardsize];
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                if (board[i][j] != null) {
                    this.board[i][j] = new Piece(board[i][j].getType(), board[i][j].getColor());
                }
            }
        }
        this.steps = steps;
        this.currPlayer = currPlayer;
        this.playerTurns = playerTurns;
        this.pushCanBeDone = pushCanBeDone;
        this.pullCanBeDone = pullCanBeDone;
        this.pushRow = pushRow;
        this.pushCol = pushCol;
        this.pushStrength = pushStrength;
        this.pullRow = pullRow;
        this.pullCol = pullCol;
        this.pullStrength = pullStrength;
    }


    public Piece[][] getBoard() {
        return board;
    }

    public int getSteps() {
        return steps;
    }

    public Player getPlayer() {
        return currPlayer;
    }

    public int getTurns() {
        return playerTurns;
    }

    public boolean isPushCanBeDone() {
        return pushCanBeDone;
    }

    public boolean isPullCanBeDone() {
        return pullCanBeDone;
    }

    public int getPushRow() {
        return pushRow;
    }

    public int getPushCol() {
        return pushCol;
    }

    public int getPushStrength() {
        return pushStrength;
    }

    public int getPullRow() {
        return pullRow;
    }

    public int getPullCol() {
        return pullCol;
    }

    public int getPullStrength() {
        return pullStrength;
    }
}
