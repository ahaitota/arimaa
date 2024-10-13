package com.pjv.View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pjv.Controller.GameController;

/**
 * The NewGameView class represents the window for starting a new game.
 */

public class NewGameView extends JFrame{
    GameController gameController;
    private JButton vsPersonButton;
    private JButton vsPcButton;

    /**
     * Constructs a NewGameView object.
     *
     * @param gameController The game controller responsible for handling game events.
     */

    public NewGameView (GameController gameController) {
        this.gameController = gameController;

        setTitle("New Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(null); 
        vsPersonButton = new JButton("VS Person");
        vsPcButton = new JButton("VS PC");
        vsPersonButton.setBounds(80, 200, 200, 60);
        vsPcButton.setBounds(330, 200, 200, 60);
        panel.add(vsPersonButton);
        panel.add(vsPcButton);
        add(panel);
        setSize(600,500);
        setLocationRelativeTo(null); 
        setVisible(true);

        //button to choose personVSperson mode
        vsPersonButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameView.this.setVisible(false);
                gameController.handleVSperson();
            }
        });

        //button to choose personVSpc mode
        vsPcButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NewGameView.this.setVisible(false);
                gameController.handleVSpc();
            }
        });
        
    }
}
