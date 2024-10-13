package com.pjv.Controller;

import java.util.Stack;
import java.io.File;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import com.pjv.Helpers.Clock;
import com.pjv.Helpers.ComputerPlayer;
import com.pjv.Helpers.MoveHistory;
import com.pjv.Helpers.Check;
import com.pjv.Helpers.LoadGame;
import com.pjv.Helpers.SaveGame;
import com.pjv.Model.Board;
import com.pjv.Model.Move;
import com.pjv.Model.Player;
import com.pjv.Model.Piece;
import com.pjv.Model.GameState;
import com.pjv.View.BoardView;
import com.pjv.View.FinishArrangement;
import com.pjv.View.MenuView;
import com.pjv.View.NewGameView;
import com.pjv.View.ShowTurns;

/**
 * GameController class manages the flow of the game, including player actions,
 * game state transitions, and interaction with views.
 */

public class GameController {
    public boolean loggingEnabled;
    public Logger logger = LoggerFactory.getLogger(GameController.class);
    public Board board;
    public BoardView boardView;
    public MenuView mainMenu;
    public NewGameView newGame;
    public SaveGame saveGame;
    public LoadGame loadGame;
    public ShowTurns showTurns;
    public FinishArrangement finishArrangement;
    public MoveHistory moveHistory;
    public Check check;
    public Clock clock;
    public Thread timerThread;
    public Player player1;
    public Player player2;
    public Player currentPlayer;
    public ComputerPlayer computerPlayer;

    //true - game, false - arrangement
    private boolean gameState = false;
    //if game is curr loading
    public boolean loadingGame = false;
    //if step back was made
    public boolean wasStepBack = false;
    //if it is vs pc mode
    public boolean vsPC = false;
    //if d=game was restarted
    private boolean gameWasRestarted = false;
    
    private int steps;
    public int goldTurns = 1;
    public int silverTurns = 1;
    private Stack<GameState> gameStates;

    /**
     * Constructor for GameController.
     * 
     * @param loggingEnabled Whether logging is enabled.
     */
    
    public GameController(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
        if (loggingEnabled) logger.info("Initializing GameController");

        this.board = new Board();
        this.boardView = new BoardView(this);
        this.mainMenu = new MenuView(this);
        this.finishArrangement = new FinishArrangement(this);
        this.player1 = new Player(Player.Color.GOLD);
        this.player2 = new Player(Player.Color.SILVER);
        this.currentPlayer = player1;
        this.steps = 4;
        this.computerPlayer = new ComputerPlayer(this, board);
        this.check = new Check(this, board, computerPlayer);
        this.moveHistory = new MoveHistory(this);
        this.clock = new Clock(this);
        this.saveGame = new SaveGame(this, clock);
        gameStates = new Stack<>();
        if (loggingEnabled) logger.info("GameController initialized successfully");
    }

    /**
     * Handles setting up the game for player vs player mode.
     */

    public void handleVSperson() {
        if (loggingEnabled) logger.info("Handling VS person mode");
        //finish arrangement button
        finishArrangement.setVisible(true);
        //fill the board and show it
        board.fillBoard();
        boardView.paintBoard();
        mainMenu.setVisible(false); 
        boardView.setVisible(true);
        if (loggingEnabled) logger.info("VS person mode set up completed");
    }

    /**
     * Handles setting up the game for player vs computer mode.
     */

    public void handleVSpc() {
        if (loggingEnabled) logger.info("Handling VS PC mode");
        //finish arrangement button
        finishArrangement.setVisible(true);
        //fill the board and show it
        board.fillBoard();
        boardView.paintBoard(); 
        vsPC = true;
        mainMenu.setVisible(false);
        boardView.setVisible(true); 
        if (loggingEnabled) logger.info("VS PC mode set up completed");
    }

     /**
     * Handles the new game button click, initializing a new game view.
     */

    public void handleNewGameButtonClick() {
        if (loggingEnabled) logger.info("New game was created");
        newGame = new NewGameView(this);
    }
    
    /**
     * Saves the current game state.
     */

    public void saveCurrGame() {
        if (loggingEnabled) logger.info("Saving current game state");
        saveGame.saveGame(moveHistory);
    }

     /**
     * Gets the name of the piece at the specified board position.
     * 
     * @param row The row of the piece.
     * @param col The column of the piece.
     * @return The name of the piece.
     */

    public String getPieceName(int row, int col) {
        return board.getPicName(row, col);
    }

    /**
     * Gets the number of steps remaining for the current player.
     * 
     * @return The number of steps remaining.
     */

    public int getSteps() {
        return this.steps;
    }

     /**
     * Gets the current game state.
     * 
     * @return True if the game is ongoing, false otherwise.
     */

    public boolean getGameState() {
        return this.gameState;
    }

     /**
     * Gets the current player.
     * 
     * @return The current player.
     */

    public Player getCurrPlayer() {
        return this.currentPlayer;
    }

    /**
     * Changes the current player to the other player and 
     * everything connected to this change.
     */

    public void changePlayer() {
        if (loggingEnabled) logger.info("Changing player");
        // if game is loading
        if (loadingGame) {
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            if (currentPlayer == player1) {
                goldTurns += 1;

            } else if (currentPlayer == player2) {
                silverTurns += 1;

            }
            steps = 4;
            
        //if it is just a middle of the game
        } else if (!check.pushCanBeDone && steps != 4) {
            //check winner
            Piece.Colors winnerColor = check.checkWinner(currentPlayer);
            if (winnerColor == Piece.Colors.GOLD) {
                if (loggingEnabled) logger.info("Gold player wins");
                clock.updateTimer(player1);
                boardView.disableAllButtons();
                showTurns.disableButtons();
                
            } else if (winnerColor == Piece.Colors.SILVER) {
                if (loggingEnabled) logger.info("Silver player wins");
                clock.updateTimer(player2);
                boardView.disableAllButtons();
                showTurns.disableButtons();
               
            }
            // clean game states for steps back
            //with pc your moves back are limited
            if (vsPC) {
                gameStates.clear();
            }
            
            clock.resetTimer();
            showTurns.historyView.append("\n");
            //change player
            currentPlayer = (currentPlayer == player1) ? player2 : player1;
            if (currentPlayer == player1 && (!wasStepBack || vsPC)) {
                goldTurns += 1;
                
            } else if (currentPlayer == player2 && (!wasStepBack || vsPC)) {
                silverTurns += 1;

            }
            wasStepBack = false;
            steps = 4;
            //append move history
            Move m = moveHistory.addTurnMarker();
            moveHistory.addMove(m); 
            showTurns.historyView.append(m.toString() + " ");
            //make pc player move
            if (vsPC && currentPlayer == player2 && !loadingGame) {
                computerPlayer.makeMove();
            }
        } 
    }

     /**
     * Starts the game.
     */

    public void startGame() {
        if (loggingEnabled) logger.info("Starting game");
        //go to the restarting function
        if (gameWasRestarted) {
            restartGame();
        
        // start the game if it wasnt restarted
        } else {        
            gameState = true;
            if (!loadingGame) {
                finishArrangement.setVisible(false);
            }
            this.showTurns = new ShowTurns(this); 
            //record figures setup 
            //if loading, it wss already recorded
            if (!loadingGame) {
                recordInitialSetup();
            }

            //show previous game history
            if (loadingGame) {
                String regex = "^\\d+[gs] ";
                Pattern pattern = Pattern.compile(regex);
                boolean firstStep = true;
                for (Move m : moveHistory.moves) {
                    String move = m.toString();
                    
                    Matcher matcher = pattern.matcher(move);
                    if (matcher.find() && !firstStep) {
                        showTurns.historyView.append("\n");
                    }
                    firstStep = false;
                    showTurns.historyView.append(move);
                }
            }
            //append steps to history view
            if (!loadingGame) {
                Move m = moveHistory.addTurnMarker();
                moveHistory.addMove(m); 
                showTurns.historyView.append(m.toString() + " ");
            }
            if (loggingEnabled) logger.info("Timer is working");
            //turn on timer
            clock.makeTimerVisible(true);
            timerThread = new Thread(clock);
            timerThread.start();
            loadingGame = false;
        }
    }

    /**
     * Rearranges two pieces on the board before the game started.
     * 
     * @param oldRow The original row of the piece.
     * @param oldCol The original column of the piece.
     * @param newRow The new row of the piece.
     * @param newCol The new column of the piece.
     */

    public void rearrTwoPieces(int oldRow, int oldCol, int newRow, int newCol) {
        if (loggingEnabled) logger.debug("Rearranging pieces from ({}, {}) to ({}, {})", oldRow, oldCol, newRow, newCol);
        if (board.board[oldRow][oldCol].getColor() == board.board[newRow][newCol].getColor()) {
            if (!vsPC || (vsPC && board.board[oldRow][oldCol].getColor() == Piece.Colors.GOLD)) {
                Piece temp = board.board[newRow][newCol];
                board.board[newRow][newCol] = board.board[oldRow][oldCol];
                board.board[oldRow][oldCol] = temp;
                boardView.paintBoard();
            } 
        }
    }

    /**
     * Records the initial setup of the board.
     */

    private void recordInitialSetup() {
        if (loggingEnabled) logger.info("Recording initial setup");
        for (int row = 7; row >= 0; row--) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.board[row][col];
                if (piece != null && (row == 0 || row == 1)) {
                    if (row == 1 && col == 0) {
                        currentPlayer = player2;
                        Move m = moveHistory.addTurnMarker();
                        moveHistory.addMove(m); 

                        currentPlayer = player1;
                    }
                    Move m = new Move(piece, row, col);
                    moveHistory.addMove(m); 

                } else if (piece != null && (row == 6 || row == 7)) {
                    if (row == 7 && col == 0) {
                        Move m = moveHistory.addTurnMarker();
                        moveHistory.addMove(m); 
                        goldTurns += 1;

                    }
                    Move m = new Move(piece, row, col);
                    moveHistory.addMove(m); 

                }
            }
        }
        
        showTurns.historyView.append(moveHistory.toString() + "\n");
    }

     /**
     * Puts piece on the board during loading.
     */

    public void putPieceOnBoard(Piece piece, int oldRow, int oldCol) {
        board.board[oldRow][oldCol] = piece;
    }

     /**
     * Checks if the move is valid, peforms the move and changes the board.
     */

    public boolean setPiece(int oldRow, int oldCol, int newRow, int newCol) {
        if (loggingEnabled) logger.debug("Checking piece to put on board from ({}, {}) to ({}, {})", oldRow, oldCol, newRow, newCol);
        //save game state before step
        int currTurns = 0;
        if (currentPlayer == player1) {
            currTurns = goldTurns;

        } else {
            currTurns = silverTurns;

        }
        GameState gameState = new GameState(board.board, steps, currentPlayer, currTurns, check.pushCanBeDone, check.pullCanBeDone, check.pushRow, check.pushCol, check.pushStrength,
        check.pullRow, check.pullCol, check.pullStrength);
        //check the step
        if (check.checkStep(oldRow, oldCol, newRow, newCol, 
        board.board[oldRow][oldCol].getType(), board.board[oldRow][oldCol].getColor(), currentPlayer, steps)) {
            if (loggingEnabled) logger.debug("Piece was put on board from ({}, {}) to ({}, {})", oldRow, oldCol, newRow, newCol);
            
            //add game state
            gameStates.push(gameState);

            steps -= 1;
            //update board
            board.board[newRow][newCol] = board.board[oldRow][oldCol];
            board.board[oldRow][oldCol] = null;

            if (!loadingGame) {
                // Add the move to history
                Move move = new Move(board.board[newRow][newCol], oldRow, oldCol, newRow, newCol);
                moveHistory.addMove(move);
                showTurns.historyView.append(move.toString() + " ");

            }
            //check traps
            Move move = check.checkTraps();
            if (vsPC && move != null) {
                //send info to pc player about deleted figures
                computerPlayer.wasTrapped(move);
            }
            //append history view
            if (!loadingGame && move != null) {
                moveHistory.addMove(move);
                showTurns.historyView.append(move.toString() + " ");

            }
            //update view of the board
            boardView.paintBoard();
            return true;
        }  
        return false;
    }

     /**
     * Performs a step back in the game to a previous state.
     */

    public void makeStepBack() {
        if (!gameStates.isEmpty()) {
            if (loggingEnabled) logger.info("Step back was made");
            wasStepBack = true;
            //get previous state from saved states
            GameState previousState = gameStates.pop();
            board.board = previousState.getBoard();
            steps = previousState.getSteps();
            //change player
            currentPlayer = previousState.getPlayer();
            if (currentPlayer == player1) {
                goldTurns = previousState.getTurns();

            } else {
                silverTurns = previousState.getTurns();

            }
            //return check instance into the same state as in previous state
            check.pushCanBeDone =  previousState.isPushCanBeDone();
            check.pullCanBeDone = previousState.isPullCanBeDone(); 
            check.pushRow = previousState.getPushRow();
            check.pushCol = previousState.getPushCol();
            check.pushStrength = previousState.getPushStrength();
            check.pullRow = previousState.getPullRow();
            check.pullCol = previousState.getPullCol();
            check.pullStrength = previousState.getPullStrength();
            //update the board view
            boardView.paintBoard();

            //change the history view, delete reversed steps

            String regex1 = "([A-Za-z][a-h][1-8]x)";
            Pattern pattern1 = Pattern.compile(regex1);
            
            String regex2 = "^\\d+[gs] ";
            Pattern pattern2 = Pattern.compile(regex2);
            
            Move move1 = moveHistory.getLast();
            String m1 = move1.toString();
            Matcher matcher1 = pattern1.matcher(m1);
            Matcher matcher2 = pattern2.matcher(m1);

            if (matcher1.find() || matcher2.find()) {
                //if piece was trapped remove one more
                moveHistory.removeMove(moveHistory.getsize() - 1);    
            }
            if (matcher2.find()) {
                //remove turn marker 
                moveHistory.removeMove(moveHistory.getsize() - 1);    
            }
            //remove the step itself
            moveHistory.removeMove(moveHistory.getsize() - 1);


            StringBuilder historyText = new StringBuilder();
            //rewrite the history into the history view
            for (Move move : moveHistory.moves) {
                String m = move.toString();
                Matcher matcher3 = pattern2.matcher(m);

                if (matcher3.find()) {
                    // This ensures we do not add a newline before the first move ("1g")
                    if (historyText.length() > 0) {
                        historyText.append("\n");
                    }
                }

                historyText.append(m).append(" ");
                
            }
            showTurns.historyView.setText(historyText.toString());
            

        } else {
            if (loggingEnabled) logger.info("No moves to undo");
        }
    }

    /**
     * Handles loading game button click.
     */

    public void handleLoadGameButtonClick() {
        if (loggingEnabled) logger.info("The game is loading");
        loadingGame = true;
        goldTurns = 1;
        silverTurns = 0;
        this.loadGame = new LoadGame(moveHistory, this, clock);
        //chose file to read
        File file = loadGame.choseFile();
        if (file != null) {
            if (loggingEnabled) logger.info("Loading saved game from {}", file);
            //read game
            loadGame.readGameHistory(file);
            //update board view
            boardView.paintBoard(); 
            mainMenu.setVisible(false);
            boardView.setVisible(true); 

            startGame();
        }
    }   

    public void handlerestartGameButton() {
        if (loggingEnabled) logger.info("Restarting game");
        //reset all variables, close unnecessary windows
        boardView.setVisible(false);
        showTurns.setVisible(false);
        showTurns.historyView.setText("");

        clock.resetTimer();
        clock.makeTimerVisible(false);
        
        gameState = false;
        gameWasRestarted = true;
        player1 = new Player(Player.Color.GOLD);
        player2 = new Player(Player.Color.SILVER);
        currentPlayer = player1;
        steps = 4;
        goldTurns = 1;
        silverTurns = 1;
        vsPC = false;
        //clean all history
        moveHistory.moves.clear();
        gameStates.clear();
        
        check = new Check(this, board, computerPlayer);
        //enable all buttons for next game
        boardView.enableAllButtons();
        showTurns.enableButtons();
        
        board.cleanBoard();
        
        mainMenu.setVisible(true);
    }

    /**
     * Restarts the game.
     */

    public void restartGame() {
        if (loggingEnabled) logger.info("Restart game function was called");
        //show windows
        finishArrangement.setVisible(false);
        mainMenu.setVisible(false);
        showTurns.setVisible(true);
        boardView.setVisible(true);
        gameState = true;
        //turn on timer
        clock.setTextInvisible();
        clock.setTimerVisible();
        clock.running = true;
        clock.makeTimerVisible(true);

        //if reset was made with loading
        if (loadingGame) {
            String regex = "^\\d+[gs] ";
            Pattern pattern = Pattern.compile(regex);
            boolean firstStep = true;
            for (Move m : moveHistory.moves) {
                String move = m.toString();
                
                Matcher matcher = pattern.matcher(move);
                if (matcher.find() && !firstStep) {
                    showTurns.historyView.append("\n");
                }
                firstStep = false;
                showTurns.historyView.append(move);
            }
        } else {
            //if reset for a new game
            recordInitialSetup();

            String regex2 = "^\\d+[gs] ";
            Pattern pattern2 = Pattern.compile(regex2);
    
            StringBuilder historyText = new StringBuilder();
                //rewrite the history
                for (Move move : moveHistory.moves) {
                    String m = move.toString();
                    Matcher matcher3 = pattern2.matcher(m);
    
                    if (matcher3.find()) {
                        // This ensures we do not add a newline before the first move ("1g")
                        if (historyText.length() > 0) {
                            historyText.append("\n");
                        }
                    }
    
                    historyText.append(m).append(" ");
                    
                }
            showTurns.historyView.setText(historyText.toString());
        }

        // Add the initial turn marker to history
        if (!loadingGame) {
            Move m = moveHistory.addTurnMarker();
            moveHistory.addMove(m);
            showTurns.historyView.append("\n");
            showTurns.historyView.append(m.toString() + " ");
        }
        
        gameWasRestarted = false;
        loadingGame = false;
        if (loggingEnabled) logger.info("Game restarted successfully");
    }
    
}
