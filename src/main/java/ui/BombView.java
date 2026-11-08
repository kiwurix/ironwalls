package ui;

import attackStrategies.Bomb;
import exceptions.ImgNotFoundException;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

/**
 * Utility class for rendering bombs on the game map.
 */
public class BombView {
    private static final int SQUARE_SIZE = 40;
    private static Image bombImg;


    static {
        try {
            findZombieImages();
        } catch (ImgNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Loads the bomb image
     * @throws ImgNotFoundException if the image cannot be loaded
     */
    private static void findZombieImages() throws ImgNotFoundException {
        bombImg = new Image("ui/img/bomb.png");
        if (bombImg.isError()) throw new ImgNotFoundException("bomb");
    }

    /**
     * Draws all bombs on the canvas.
     * @param gc the GraphicsContext to draw on
     * @param bombs the list of bombs to render
     */
    public static void drawBombs(GraphicsContext gc, List<Bomb> bombs){
        for(Bomb bomb:bombs){
            int x = bomb.getX();
            int y = bomb.getY();

            gc.drawImage(bombImg, x, y,SQUARE_SIZE, SQUARE_SIZE);
        }
    }

}
