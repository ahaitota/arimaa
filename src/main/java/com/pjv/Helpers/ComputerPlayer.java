package com.pjv.Helpers;

import com.pjv.Controller.GameController;
import com.pjv.Model.Board;
import com.pjv.Model.Move;
import com.pjv.Model.Piece;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a computer player in the game. It handles the logic for 
 * making moves, including selecting random moves and handling trapped pieces.
 */

public class ComputerPlayer {
    GameController gameController;
    Board board;
    Random random;
    List<int[]> moves;
    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    public boolean checkingForSteps = false;
    private Move trappedMove = null;
    Logger logger = LoggerFactory.getLogger(ComputerPlayer.class);

    /**
     * Constructor for the ComputerPlayer class.
     * 
     * @param gameController the GameController instance
     * @param board the Board instance
     */

    public ComputerPlayer (GameController gameController, Board board) {
        this.gameController = gameController;
        this.board = board;
        this.random = new Random();
        this.moves = new ArrayList<>();
    }

    /**
     * Chooses a random number of steps between 1 and 4.
     * 
     * @return the number of steps
     */

    public int choseStepsAmount() {
        return random.nextInt(4) + 1;
    }

     /**
     * Selects a random move from a list of possible moves.
     * 
     * @param movesToChoose the list of possible moves
     * @return the selected move as an array of two integers representing row and column shifts
     */

    public int[] getRandomMove(List<int[]> movesToChoose) {
        int randomIndex = random.nextInt(movesToChoose.size());
        return movesToChoose.get(randomIndex);
    }

    /**
     * Records a move that resulted in a piece being trapped.
     * 
     * @param getmove the move that caused the piece to be trapped
     */

    public void wasTrapped(Move getmove) {
        trappedMove = getmove;
    }

     /**
     * Makes a move for the computer player by selecting a random piece and moving it 
     * in a random valid direction for a random number of steps.
     */

    public void makeMove() {
        int stepsAmount = choseStepsAmount();
        List<Piece> piecesToChoose = new ArrayList<>();
        List<Map.Entry<Integer, Integer>> piecePlace = new ArrayList<>();
        //find all pieces and their positions
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board.board[i][j] != null) {
                    piecesToChoose.add(board.board[i][j]);
                    piecePlace.add(new AbstractMap.SimpleEntry<>(i, j));
                }
            }
        }

        //make for each step
        while (stepsAmount > 0) {

            int newRow = 0;
            int newCol = 0;
            int oldRow = 0;
            int oldCol = 0;
            moves.clear();
            int randomNumber = 0;
            trappedMove = null;

            while (moves.isEmpty()) {
                //chose random piece on a board
                randomNumber = random.nextInt(piecesToChoose.size()); 
                Piece randomPiece = piecesToChoose.get(randomNumber);
                //find its position
                Map.Entry<Integer, Integer> firstPair = piecePlace.get(randomNumber);
                oldRow = firstPair.getKey();
                oldCol = firstPair.getValue();
                //find possible moves from this position
                checkingForSteps = true;
                for (int i = 0; i < 4; i++) {
                    if (gameController.check.checkStep(oldRow, oldCol, oldRow + directions[i][0], oldCol + directions[i][1], 
                    randomPiece.getType(), randomPiece.getColor(), gameController.getCurrPlayer(), 
                    stepsAmount)) {
                        moves.add(new int[] {directions[i][0], directions[i][1]}); 
                    }
                }
                checkingForSteps = false;
            }
            //randomly chose move
            int[] move= getRandomMove(moves);
            newRow = oldRow + move[0];
            newCol = oldCol + move[1];
            //make this move
            gameController.setPiece(oldRow, oldCol, newRow, newCol);
            if (gameController.loggingEnabled) logger.debug("Computer player step: from ({}, {}) to ({}, {})", oldRow, oldCol, newRow, newCol);

            //update figure's placing
            Map.Entry<Integer, Integer> entry = new AbstractMap.SimpleEntry<>(newRow, newCol);
            piecePlace.set(randomNumber, entry);
            //delete if was trapped
            if (trappedMove != null) {
                for (int i = 0; i < piecePlace.size(); i++) {
                    Map.Entry<Integer, Integer> piece = piecePlace.get(i);
                    if (piece.getKey() == trappedMove.oldRow && piece.getValue() == trappedMove.oldCol) {
                        piecesToChoose.remove(i);
                        piecePlace.remove(i);

                        break;
                    }
                }
            } 

            trappedMove = null;
            stepsAmount--;
        }
        
        gameController.changePlayer();
        if (gameController.loggingEnabled) logger.info("Computer Player finished turn");
        
    }
    
}
