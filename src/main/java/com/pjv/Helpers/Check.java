package com.pjv.Helpers;

import com.pjv.Model.Board;
import com.pjv.Model.Move;
import com.pjv.Model.Player;
import com.pjv.Model.Piece;
import com.pjv.Controller.GameController;

/**
 * This class provides various checks and validations for the game, including move validation,
 * push and pull checks, and win condition checks.
 */

public class Check {
    GameController gameController;
    Board board;
    ComputerPlayer computerPlayer;
    public boolean pushCanBeDone = false;
    public boolean pullCanBeDone = false;
    public int pushRow = 0;
    public int pushCol = 0;
    public int pushStrength = 0;
    public int pullRow = 0;
    public int pullCol = 0;
    public int pullStrength = 0;
    private boolean checkingImmo = false;
    private int[][] checkingDirections = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    /**
     * Constructor for the Check class.
     * 
     * @param gameController the GameController instance
     * @param board the Board instance
     * @param computerPlayer the ComputerPlayer instance
     */

    public Check(GameController gameController, Board board, ComputerPlayer computerPlayer) {
        this.gameController = gameController;
        this.board = board;
        this.computerPlayer = computerPlayer;
    }

      /**
     * Validates a step move.
     * 
     * @param oldRow the starting row of the piece
     * @param oldCol the starting column of the piece
     * @param newRow the target row of the piece
     * @param newCol the target column of the piece
     * @param type the type of the piece
     * @param color the color of the piece
     * @param currPlayer the current player
     * @param steps the number of steps left
     * @return true if the step is valid, false otherwise
     */

    public boolean checkStep(int oldRow, int oldCol, int newRow, int newCol, 
    Piece.Pieces type, Piece.Colors color, Player currPlayer, int steps) {

        //check bounds of the field
        boolean c6 = checkBounds(newRow, newCol);
        if (!c6) {
            return false;
        }
        //if there no other figure in new place
        boolean c0 = checkFreeSpace(newRow, newCol);

        //in orthogonal directions and one step at a time
        boolean c1 = checkOrthogonal(oldRow, oldCol, newRow, newCol);
        //rabbits cannot move backward
        //although rabbits cannot move backwards on their own, they may be pushed or pulled back.]
        boolean c2 = type != Piece.Pieces.RABBIT || checkRabbit(oldRow, newRow, color);
        boolean c3 = checkFreezing(oldRow, oldCol, type, color);
        boolean c4 = checkTurn(color, currPlayer);
        //my player color != piece color, this is beginning of push 
        //or second step of pull
        if (!c4) {
            boolean c5 = checkPush(oldRow, oldCol, color, type, currPlayer, steps);
            if (c5) {
                //push, no need to check freezing and rabbit
                return c0 && c1 && c6;
            } else if (pullCanBeDone) {
                //pull
                if (newRow == pullRow && newCol == pullCol && type.getStrength() < pullStrength) {
                    pullCanBeDone = false;
                    pullRow = 0;
                    pullCol = 0;
                    pullStrength = 0;
                    return c0 && c1 && c6;
                } else {
                    return false;
                }
                
            } else {
                return false;
            }
             
        } else {
            //my piece, but push
            if (pushCanBeDone) {
                if (newRow == pushRow && newCol == pushCol && type.getStrength() > pushStrength) {
                    //reset all vars
                    pushCanBeDone = false;
                    pushRow = 0;
                    pushCol = 0;
                    pushStrength = 0;
                    return c0 && c1 && c2 && (!c3) && c6;
                } else {
                    return false;
                }
                
            }
            //my turn, my piece
            //pull may be later
            pullCanBeDone = true;
            pullRow = oldRow;
            pullCol = oldCol;
            pullStrength = type.getStrength();
            return c0 && c1 && c2 && (!c3) && c6;
        }
    }

     /**
     * Checks if the new position is within the bounds of the board.
     * 
     * @param newRow the target row of the piece
     * @param newCol the target column of the piece
     * @return true if within bounds, false otherwise
     */

    public boolean checkBounds(int newRow, int newCol) {
        if (newRow == -1 || newRow == 8 || newCol == -1 || newCol == 8) {
            return false;
        }
        return true;
    }

     /**
     * Checks if the target space is free.
     * 
     * @param newRow the target row of the piece
     * @param newCol the target column of the piece
     * @return true if the space is free, false otherwise
     */

    public boolean checkFreeSpace(int newRow, int newCol) {
        if (board.returnColor(newRow, newCol) == null) {
            return true;
        }
        return false;
    }

     /**
     * Checks if a push can be performed.
     * 
     * @param oldRow the starting row of the piece
     * @param oldCol the starting column of the piece
     * @param color the color of the piece
     * @param type the type of the piece
     * @param currPlayer the current player
     * @param steps the number of steps taken
     * @return true if a push can be performed, false otherwise
     */

    public boolean checkPush(int oldRow, int oldCol, Piece.Colors color, Piece.Pieces type, Player currPlayer, int steps) {
        if (!currPlayer.getColor().toString().equals(color.toString()) && steps > 1) {
            //if someone stronger is near
            boolean strongerEnemyNear = (oldRow != 7 && board.returnType(oldRow + 1, oldCol) != null && board.returnColor(oldRow + 1, oldCol) != color && board.returnType(oldRow + 1, oldCol).getStrength() > type.getStrength() && !checkFreezing(oldRow + 1, oldCol, board.returnType(oldRow + 1, oldCol), board.returnColor(oldRow + 1, oldCol)))
            || (oldRow != 0 && board.returnType(oldRow - 1, oldCol) != null && board.returnColor(oldRow - 1, oldCol) != color && board.returnType(oldRow - 1, oldCol).getStrength() > type.getStrength() && !checkFreezing(oldRow - 1, oldCol, board.returnType(oldRow - 1, oldCol), board.returnColor(oldRow - 1, oldCol)))
            || (oldCol != 7 && board.returnType(oldRow, oldCol + 1) != null && board.returnColor(oldRow, oldCol + 1) != color && board.returnType(oldRow, oldCol + 1).getStrength() > type.getStrength() && !checkFreezing(oldRow, oldCol + 1, board.returnType(oldRow, oldCol + 1), board.returnColor(oldRow, oldCol + 1)))
            || (oldCol != 0 && board.returnType(oldRow, oldCol - 1) != null && board.returnColor(oldRow, oldCol - 1) != color && board.returnType(oldRow, oldCol - 1).getStrength() > type.getStrength() && !checkFreezing(oldRow, oldCol - 1, board.returnType(oldRow, oldCol - 1), board.returnColor(oldRow, oldCol - 1)));
            if (strongerEnemyNear) {
                if ((gameController.vsPC == false || computerPlayer.checkingForSteps == false) && checkingImmo == false) {
                    pushCanBeDone = true;
                    pushRow = oldRow;
                    pushCol = oldCol;
                    pushStrength = type.getStrength();
                }
                return true;
            } 
        }
        return false;
    }

      /**
     * Checks if it's the current player's turn.
     * 
     * @param color the color of the piece
     * @param currPlayer the current player
     * @return true if it's the current player's turn, false otherwise
     */

    public boolean checkTurn(Piece.Colors color, Player currPlayer) {
        if (currPlayer.getColor().toString().equals(color.toString())) {
            return true;
        }
        return false;
    }

     /**
     * Checks if the move is orthogonal.
     * 
     * @param oldRow the starting row of the piece
     * @param oldCol the starting column of the piece
     * @param newRow the target row of the piece
     * @param newCol the target column of the piece
     * @return true if the move is orthogonal, false otherwise
     */

    public boolean checkOrthogonal(int oldRow, int oldCol, int newRow, int newCol) {
        //changed one row up/down and stayed in the same column
        return ((oldRow - 1 == newRow || oldRow + 1 == newRow) && oldCol == newCol)
        //changed one column left/right and stayed in the same row
        || ((oldCol - 1 == newCol || oldCol + 1 == newCol) && oldRow == newRow);
    }

      /**
     * Checks if a rabbit move is valid (rabbits cannot move backwards).
     * 
     * @param oldRow the starting row of the piece
     * @param newRow the target row of the piece
     * @param color the color of the piece
     * @return true if the move is valid, false otherwise
     */

    public boolean checkRabbit(int oldRow, int newRow, Piece.Colors color) {
        if (color == Piece.Colors.GOLD) {
            return oldRow - 1 == newRow || oldRow == newRow;
        }
        return oldRow + 1 == newRow || oldRow == newRow;
    }

     /**
     * Checks if a piece is frozen.
     * 
     * @param row the row of the piece
     * @param col the column of the piece
     * @param type the type of the piece
     * @param color the color of the piece
     * @return true if the piece is frozen, false otherwise
     */

    public boolean checkFreezing(int oldRow, int oldCol, Piece.Pieces type, Piece.Colors color) {
        //if someone stronger is near
        boolean strongerEnemyNear = (oldRow != 7 && board.returnType(oldRow + 1, oldCol) != null && board.returnColor(oldRow + 1, oldCol) != color && board.returnType(oldRow + 1, oldCol).getStrength() > type.getStrength())
        || (oldRow != 0 && board.returnType(oldRow - 1, oldCol) != null && board.returnColor(oldRow - 1, oldCol) != color && board.returnType(oldRow - 1, oldCol).getStrength() > type.getStrength())
        || (oldCol != 7 && board.returnType(oldRow, oldCol + 1) != null && board.returnColor(oldRow, oldCol + 1) != color && board.returnType(oldRow, oldCol + 1).getStrength() > type.getStrength())
        || (oldCol != 0 && board.returnType(oldRow, oldCol - 1) != null && board.returnColor(oldRow, oldCol - 1) != color && board.returnType(oldRow, oldCol - 1).getStrength() > type.getStrength());

        if (!strongerEnemyNear) {
            return false;
        }
        //if there is no friend near
        boolean friendNear = (oldRow != 7 && board.returnType(oldRow + 1, oldCol) != null && board.returnColor(oldRow + 1, oldCol) == color) 
        || (oldRow != 0 && board.returnType(oldRow - 1, oldCol) != null && board.returnColor(oldRow - 1, oldCol) == color) 
        || (oldCol != 7 && board.returnType(oldRow, oldCol + 1) != null && board.returnColor(oldRow, oldCol + 1) == color) 
        || (oldCol != 0 && board.returnType(oldRow, oldCol - 1) != null && board.returnColor(oldRow, oldCol - 1) == color);
        if (friendNear) {
            return false;
        }
        return true;
    }

    /**
     * Checks all win condition.
     * 
     * @return the move that caused the piece to get into trap
     */

    public Move checkTraps() {
        if (gameController.vsPC && computerPlayer.checkingForSteps) {
            return null;
        }
        //check each step one by one
        if (board.board[2][2] != null) {
            Piece.Colors myColor = board.returnColor(2, 2);
            boolean friendNear = (board.returnColor(2 + 1, 2) == myColor || 
            board.returnColor(2 - 1, 2) == myColor || 
            board.returnColor(2, 2 + 1) == myColor || 
            board.returnColor(2, 2 - 1) == myColor);
            if (!friendNear) {
                Move m = new Move(board.board[2][2], 2, 2, 2, 2);
                board.board[2][2] = null;
                return m;
            }
        }
        if (board.board[2][5] != null) {
            Piece.Colors myColor = board.returnColor(2, 5);
            boolean friendNear = (board.returnColor(2 + 1, 5) == myColor || 
            board.returnColor(2 - 1, 5) == myColor || 
            board.returnColor(2, 5 + 1) == myColor || 
            board.returnColor(2, 5 - 1) == myColor);
            if (!friendNear) {
                Move m = new Move(board.board[2][5], 2, 5, 2, 5);
                board.board[2][5] = null;
                return m;
            }
        }
        if (board.board[5][2] != null) {
            Piece.Colors myColor = board.returnColor(5, 2);
            boolean friendNear = (board.returnColor(5 + 1, 2) == myColor || 
            board.returnColor(5 - 1, 2) == myColor || 
            board.returnColor(5, 2 + 1) == myColor || 
            board.returnColor(5, 2 - 1) == myColor);
            if (!friendNear) {
                Move m = new Move(board.board[5][2], 5, 2, 5, 2);
                board.board[5][2] = null;
                return m;
            }
        }
        if (board.board[5][5] != null) {
            Piece.Colors myColor = board.returnColor(5, 5);
            boolean friendNear = (board.returnColor(5 + 1, 5) == myColor || 
            board.returnColor(5 - 1, 5) == myColor || 
            board.returnColor(5, 5 + 1) == myColor || 
            board.returnColor(5, 5 - 1) == myColor);
            if (!friendNear) {
                Move m = new Move(board.board[5][5], 5, 5, 5, 5);
                board.board[5][5] = null;
                return m;
            }
        }
        return null;

    }

    /**
     * Checks all win condition.
     * 
     * @param currentPlayer current player
     * @return the color of the winner or null if none has won yet
     */

    public Piece.Colors checkWinner(Player currentPlayer) {
        Piece.Colors c1 = checkRabbitAtEnd();
        if (c1 != null) {
            return c1;
        }
        Piece.Colors c2 = checkNoRabbits(currentPlayer);
        if (c2 != null) {
            return c2;
        }
        Piece.Colors c3 = checkImmobilization();
        if (c3 != null) {
            return c3;
        }

        //when none has won yet
        return null;
    }

    /**
     * Checks if a player cannot make a move.
     * 
     * @return the color of the player who has won
     */

    public Piece.Colors checkImmobilization() {
        checkingImmo = true;
        int canMove = 0;
        Player tempPlayer = gameController.getCurrPlayer() == gameController.player2 ? gameController.player1 : gameController.player2;
        Piece.Colors opponentColor = null;
        Piece.Colors myColor = null;
        if (tempPlayer.getColor() == Player.Color.GOLD) {
            opponentColor = Piece.Colors.GOLD;
            myColor = Piece.Colors.SILVER;
        } else {
            opponentColor = Piece.Colors.SILVER;
            myColor = Piece.Colors.GOLD;
        }
        //for each figure
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //if there is something on the board
                if (board.board[i][j] != null && opponentColor == board.board[i][j].getColor()) {
                    for (int s = 0; s < 4; s++) {
                        //if there are some moves possible - break
                        if (gameController.check.checkStep(i, j, i + checkingDirections[s][0], 
                        j + checkingDirections[s][1], board.board[i][j].getType(), 
                        board.board[i][j].getColor(), tempPlayer, 4)) {
                            canMove += 1;
                            break;
                        }
                    }
                    if (canMove > 0) {
                        break;
                    }
                }
            }
            if (canMove > 0) {
                break;
            }
        }
        checkingImmo = false;
        if (canMove == 0) {
            return myColor;
        }
        return null;
    }

     /**
     * Checks if any rabbit of the specified color has reached the opposite end of the board.
     * 
     * @return the color of a rabbit that has reached the opposite end
     */

    public Piece.Colors checkRabbitAtEnd() {
        //Gold wins by moving a gold rabbit onto the eighth rank, 
        //and Silver wins by moving a silver rabbit onto the first rank 
        for (int j = 0; j < 8; j++) {
            if (board.board[0][j] != null && board.board[0][j].getColor() == Piece.Colors.GOLD && board.board[0][j].getType() == Piece.Pieces.RABBIT) {
                return Piece.Colors.GOLD;
            }
        }
        for (int j = 0; j < 8; j++) {
            if (board.board[7][j] != null && board.board[7][j].getColor() == Piece.Colors.SILVER && board.board[7][j].getType() == Piece.Pieces.RABBIT) {
                return Piece.Colors.SILVER;
            }
        }
        //when none has won yet
        return null;
    }

    /**
     * Checks if there are no rabbits of the specified color on the board.
     * 
     * @return color of the player that wins when  the other one does not have
     * rabbits
     */

    public Piece.Colors checkNoRabbits(Player currentPlayer) {
        boolean goldRabbits = false;
        boolean silverRabbits = false;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                if (board.board[i][j] != null && board.board[i][j].getColor() == Piece.Colors.SILVER && board.board[i][j].getType() == Piece.Pieces.RABBIT) {
                    //there are silver rabbits
                    silverRabbits = true;
                } else if (board.board[i][j] != null && board.board[i][j].getColor() == Piece.Colors.GOLD && board.board[i][j].getType() == Piece.Pieces.RABBIT) {
                    //there are gold rabbits
                    goldRabbits = true;
                }
                if (goldRabbits && silverRabbits) {
                    break;
                }
            }
            if (goldRabbits && silverRabbits) {
                break;
            }
        }
        
        if (!goldRabbits && !silverRabbits) {
            //both dont have rabbits, won the one whose turn it just was
            if (currentPlayer.getColor() == Player.Color.GOLD) {
                return Piece.Colors.GOLD;
            } else if (currentPlayer.getColor() == Player.Color.SILVER) {
                return Piece.Colors.SILVER;
            }
            
        } else if (!goldRabbits) {
            //silver wins
            return Piece.Colors.SILVER;
        } else if (!silverRabbits) {
            //gold wins
            return Piece.Colors.GOLD;
        }
        //when none has won yet
        return null;
    }


}
