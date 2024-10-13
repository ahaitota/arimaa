package com.pjv.View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pjv.Controller.GameController;

/**
 * The MenuView class represents the main menu of the game.
 */

public class MenuView extends JFrame {

    GameController gameController;
    private JButton newGameButton;
    private JButton loadGameButton;

    /**
     * Constructs a MenuView object with the specified GameController.
     *
     * @param gameController the GameController associated with the game
     */

    public MenuView (GameController gameController) {
        this.gameController = gameController;

        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(null); 
        newGameButton = new JButton("New Game");
        loadGameButton = new JButton("Load Game");
        newGameButton.setBounds(80, 200, 200, 60);
        loadGameButton.setBounds(330, 200, 200, 60);
        panel.add(newGameButton);
        panel.add(loadGameButton);
        add(panel);
        setSize(600,500);
        setLocationRelativeTo(null); 
        setVisible(true);

        //button for new game
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.handleNewGameButtonClick();
            }
        });

        //button for loading game
        loadGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.handleLoadGameButtonClick();
            }
        });
        
    }
   
}
