package com.pjv.Helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JFileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.pjv.Controller.GameController;
import com.pjv.Model.Move;
import com.pjv.Model.Piece;

/**
 * The LoadGame class provides functionality to load a game from a file, 
 * read the game history, and restore the game state.
 */

public class LoadGame {
    MoveHistory moveHistory;
    GameController gameController;
    Clock clock;
    Logger logger = LoggerFactory.getLogger(LoadGame.class);

    /**
     * Constructs a LoadGame instance.
     * 
     * @param moveHistory the MoveHistory instance
     * @param gameController the GameController instance
     * @param clock the Clock instance
     */
    
    public LoadGame(MoveHistory moveHistory, GameController gameController, Clock clock) {
        this.moveHistory = moveHistory;
        this.gameController = gameController;
        this.clock = clock;
    }


    /**
     * Opens a file chooser dialog for the user to select a game file to load.
     * 
     * @return the selected File, or null if no file was selected
     */

    public File choseFile() {
        // Get the current working directory
        String currentDirectory = System.getProperty("user.dir");

        // Create a file chooser with the current directory
        JFileChooser fileChooser = new JFileChooser(currentDirectory + "/arimaa/save");

        // Set file chooser to open dialog
        int returnValue = fileChooser.showOpenDialog(null);
        File selectedFile = null;
        // Check if user selected a file
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (gameController.loggingEnabled) logger.info("The file to load was chosen");

            // Get the selected file
            selectedFile = fileChooser.getSelectedFile();
            if (gameController.loggingEnabled) logger.info("Selected file: " + selectedFile.getAbsolutePath());

            return selectedFile;
        } else {
            if (gameController.loggingEnabled) logger.info("No file selected");

        }
        return null;
    }

    /**
     * Reads the game history from the provided file and restores the game state.
     * 
     * @param file the File to read the game history from
     */

    public void readGameHistory(File file) {

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (gameController.loggingEnabled) logger.info("Started reading file");

            String line = br.readLine();
            String[] steps = line.split(" ");
            
            for (String step : steps) {
                Move move = createMove(step);
                if (move != null) {
                    moveHistory.addMove(move);
                }
            }

        } catch (IOException e) {
            if (gameController.loggingEnabled) logger.info("An error occurred while reading the file");

        }
    }

     /**
     * Creates a Move object from the given move information string
     * and makes steps to get to the necessary state.
     * 
     * @param moveInfo the move information string
     * @return the created Move object, or null if the move is not valid
     */

    private Move createMove(String moveInfo) {
        //convert string to move
        int len = moveInfo.length();

        String regex1 = "\\d+[gs]";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(moveInfo);

        String regex2 = "\\d+";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(moveInfo);

        //get markers at the end of the file (game type, time)
        if (moveInfo.equals("PC")) {
            gameController.vsPC = true;
            return null;

        //reserve time
        } else if (moveInfo.startsWith("G")) {
            int time = Integer.parseInt(moveInfo.substring(1));
            gameController.player1.setPlayerReserve(time);
         
            return null;
        //reserve time
        } else if (moveInfo.startsWith("S")) {
            int time = Integer.parseInt(moveInfo.substring(1));
            gameController.player2.setPlayerReserve(time);
          
            return null;
        //total time
        } else if (matcher2.matches()) {
            int newTime = Integer.parseInt(moveInfo);
            clock.resetTotalTime(newTime);
     

            return null;
        }
        //turn marker
        else if (matcher1.matches()) {
            int stepCount = moveInfo.charAt(0) - '0';
            if (moveInfo.charAt(len - 1) == 'g') {
                if (stepCount == 1 && len == 2) {
                return new Move(gameController.goldTurns, 1);

                } else {
                    gameController.changePlayer();
                    return new Move(gameController.goldTurns, 1);

                }

            } else {
                gameController.changePlayer();
                return new Move(gameController.silverTurns, 0);

            }
        //game beginning
        } else if (len == 3) {
            char pieceType = moveInfo.charAt(0);
            int oldRow = 8 - (moveInfo.charAt(2) - '0');
            int oldCol = moveInfo.charAt(1) - 'a';
            Piece piece;
            //lower case - silver
            if(Character.isLowerCase(pieceType)) {
                piece = new Piece(Piece.Pieces.fromNotation(pieceType), Piece.Colors.SILVER);

            } else {
                piece = new Piece(Piece.Pieces.fromNotation(pieceType), Piece.Colors.GOLD);

            }
            gameController.putPieceOnBoard(piece, oldRow, oldCol);
            return new Move(piece, oldRow, oldCol);    

        } else {
            //usual move
            char pieceType = moveInfo.charAt(0);
            int oldRow = 8 - ((moveInfo.charAt(2)) - '0');
            int oldCol = moveInfo.charAt(1) - 'a';
            char direction = moveInfo.charAt(3);
            int newRow = oldRow;
            int newCol = oldCol;
            if (direction == 'n') {
                newRow = oldRow - 1;

            } else if (direction == 's') {
                newRow = oldRow + 1;

            } else if (direction == 'e') {
                newCol = oldCol + 1;

            } else if (direction == 'w') {
                newCol = oldCol - 1;
            }
            Piece piece;
            //lower case - silver
            if(Character.isLowerCase(pieceType)) {
                piece = new Piece(Piece.Pieces.fromNotation(pieceType), Piece.Colors.SILVER);

            } else {
                //gold
                piece = new Piece(Piece.Pieces.fromNotation(pieceType), Piece.Colors.GOLD);

            }
            //if it is not a trap info
            if (direction != 'x') {
                //make move
                gameController.setPiece(oldRow, oldCol, newRow, newCol);
            }
            return new Move(piece, oldRow, oldCol, newRow, newCol);
        }
    }
}
