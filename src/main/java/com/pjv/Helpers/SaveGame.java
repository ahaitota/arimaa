package com.pjv.Helpers;

import com.pjv.Controller.GameController;
import com.pjv.Model.Move;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

/**
 * The SaveGame class provides functionality to save the current state of the game.
 * It interacts with the game controller, clock, and move history to save game details to a file.
 */

public class SaveGame {
    GameController gameController;
    Clock clock;
    Logger logger = LoggerFactory.getLogger(SaveGame.class);

    /**
     * Constructs a SaveGame instance.
     *
     * @param gameController the GameController instance
     * @param clock          the Clock instance
     */

    public SaveGame(GameController gameController, Clock clock) {
        this.gameController = gameController;
        this.clock = clock;
    }

    /**
     * Saves the current game state, including move history, game type, and reserve time, to a file.
     *
     * @param moveHistory the MoveHistory instance containing the moves made in the game
     */

    public void saveGame(MoveHistory moveHistory) {
        if (gameController.loggingEnabled) logger.info("Started saving Game");

        //remember curr time
        int currTime = clock.getTotalTime();
        if (currTime < 0) {
            currTime = 0;
        }

        String currentDirectory = System.getProperty("user.dir");

        //create a file chooser with the current directory
        JFileChooser fileChooser = new JFileChooser(currentDirectory + "/arimaa/save");

        int result = fileChooser.showSaveDialog(null);
        //select file
        File selectedFile;
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                if (gameController.loggingEnabled) logger.info("File was selected");

                selectedFile = fileChooser.getSelectedFile();
                FileWriter fileWriter = new FileWriter(selectedFile + ".txt");

                // write each move, converting it to a string
                for (Move move : moveHistory.moves) {
                    fileWriter.write(move.toString());
                }
                //write game type
                if (gameController.vsPC) {
                    fileWriter.write("PC ");
                }

                //write reserve time
                fileWriter.write("G" + gameController.player1.getPlayerReserve() + " ");
                fileWriter.write("S" + gameController.player2.getPlayerReserve() + " ");

                //write curr time
                fileWriter.write(String.valueOf(currTime));

                fileWriter.close();
                if (gameController.loggingEnabled) logger.info("Content has been written to the file successfully!");


            } catch (IOException e) {
                if (gameController.loggingEnabled) logger.info("No file selected");

            }
        } else {
        if (gameController.loggingEnabled) logger.info("Save operation canceled by the user");

        }
    }
}

