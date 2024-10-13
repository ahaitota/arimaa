package com.pjv;
import com.pjv.Controller.GameController;

/**
 * The Main class contains the main method to start the Arimaa game application.
 */

public class Main {
     /**
     * The entry point of the application.
     *
     * @param args Command-line arguments. If provided, enables logging.
     */
    public static void main(String[] args) {

        boolean loggingEnabled = false;

        //if args were given loggingEnabled - true
        if (args.length > 0) {
            loggingEnabled = true;
        }

        // Create a new instance of the GameController
        GameController controller = new GameController(loggingEnabled);
        
    }
}
