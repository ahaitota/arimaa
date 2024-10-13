import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.pjv.Controller.GameController;
import com.pjv.Model.Board;
import com.pjv.Helpers.Check;
import com.pjv.Helpers.ComputerPlayer;
import com.pjv.Model.Piece;
import com.pjv.Model.Player;

public class CheckTest {
    private GameController gameController;
    private Board board;
    private ComputerPlayer computerPlayer;
    private Check check;

    @BeforeEach
    void setUp() {
        gameController = new GameController(false);
        board = new Board();
        board.fillBoard();
        check = new Check(gameController, board, computerPlayer);
    }

    @Test
    void checkBoundsValidPosition() {
        assertTrue(check.checkBounds(0, 0));
        assertFalse(check.checkBounds(8, 8));
    }

    @Test
    void checkBoundsOutsideBounds() {
        assertFalse(check.checkBounds(-1, 0));
        assertFalse(check.checkBounds(0, -1));
        assertFalse(check.checkBounds(8, 0));
        assertFalse(check.checkBounds(0, 8));
    }

    @Test
    void checkFreeSpace() {
        assertTrue(check.checkFreeSpace(3, 3));
        assertFalse(check.checkFreeSpace(1, 1));

    }

    @Test
    void checkOrthogonal() {
        assertTrue(check.checkOrthogonal(0, 0, 0, 1));
        assertTrue(check.checkOrthogonal(0, 0, 1, 0));
        assertFalse(check.checkOrthogonal(0, 0, 1, 1));

    }

    @Test
    void checkRabbit_ValidMove() {
        assertTrue(check.checkRabbit(0, 1, Piece.Colors.SILVER));
        assertTrue(check.checkRabbit(7, 6, Piece.Colors.GOLD));
        assertFalse(check.checkRabbit(0, 1, Piece.Colors.GOLD));
        assertFalse(check.checkRabbit(7, 6, Piece.Colors.SILVER));
    }

    @Test
    void checkNoRabbits() {
        Player goldPlayer = new Player(Player.Color.GOLD);
        Player silverPlayer = new Player(Player.Color.SILVER);

        //no one won
        board.cleanBoard();
        board.board[3][3] = new Piece(Piece.Pieces.RABBIT, Piece.Colors.SILVER);
        board.board[6][6] = new Piece(Piece.Pieces.RABBIT, Piece.Colors.GOLD);
        assertEquals(null, check.checkNoRabbits(goldPlayer));

        //gold won
        board.cleanBoard();
        board.board[3][3] = new Piece(Piece.Pieces.CAMEL, Piece.Colors.SILVER);
        board.board[6][6] = new Piece(Piece.Pieces.RABBIT, Piece.Colors.GOLD);
        assertEquals(Piece.Colors.GOLD, check.checkNoRabbits(goldPlayer));

        //no one has rabbits, but it was silver's turn
        board.cleanBoard();
        board.board[3][3] = new Piece(Piece.Pieces.CAMEL, Piece.Colors.SILVER);
        board.board[6][6] = new Piece(Piece.Pieces.CAT, Piece.Colors.GOLD);
        assertEquals(Piece.Colors.SILVER, check.checkNoRabbits(silverPlayer));
        
    }

}
