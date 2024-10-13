package com.pjv.View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pjv.Controller.GameController;

/**
 * The ShowTurns class represents the window for displaying game turn options and history.
 */

public class ShowTurns extends JFrame{
    GameController gameController;
    private JButton FinishMyTurnButton;
    private JButton SaveGameButton;
    private JButton StepBackButton;
    private JButton RestartGame;
    public JTextArea historyView;

    /**
     * Constructs a ShowTurns object.
     *
     * @param gameController The game controller responsible for handling game events.
     */

    public ShowTurns (GameController gameController) {
        this.gameController = gameController;
        
        setTitle("Finish My Turn");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null); 

        FinishMyTurnButton = new JButton("Finish My Turn");
        FinishMyTurnButton.setBounds(50, 300, 200, 100);
        panel.add(FinishMyTurnButton);
        add(panel);

        SaveGameButton = new JButton("Save Game");
        SaveGameButton.setBounds(50, 200, 200, 100);
        panel.add(SaveGameButton);
        add(panel);

        StepBackButton = new JButton("Step back");
        StepBackButton.setBounds(50, 400, 200, 100);
        panel.add(StepBackButton);
        add(panel);

        RestartGame = new JButton("Restart Game");
        RestartGame.setBounds(50, 500, 200, 100);
        panel.add(RestartGame);
        add(panel);

        historyView = new JTextArea();
        historyView.setLineWrap(true);
        historyView.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(historyView);
        scrollPane.setBounds(0, 0, 300, 200);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);

        setSize(300,650);
        setLocation(1100, 170);
        setVisible(true);

        //button to finish turn
        FinishMyTurnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.changePlayer();
            }
        });

        //button to save the game
        SaveGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.saveCurrGame();
            }
        });

        //button to make step back
        StepBackButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.makeStepBack();
            }
        });

        //button to restart the game
        RestartGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.handlerestartGameButton();
            }
        });

    }

    /**
     * Disables all buttons in the window.
     */

    public void disableButtons() {
        FinishMyTurnButton.setEnabled(false);
        SaveGameButton.setEnabled(false);
        StepBackButton.setEnabled(false);
    }

     /**
     * Enables all buttons in the window.
     */

    public void enableButtons() {
        FinishMyTurnButton.setEnabled(true);
        SaveGameButton.setEnabled(true);
        StepBackButton.setEnabled(true);
    }


}
