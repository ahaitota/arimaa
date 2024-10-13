import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.pjv.Controller.GameController;
import com.pjv.Helpers.ComputerPlayer;
import com.pjv.Model.Board;
import java.util.Arrays;
import java.util.List;


public class ComputerPlayerTest {
    private GameController gameController;
    private Board board = new Board();
    private ComputerPlayer computerPlayer;

    @BeforeEach
    void setUp() {
        gameController = new GameController(false);
    
        computerPlayer = new ComputerPlayer(gameController, board);
    }

    @Test
    void choseStepsAmount() {
        int steps = computerPlayer.choseStepsAmount();
        assertTrue(steps >= 1 && steps <= 4);
    }

    @Test
    void getRandomMove() {
        List<int[]> movesToChoose = Arrays.asList(new int[]{1, 0}, new int[]{-1, 0}, new int[]{0, 1}, new int[]{0, -1});
        int[] move = computerPlayer.getRandomMove(movesToChoose);
        assertNotNull(move);
        assertTrue(movesToChoose.contains(move));
    }

}
