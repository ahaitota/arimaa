package com.pjv.Helpers;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pjv.View.MyTimer;
import com.pjv.Controller.GameController;
import com.pjv.Model.Player;

/**
 * This class handles the game clock and timer functionality.
 */

public class Clock implements Runnable {
    private int totalSeconds = 300;
    private int reserveTime;
    private GameController gameController;
    private MyTimer showTime;
    private int minutes, seconds;
    private boolean usingReserveTime = false;
    public boolean running = true;
    Logger logger = LoggerFactory.getLogger(Clock.class);

     /**
     * Constructor for the Clock class.
     * 
     * @param gameController the GameController instance
     * 
     */

    public Clock(GameController gameController) {
        this.gameController = gameController;
        this.showTime = new MyTimer();
    }

    /**
     * Returns total time.
     * 
     * @return time in seconds
     */

    public int getTotalTime() {
        return totalSeconds;
    }

    /**
     * Resets the game clock while loading from the file.
     * 
     * @param newTime new time for the clock
     */

    public void resetTotalTime(int newTime) {
        this.totalSeconds = newTime;

    }

    /**
     * Resets the game clock after the player's turn was over.
     */

    public void resetTimer() {
        if (gameController.loggingEnabled) logger.info("Timer was reseted");

        if (reserveTime == 0) {
            //reserv wasnt used, player's reserve wont be changed
            gameController.getCurrPlayer().setPlayerReserve(gameController.getCurrPlayer().getPlayerReserve() +  totalSeconds);
        } else {
            //reserv was used 
            gameController.getCurrPlayer().setPlayerReserve(reserveTime);
        }
        usingReserveTime = false;
        totalSeconds = 300;
        reserveTime = 0;
    }

    /**
     * Updates the game clock window after the win.
     * 
     * @param player player who won
     */

    public void updateTimer(Player player) {
        //show winner
        showTime.label.setText(player.getColor() + " won"); 
        //turn off timer
        showTime.label2.setVisible(false); 
        //show text
        showTime.label.setVisible(true);
        

    }

    /**
     * Makes timer frame visible/invisible.
     * 
     * @param turn true - visible, false otherwise
     */

    public void makeTimerVisible(boolean turn) {
        showTime.makeVisible(turn);
    }

    /**
     * Makes timer lable invisible.
     */

    public void setTextInvisible() {
        showTime.label.setVisible(false);
    }

    /**
     * Makes timer lable visible.
     */

    public void setTimerVisible() {
        showTime.label2.setVisible(true);
    }

    /**
     * The run method of the clock thread. It decreases the remaining time
     * of the active player by one second at regular intervals and checks for
     * timeout.
     */

    @Override
    public void run() {
        if (gameController.loggingEnabled) logger.info("Time is running");

        while (running) {
            long startTime = System.currentTimeMillis();
            
            while (totalSeconds >= 0 && running) {
                
                long currentTime = System.currentTimeMillis();
                //compare curr time with saved, if 1 sec passed - run
                if ((currentTime - startTime) >= 1000) {
                    startTime = currentTime;
                    //count mis and secs
                    minutes = totalSeconds / 60;
                    seconds = totalSeconds % 60;
                    totalSeconds--;
                        
                    SwingUtilities.invokeLater(() -> {
                        showTime.label2.setText(gameController.getCurrPlayer().getColor() + "'s Time: " + String.format("%d:%02d", minutes, seconds));
                    });
                   
                }

            }
            usingReserveTime = true;
            reserveTime = gameController.getCurrPlayer().getPlayerReserve();

            
            //switch to reserve time
            startTime = System.currentTimeMillis();
            while (reserveTime >= 0 && usingReserveTime && running) {
                long currentTime = System.currentTimeMillis();

                if ((currentTime - startTime) >= 1000) {
                    startTime = currentTime;

                    minutes = reserveTime / 60;
                    seconds = reserveTime % 60;
                    reserveTime--;
                    if (usingReserveTime) {
                        SwingUtilities.invokeLater(() -> {
                            showTime.label2.setText(gameController.getCurrPlayer().getColor() + "'s RESERVE Time: " + String.format("%d:%02d", minutes, seconds));
                        });
                    } else {
                        break;
                    }  
                }
                //player lost
                if (reserveTime == 0 - 1) {
                    if (gameController.getCurrPlayer() == gameController.player1) {
                        updateTimer(gameController.player2);
                    } else {
                        updateTimer(gameController.player1);
                    }
                    
                    break;
                }
                
            }
            
        }
    }   
    
    
    
}
