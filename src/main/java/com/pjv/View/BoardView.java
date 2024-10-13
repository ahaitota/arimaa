package com.pjv.View;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import com.pjv.Controller.GameController;

/**
 * The BoardView class represents the graphical user interface for the Arimaa game board.
 */

public class BoardView extends JFrame {
    private JPanel panel;
    private Image backImg;
    private GameController gameController;
    private int boardsize = 8;
    private JButton[][] icons;
    private boolean firstClick = true;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private int selectedRow2 = -1;
    private int selectedCol2 = -1;

    /**
     * Constructs a BoardView object with the specified GameController.
     *
     * @param gameController the GameController associated with the game board
     */

    public BoardView(GameController gameController) {
        super("Arimaa Game Board");
        this.gameController = gameController;
        panel = new JPanel();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 650); 
        setLocationRelativeTo(null); 
        setVisible(false);
        //get board img
        ImageIcon imageIcon = new ImageIcon(getClass().getResource("/board.jpg"));
        backImg = imageIcon.getImage(); 
        icons = new JButton[boardsize][boardsize];
        //create panel
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backImg, 0, 0, getWidth(), getHeight(), this);
            }
        };
        panel.setLayout(new GridLayout(boardsize, boardsize));
        add(panel);
        //put pieces on board
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                //make it a button
                JButton icon = new JButton();
                icon.setOpaque(false);
                icon.setContentAreaFilled(false); 
                icon.setBorderPainted(false);
                panel.add(icon);
                icons[i][j] = icon;
                //add listener
                icon.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //if it is the arrangement of figures in the beginning
                        if (!gameController.getGameState()) {
                            //first click and there is something on the board
                            if (firstClick && icon.getIcon() != null) {
                                savePiece(icon);
                                firstClick = false;
                            //second click and there is smth
                            } else if (!firstClick && icon.getIcon() != null) {
                                movePiece(icon);
                                //change them with each other
                                gameController.rearrTwoPieces(selectedRow, selectedCol, selectedRow2, selectedCol2);
                                firstClick = true;
                            }
                        //if it is a game
                        } else {
                            //first click or not null(can choose another figure)
                            if ((firstClick || icon.getIcon() != null) && gameController.getSteps() > 0) {
                                savePiece(icon);
                                if (gameController.getPieceName(selectedRow, selectedCol) != null) {
                                    firstClick = false;
                                }     
                            //second click
                            } else if (!firstClick){
                                movePiece(icon);
                                gameController.setPiece(selectedRow, selectedCol, selectedRow2, selectedCol2);
                                firstClick = true;
                            }
                        }
                    }
                });   
            }
        }

    }

    /**
     * Disables all buttons on the game board.
     */

    public void disableAllButtons() {
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                icons[i][j].setEnabled(false);
            }
        }
    }


    /**
     * Enables all buttons on the game board.
     */

    public void enableAllButtons() {
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                icons[i][j].setEnabled(true);
            }
        }
    }
    
     /**
     * Paints the game board based on the current state of the game.
     */

    public void paintBoard() {
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                //get picture name
                String type = gameController.getPieceName(i, j);
                if (type != null) {
                    //put it on board
                    ImageIcon piece = new ImageIcon(getClass().getResource("/" + type + ".png"));
                    icons[i][j].setIcon(piece);
                } else {
                    icons[i][j].setIcon(null);
                }
            }
        }
    }

     /**
     * Saves the selected piece's position when clicked.
     *
     * @param icon the JButton representing the selected piece
     */

    public void savePiece(JButton icon) {
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                if (icons[i][j] == icon) {
                    selectedRow = i;
                    selectedCol = j;
                    break;
                }
            }
        }
    }

     /**
     * Saves the selected position.
     *
     * @param icon the JButton representing the clicked position
     */

    public void movePiece(JButton icon) {
        for (int i = 0; i < boardsize; i++) {
            for (int j = 0; j < boardsize; j++) {
                if (icons[i][j] == icon) {
                    selectedRow2 = i;
                    selectedCol2 = j;
                    break;
                }
            }
        }
    }
    

}
