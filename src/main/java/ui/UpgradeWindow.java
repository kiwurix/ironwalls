package ui;

import exceptions.ImgNotFoundException;
import factories.SoldierFactory;
import defense.*;
import game.GameController;
import interfaces.Damageable;
import interfaces.Upgradable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * Manages the upgrade window for units and soldier factories.
 * Allows players to view and upgrade selected units.
 */
public class UpgradeWindow {
    private static final Stage dialog = new Stage();
    private static PassiveProtection unit ;
    private static SoldierFactory factory ;
    private static Upgradable upgradable ;
    private static Button upgradeButton;
    private static Label infoLabel;
    private static GameWindow gameWindow;

    private static Image backgroundImg;

    static {
        try {
            findZombieImages();
        } catch (ImgNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Loads background image for the upgrade window.
     * @throws ImgNotFoundException if the image cannot be loaded
     */
    private static void findZombieImages() throws ImgNotFoundException {
        backgroundImg = new Image("ui/img/infoBackground.png");
        if (backgroundImg.isError()) throw new ImgNotFoundException("upgrade window background");
    }

    private static BackgroundImage bgImage = new BackgroundImage(
            backgroundImg,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.DEFAULT,
            new BackgroundSize(
                    100, 100, true, true, true, true
            )
    );

    /**
     * Opens the upgrade window for a unit or soldier factory at the given coordinates.
     * @param x the x position
     * @param y the y position
     * @param gameController the current game controller
     * @param gameW the game window
     */
    public static void upgradeWindow(int x,int y ,GameController gameController,GameWindow gameW) {
        unit = null;
        factory = null;
        upgradable = null;
        gameWindow = gameW;

        for (PassiveProtection u : gameController.getDefense()) {
            if (u.getXPosition() == x && u.getYPosition() == y) {
                gameWindow.setHighlightRange((u instanceof ActiveProtection active)? active.getRange() : 0);
                unit = u;
                break;
            }
        }

        if(unit==null){
            for (SoldierFactory f : gameController.getSoldierFactories()) {
                if (f.getX() == x && f.getY() == y) {
                    gameWindow.setHighlightRange(0);
                    factory = f;
                    break;
                }
            }
        }

        if(unit==null&&factory==null){return;}

        dialog.setTitle(unit==null?"Soldier base": unit.getName());
        dialog.setAlwaysOnTop(true);

        infoLabel = new Label();
        infoLabel.setStyle("-fx-text-fill: #000000; -fx-font-size: 15px;");
        upgradeButton = new Button();
        upgradeButton.setPrefSize(100,50);

        updateInfo();
        if(upgradable==null){return;}

        upgradeButton.setOnAction(e -> {
            upgradable.upgrade(gameController.getGame());
            updateInfo();
        });

        VBox layout = new VBox(15, infoLabel, upgradeButton);
        layout.setPadding(new Insets(30, 20, 20, 70));
        layout.setBackground(new Background(bgImage));
        Scene scene = new Scene(layout,250,300);
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Updates the displayed unit/factory info in the upgrade window.
     */
    private static void updateInfo() {
        StringBuilder info = new StringBuilder();

        if (unit != null) {
            info.append(unit.getName()).append("\n");
            info.append("Level: ").append(unit.getLvl()).append("\n");

            if (unit instanceof ActiveProtection active) {
                info.append("Damage: ").append(active.getDamage()).append("\n");
                int range = active.getRange();
                info.append("Range: ").append(range/40).append("\n");
                gameWindow.setHighlightRange(range);
                info.append("Attack Delay: ").append(active.getAttackDelay()).append(" s\n");
            }

            if (unit instanceof Bomber bomber) {
                info.append("Bomb Range: ").append(bomber.getBlastRange()/40).append("\n");
                info.append("Bomb Timer: ").append(bomber.getBombTimer()).append(" s\n");
            }

            if (unit instanceof Sniper sniper) {
                info.append("Vision: ").append(sniper.hasVision() ? "Yes" : "No").append("\n");
            }

            if (unit instanceof Damageable damageable) {
                info.append("HP: ").append(damageable.getHp()).append(" / ").append(damageable.getMaxHp()).append("\n");
            }

            if (unit instanceof Upgradable u) {
                info.append("Upgrade Price: ").append(u.getUpgradePrice()).append("\n");
                upgradable = u;
            }

        } else if (factory != null) {
            info.append("Soldier base\n");
            info.append("Level: ").append(factory.getLvl()).append("\n");
            info.append("Upgrade Cost: ").append(factory.getUpgradePrice()).append("\n");
            upgradable = factory;
        }
        infoLabel.setText(info.toString());
        upgradeButton.setText("Upgrade (" + upgradable.getUpgradePrice() + ")");
    }

    /**
     * Checks if the upgrade window is currently open.
     * @return true if the window is open, false otherwise
     */
    public static boolean isOpen() {
        return dialog.isShowing();
    }
}
