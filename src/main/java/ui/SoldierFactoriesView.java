package ui;

import exceptions.ImgNotFoundException;
import factories.SoldierFactory;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Utility class for rendering Soldier Factories on the map.
 */
public class SoldierFactoriesView {
    private static final int SQUARE_SIZE = 40;
    private static Image soldierFactoryImg;

    static {
        try {
            findZombieImages();
        } catch (ImgNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Loads the image for soldier factories.
     * @throws ImgNotFoundException if the image is not found
     */
    private static void findZombieImages() throws ImgNotFoundException {
        soldierFactoryImg = new Image("ui/img/soldierFactory.png");
        if (soldierFactoryImg.isError()) throw new ImgNotFoundException("soldierFactory");
    }

    /**
     * Draws all soldier factories with their level displayed.
     * @param gc the GraphicsContext to draw on
     * @param soldierFactories list of soldier factories
     */
    public static void drawSoldierFactories(GraphicsContext gc, List<SoldierFactory> soldierFactories){
        for(SoldierFactory soldierFactory :soldierFactories){
            int x = soldierFactory.getX();
            int y = soldierFactory.getY();
            gc.drawImage(soldierFactoryImg, x, y , SQUARE_SIZE, SQUARE_SIZE);
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(10));
            String LvlText = "Lvl " + soldierFactory.getLvl();
            gc.fillText(LvlText, x+10, y - 14);
        }
    }
}
