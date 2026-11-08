package ui;

import factories.DefensiveUnitFactory;
import serialization.RecordManager;
import windows.WindowsManager;
import game.Game;
import game.GameLoop;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import game.GameController;
import javafx.util.Duration;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.util.logging.Logger;

import static ui.UpgradeWindow.isOpen;
import static ui.UpgradeWindow.upgradeWindow;

/**
 * The main window for the Tower Defense game.
 * Handles user input, game rendering, and game loop control.
 */
public class GameWindow extends Application {
    Logger logger = Logger.getLogger(GameWindow.class.getName());
    private enum SelectedType {
        NONE, SNIPER, WALL, BOMBER, SOLDIER_FACTORY
    }
    private SelectedType selectedType = SelectedType.NONE;
    MapView mapView = new MapView();
    GraphicsContext gc = mapView.getGraphicsContext();
    GameController gameController = new GameController();
    private Timeline timeline;
    HBox bottomBar = new HBox(10);
    private int highlightX = -1;
    private int highlightY = -1;
    private int highlightRange=0;
    private int mouseX;
    private int mouseY;

    GameLoop gameLoop = new GameLoop(60,gameController);

    private void foundHighlightRange(){
        highlightRange = switch(selectedType){
            case SNIPER -> DefensiveUnitFactory.createSniper(0, 0).getRange();
            case BOMBER -> DefensiveUnitFactory.createBomber(0, 0).getRange();
            case WALL, SOLDIER_FACTORY -> 0;
            default -> 0;
        };
    }

    /**
     * Initializes and starts the game window.
     * @param stage the stage
     */
    @Override
    public void start(Stage stage) {

        ZombieDeathEffectView.getZombieDeathEffects().clear();

        bottomBar.setStyle("-fx-padding: 10; -fx-background-color: #dddddd; -fx-alignment: center;");
        bottomBar.setPrefHeight(80);

        Button sniperButton = new Button("1\nSniper");
        sniperButton.setFocusTraversable(false);
        sniperButton.setOnAction(e -> selectedType = SelectedType.SNIPER);

        Button wallButton = new Button("2\nWall");
        wallButton.setFocusTraversable(false);
        wallButton.setOnAction(e -> selectedType = SelectedType.WALL);

        Button bomberButton = new Button("3\nBomber");
        bomberButton.setFocusTraversable(false);
        bomberButton.setOnAction(e -> selectedType = SelectedType.BOMBER);

        Button soldierFactoryButton = new Button("4\nSoldier Factory");
        soldierFactoryButton.setFocusTraversable(false);
        soldierFactoryButton.setOnAction(e -> selectedType = SelectedType.SOLDIER_FACTORY);

        mapView.setOnMouseClicked(event -> {
            if(isOpen())return;
            int X = ((int) (event.getX()/40))*40;
            int Y = ((int) (event.getY()/40))*40;
            logger.info("Mouse clicked at (" + X + ", " + Y + "). Selected type: " + selectedType);
            switch (selectedType) {
                case SNIPER -> gameController.placeDefensiveUnit(
                        DefensiveUnitFactory.createSniper(X, Y));
                case WALL -> gameController.placeDefensiveUnit(
                        DefensiveUnitFactory.createWall(X, Y));
                case BOMBER -> gameController.placeDefensiveUnit(
                        DefensiveUnitFactory.createBomber(X, Y));
                case SOLDIER_FACTORY -> gameController.placeSoldierFactory(X, Y);
                case NONE -> {
                    if(!isOpen()) {
                        logger.info("Opening upgrade window for unit at (" + X + ", " + Y + ")");
                        upgradeWindow(X,Y,gameController,this);
                        highlightX = X / 40;
                        highlightY = Y / 40;
                    }
                }

            }
            selectedType = SelectedType.NONE;
        });

        mapView.setOnMouseMoved(event -> {
            mouseX = (int) (event.getX() / 40);
            mouseY = (int) (event.getY() / 40);
            logger.fine("Mouse moved to (" + mouseX + ", " + mouseY + ")");
            if (selectedType != SelectedType.NONE&&!isOpen()) {
                highlightX = (int) (event.getX() / 40);
                highlightY = (int) (event.getY() / 40);
                foundHighlightRange();
            } else {
                if(!isOpen()) {
                    highlightX = -1;
                    highlightY = -1;
                    highlightRange = 0;
                }
            }
        });

        bottomBar.getChildren().addAll(sniperButton, wallButton, bomberButton,soldierFactoryButton);

        BorderPane root = new BorderPane();
        root.setCenter(mapView);
        root.setBottom(bottomBar);

        stage.setOnCloseRequest(event -> {
            timeline.stop();
            gameLoop.shutdown();
            RecordManager.saveRecord(gameController.getWave(), Game.getDifficulty());
            WindowsManager.runMenu();
        });

        Scene scene = new Scene(root,1280,720);

        scene.setOnKeyPressed(e -> {
            logger.info("Key "+"'"+e.getCode()+"'"+" pressed");
            highlightX=mouseX;
            highlightY=mouseY;
            if (e.getCode() == KeyCode.DIGIT1 || e.getCode() == KeyCode.NUMPAD1) {
                selectedType = SelectedType.SNIPER;
            } else if (e.getCode() == KeyCode.DIGIT2 || e.getCode() == KeyCode.NUMPAD2) {
                selectedType = SelectedType.WALL;
            } else if (e.getCode() == KeyCode.DIGIT3 || e.getCode() == KeyCode.NUMPAD3) {
                selectedType = SelectedType.BOMBER;
            } else if (e.getCode() == KeyCode.DIGIT4 || e.getCode() == KeyCode.NUMPAD4) {
                selectedType = SelectedType.SOLDIER_FACTORY;
            }
            foundHighlightRange();
        });


        stage.setTitle("Tower Defense");
        stage.setScene(scene);
        stage.show();

        timeline = new Timeline(
                new KeyFrame(Duration.millis(1000/60.0), e -> update())
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    /**
     * Main update loop, called every frame.
     */
    public void update(){
        gc.clearRect(0, 0, 1280, 720);
        mapView.drawMap(highlightX, highlightY);
        DefenseView.drawSpawns(gc,gameController);
        drawMoney(gc,gameController.getGame().getMoney());
        ZombieView.drawZombies(gc, gameController.getZombies());
        SoldierFactoriesView.drawSoldierFactories(gc,gameController.getSoldierFactories());
        DefenseView.drawDefenseUnits(gc,gameController.getDefense());
        ZombieDeathEffectView.drawAll(gc);
        BombView.drawBombs(gc,gameController.getBombs());
        DefenseView.drawRange(gc,highlightX,highlightY,highlightRange);
        gameLoop.tick();
    }

    /**
     * Draws the current amount of money on the screen.
     * @param gc    the GraphicsContext to draw on
     * @param money the amount of money
     */
    public void drawMoney(GraphicsContext gc, int money) {
        gc.setFill(Color.BLACK);
        gc.setFont(new Font("Arial",20));
        gc.fillText("Money: " + money, 1150, 30);
    }

    /**
     * Called when the player wins the game.
     * Shows a victory alert and exits the game.
     */
    public void win(){
        timeline.stop();
        gameLoop.shutdown();
        RecordManager.saveRecord(gameController.getWave(), Game.getDifficulty());
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("VICTORY!!!");
            alert.setHeaderText(null);
            alert.setContentText("YOU WIN!!!");
            alert.showAndWait();
            System.exit(0);
        });
    }

    /**
     * Called when the player loses the game.
     * Shows a defeat alert and exits the game.
     */
    public void lose(){
        timeline.stop();
        gameLoop.shutdown();
        RecordManager.saveRecord(gameController.getWave(), Game.getDifficulty());
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Over");
            alert.setHeaderText(null);
            alert.setContentText("Lose! The base has been destroyed.");
            alert.showAndWait();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void setHighlightRange(int highlightRange) {
        this.highlightRange = highlightRange;
    }
}
