import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.pjv.Controller.GameController;
import com.pjv.Model.Board;
import com.pjv.Model.Player;
import com.pjv.View.BoardView;
import com.pjv.View.MenuView;
import com.pjv.View.NewGameView;
import com.pjv.View.FinishArrangement;
import com.pjv.View.ShowTurns;
import com.pjv.Helpers.*;

public class GameControllerTest {

    @Mock
    private Logger mockLogger;
    @Mock
    private Board mockBoard;
    @Mock
    private BoardView mockBoardView;
    @Mock
    private MenuView mockMenuView;
    @Mock
    private NewGameView mockNewGameView;
    @Mock
    private SaveGame mockSaveGame;
    @Mock
    private LoadGame mockLoadGame;
    @Mock
    private ShowTurns mockShowTurns;
    @Mock
    private FinishArrangement mockFinishArrangement;
    @Mock
    private MoveHistory mockMoveHistory;
    @Mock
    private Check mockCheck;
    @Mock
    private Clock mockClock;
    @Mock
    private ComputerPlayer mockComputerPlayer;

    private GameController gameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        gameController = new GameController(false);

        // Inject mock dependencies
        gameController.logger = mockLogger;
        gameController.board = mockBoard;
        gameController.boardView = mockBoardView;
        gameController.mainMenu = mockMenuView;
        gameController.newGame = mockNewGameView;
        gameController.saveGame = mockSaveGame;
        gameController.loadGame = mockLoadGame;
        gameController.showTurns = mockShowTurns;
        gameController.finishArrangement = mockFinishArrangement;
        gameController.moveHistory = mockMoveHistory;
        gameController.check = mockCheck;
        gameController.clock = mockClock;
        gameController.computerPlayer = mockComputerPlayer;
        gameController.loggingEnabled = true;
    }

    @Test
    public void testInitializeGameController() {
        assertTrue(gameController.loggingEnabled);

    }

    @Test
    public void testHandleVSperson() {
        gameController.handleVSperson();

        verify(mockFinishArrangement).setVisible(true);
        verify(mockBoard).fillBoard();
        verify(mockBoardView).paintBoard();
        verify(mockMenuView).setVisible(false);
        verify(mockBoardView).setVisible(true);
    }

    @Test
    public void testHandleVSpc() {
        gameController.handleVSpc();

        verify(mockFinishArrangement).setVisible(true);
        verify(mockBoard).fillBoard();
        verify(mockBoardView).paintBoard();
        assertTrue(gameController.vsPC);
        verify(mockMenuView).setVisible(false);
        verify(mockBoardView).setVisible(true);
    }

    @Test
    public void testHandleNewGameButtonClick() {
        gameController.handleNewGameButtonClick();

        assertNotNull(gameController.newGame);
    }

    @Test
    public void testSaveCurrGame() {
        gameController.saveCurrGame();

        verify(mockSaveGame).saveGame(mockMoveHistory);
    }

    @Test
    public void testGetSteps() {
        int steps = gameController.getSteps();

        assertEquals(4, steps);
    }

    @Test
    public void testGetGameState() {
        boolean gameState = gameController.getGameState();

        assertFalse(gameState);
    }

    @Test
    public void testGetCurrPlayer() {
        Player currPlayer = gameController.getCurrPlayer();

        assertNotNull(currPlayer);
        assertEquals(Player.Color.GOLD, currPlayer.getColor());
    }

    
}
