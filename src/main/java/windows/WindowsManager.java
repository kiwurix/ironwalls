package windows;
import javafx.application.Application;
import javafx.stage.Stage;
import ui.GameWindow;

import java.util.logging.Logger;

/**
 * Manages switching between different game windows like the menu and game.
 */
public class WindowsManager extends Application {
    /** Logger for tracking window events. */
    private static final Logger logger = Logger.getLogger(WindowsManager.class.getName());

    /** Reference to the current GameWindow. */
    private static GameWindow gameWindow;


    /**
     * Launches the game window.
     */
    public static void runGame(){
        gameWindow = new GameWindow();
        Stage gamestage = new Stage();
        gameWindow.start(gamestage);
        logger.info("Game window launched");
    }

    /**
     * Launches the main menu window.
     */
    public static void runMenu(){
        Stage menuStage = new Stage();
        MainMenu menu = new MainMenu();
        menu.start(menuStage);
        logger.info("Menu window launched");
    }

    /**
     * Displays the victory window.
     */
    public static void victoryWindow(){
        if(gameWindow!=null) {
            gameWindow.win();
            logger.info("Victory window displayed");
        }
    }

    /**
     * Displays the loss window.
     */
    public static void loseWindow(){
        if(gameWindow!=null) {
            gameWindow.lose();
            logger.info("Lose window displayed");
        }
    }

    /**
     * Entry point for the JavaFX application. Opens the main menu.
     * @param primaryStage the primary stage
     */
    @Override
    public void start(Stage primaryStage) {
        runMenu();
    }

}

