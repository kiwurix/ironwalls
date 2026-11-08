package ui;

import exceptions.ImgNotFoundException;
import attack.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Responsible for displaying zombies on the canvas.
 * Loads zombie images and draws each zombie with its health bar.
 */
public class ZombieView {
    private static final int SQUARE_SIZE = 40;
    private static Image greenZombie ;
    private static Image blueZombie;
    private static Image redZombie ;
    private static Image blackZombie ;
    private static Image bossZombie ;



    static {
        try {
            findZombieImages();
        } catch (ImgNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Loads all zombie images from resources.
     * @throws ImgNotFoundException if any image fails to load
     */
    private static void findZombieImages() throws ImgNotFoundException {
        greenZombie = new Image("ui/img/greenZombie.png");
        if (greenZombie.isError()) throw new ImgNotFoundException("greenZombie");

        blueZombie = new Image("ui/img/blueZombie.png");
        if (blueZombie.isError()) throw new ImgNotFoundException("blueZombie");

        redZombie = new Image("ui/img/redZombie.png");
        if (redZombie.isError()) throw new ImgNotFoundException("redZombie");

        blackZombie = new Image("ui/img/blackZombie.png");
        if (blackZombie.isError()) throw new ImgNotFoundException("blackZombie");

        bossZombie = new Image("ui/img/bossZombie.png");
        if (bossZombie.isError()) throw new ImgNotFoundException("bossZombie");
    }

    /**
     * Draws all zombies on the canvas with their health displayed.
     * @param gc the GraphicsContext to draw on
     * @param zombies the list of zombies to render
     */
    public static void drawZombies(GraphicsContext gc, List<Zombie> zombies) {
        for (Zombie z : zombies) {
            int x = z.getXPosition();
            int y = z.getYPosition();

            Image image = switch(z){
                case BossZombie boss -> bossZombie;
                case GreenZombie green -> greenZombie;
                case BlueZombie blue ->  blueZombie;
                case RedZombie red -> redZombie;
                case BlackZombie black -> blackZombie;
                default -> greenZombie;
            };

            if (z.isLookingLeft()) {
                gc.drawImage(image, x, y, SQUARE_SIZE, SQUARE_SIZE);
            } else {
                gc.save();
                gc.translate(x + SQUARE_SIZE, y);
                gc.scale(-1, 1);
                gc.drawImage(image, 0, 0, SQUARE_SIZE, SQUARE_SIZE);
                gc.restore();
            }

            gc.setFill(Color.BLACK);
            gc.setFont(new Font(10));
            String hpText = z.getHp() + " / " + z.getMaxHp();
            gc.fillText(hpText, x+12, y - 5);
        }
    }

}
