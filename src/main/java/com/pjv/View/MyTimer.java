package com.pjv.View;

import javax.swing.*;
import java.awt.*;

/**
 * The MyTimer class represents the game timer window.
 */

public class MyTimer extends JFrame{
    //private JFrame frame;
    public JLabel label;
    public JLabel label2;

    /**
     * Constructs a MyTimer object.
     */

    public MyTimer () {
        
        super.setTitle("Game Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocation(100, 170);
        setLayout(new FlowLayout());
        setVisible(false);

        //winner text
        label = new JLabel();
        add(label);
        label.setVisible(false);

        //timer text
        label2 = new JLabel();
        add(label2);
        label2.setVisible(true);
        
    }

     /**
     * Sets the visibility of the timer window.
     *
     * @param turn true to make the window visible, false otherwise
     */

    public void makeVisible(boolean turn) {
        setVisible(turn);
    }
}
