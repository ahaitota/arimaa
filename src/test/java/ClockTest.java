
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.pjv.Controller.GameController;
import com.pjv.Helpers.Clock;

public class ClockTest {

    @Test
    public void testTotalTime() {
        // Create a GameController instance for testing with logging enabled
        GameController gameController = new GameController(false);
        
        // Create a Clock instance
        Clock clock = new Clock(gameController);
        
        // Test getTotalTime and resetTotalTime methods
        assertEquals(300, clock.getTotalTime()); 
        
        // Change total time and test
        clock.resetTotalTime(50);
        assertEquals(50, clock.getTotalTime());
    }
}
