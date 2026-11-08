package windows;

import exceptions.ImgNotFoundException;
import serialization.RecordManager;
import game.Game;
import game.Maps;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.util.logging.Logger;

/**
 * Displays the main menu of the game, allowing the player to select a map and difficulty.
 */
public class MainMenu extends Application {
    /** Background image of the main menu. */
    private static Image backgroundImg;

    static {
        try {
            findZombieImages();
        } catch (ImgNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void findZombieImages() throws ImgNotFoundException {
        backgroundImg = new Image("ui/img/MainMenuBackground.png");
        if (backgroundImg.isError()) throw new ImgNotFoundException("Main menu window background");
    }



    BackgroundImage bgImage = new BackgroundImage(
            backgroundImg,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(
                    100, 100, true, true, true, false
            )
    );

    private Logger logger = Logger.getLogger(MainMenu.class.getName());

    /**
     * Starts the JavaFX application and initializes the main menu UI.
     * @param stage the stage
     */
    @Override
    public void start(Stage stage) {
        stage.setTitle("Main Menu");
        Button map1Button = new Button("Play Map 1");
        Button map2Button = new Button("Play Map 2");

        map1Button.setPrefSize(200,40);
        map2Button.setPrefSize(200,40);

        map1Button.setFocusTraversable(false);
        map2Button.setFocusTraversable(false);

        map1Button.setOnAction(e -> {
            Maps.setSelectedMap(1);
            logger.info("Map 1 selected");
            WindowsManager.runGame();
            stage.close();
        });

        map2Button.setOnAction(e -> {
            Maps.setSelectedMap(2);
            logger.info("Map 2 selected");
            WindowsManager.runGame();
            stage.close();
        });


        Label difficultyLabel = new Label("Difficulty: Easy");
        difficultyLabel.setStyle("-fx-text-fill: #ff0000; -fx-font-weight: bold; -fx-font-size: 20px;");

        RecordManager.loadRecord();
        String dif = switch (RecordManager.getDifficulty()){
            case 1->"Easy";
            case 2->"Medium";
            case 3->"Hard";
            default -> "Easy";
        };
        Label recordLabel = new Label("Your record:\nWave "+RecordManager.getBestWave()+" with difficulty "+ dif);
        recordLabel.setStyle("-fx-text-fill: #dcff00; -fx-font-weight: bold; -fx-font-size: 20px;");

        Slider difficultySlider = new Slider(1, 3, 1);
        difficultySlider.setMajorTickUnit(1);
        difficultySlider.setMinorTickCount(0);
        difficultySlider.setSnapToTicks(true);
        difficultySlider.setShowTickMarks(true);
        difficultySlider.setShowTickLabels(false);
        difficultySlider.setBlockIncrement(1);

        difficultySlider.valueProperty().addListener((obg, oldVal, newVal) -> {
            int difficulty = newVal.intValue();
            Game.setDifficulty(difficulty);
            String level = switch (difficulty) {
                case 1 -> "Easy";
                case 3 -> "Hard";
                default -> "Medium";
            };
            difficultyLabel.setText("Difficulty: " + level);
            logger.info("Difficulty level set: " + level);
        });

        VBox layout = new VBox(20, map1Button, map2Button,recordLabel,difficultyLabel,difficultySlider);
        layout.setStyle("-fx-padding: 40; -fx-alignment: center;");
        layout.setBackground(new Background(bgImage));
        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}
