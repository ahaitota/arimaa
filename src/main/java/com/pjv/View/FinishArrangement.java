package com.pjv.View;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pjv.Controller.GameController;

/**
 * The FinishArrangement class represents a dialog window to finish the initial arrangement phase of the game.
 */

public class FinishArrangement extends JFrame{
    GameController gameController;
    private JButton FinishArrangement;

     /**
     * Constructs a FinishArrangement object with the specified GameController.
     *
     * @param gameController the GameController associated with the game
     */

    public FinishArrangement (GameController gameController) {
        this.gameController = gameController;
        
        setTitle("Finish Arrangement");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null); 

        FinishArrangement = new JButton("Finish Arrangement");
        FinishArrangement.setBounds(0, 0, 300, 150);
        panel.add(FinishArrangement);
        add(panel);
        setSize(300,150);
        setLocation(1100, 400);
        setVisible(false);
        
        //button for finishing arrangement of figures
        FinishArrangement.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.startGame();
            }
        });

    }
}
