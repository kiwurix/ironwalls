package ui;

import exceptions.ImgNotFoundException;
import attack.Zombie;
import defense.*;
import game.GameController;
import interfaces.Damageable;
import interfaces.Upgradable;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Utility class for rendering defensive units, their range, and related visuals.
 */
public class DefenseView {
    private static final int SQUARE_SIZE = 40;
    private static  Image sniperImg ;
    private static  Image bomberImg ;
    private static  Image bomberPlaceImg ;
    private static  Image wallImg ;
    private static  Image soldierImg ;

    static {
        try {
            findImages();
        } catch (ImgNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Loads images for all defense units.
     * @throws ImgNotFoundException if any image fails to load
     */
    private static void findImages() throws ImgNotFoundException {
        sniperImg = new Image("ui/img/sniper.png");
        if(sniperImg.isError())throw new ImgNotFoundException("sniper");

        bomberImg = new Image("ui/img/bomber.png");
        if(bomberImg.isError())throw new ImgNotFoundException("bomber");

        bomberPlaceImg = new Image("ui/img/bomberPlace.png");
        if(bomberPlaceImg.isError())throw new ImgNotFoundException("bomberPlace");

        wallImg = new Image("ui/img/wall.png");
        if(wallImg.isError())throw new ImgNotFoundException("wall");

        soldierImg = new Image("ui/img/soldier.png");
        if(soldierImg.isError())throw new ImgNotFoundException("soldier");
    }

    /**
     * Draws all defensive units with their images, HP, and level.
     * @param gc the GraphicsContext to draw on
     * @param units the list of defensive units
     */
    public static void drawDefenseUnits(GraphicsContext gc, List<PassiveProtection> units) {
        for (PassiveProtection unit : units) {
            int x = unit.getXPosition();
            int y = unit.getYPosition();
            Image img = null;
            if (unit instanceof Sniper) {
                img = sniperImg;
            } else if (unit instanceof Bomber) {
                gc.drawImage(bomberPlaceImg, x, y , SQUARE_SIZE, SQUARE_SIZE);
                img = bomberImg;
            } else if (unit instanceof Wall) {
                img = wallImg;
            } else if(unit instanceof  Soldier){
                img = soldierImg;
            }
            if(unit instanceof ActiveProtection active){
                gc.save();
                gc.translate(x + SQUARE_SIZE / 2, y + SQUARE_SIZE / 2);
                gc.rotate(active.getDegree() + active.getImageOffset());
                gc.drawImage(img, -SQUARE_SIZE / 2, -SQUARE_SIZE / 2, SQUARE_SIZE, SQUARE_SIZE);
                gc.restore();
                if (unit instanceof Sniper sniper ) {
                    Zombie target = sniper.getFirstTarget();
                    if (target != null) {
                        drawBullets(gc,x,y,target,Color.BLACK);
                    }
                }
                if (unit instanceof Soldier soldier ) {
                    Zombie target = soldier.getFirstTarget();
                    if (target != null) {
                        drawBullets(gc,x,y,target,Color.YELLOW);
                    }
                }
            }else {
                gc.drawImage(img, x, y , SQUARE_SIZE, SQUARE_SIZE);
            }
            if(unit instanceof Damageable damageable){
                gc.setFill(Color.BLACK);
                gc.setFont(new Font(10));
                String hpText = damageable.getHp() + " / " + damageable.getMaxHp();
                gc.fillText(hpText, x+4, y - 5);
            }
            if(unit instanceof Upgradable upgradable){
                gc.setFill(Color.BLACK);
                gc.setFont(new Font(10));
                String LvlText = "Lvl " + upgradable.getLvl();
                gc.fillText(LvlText, x+10, y - 14);
            }
        }
    }

    /**
     * Draws the range highlight for placing units.
     * @param gc the GraphicsContext to draw on
     * @param highlightX the X cell
     * @param highlightY the Y cell
     * @param highlightRange the range radius
     */
    public static void drawRange(GraphicsContext gc,int highlightX,int highlightY,int highlightRange){
        if (highlightX == -1 || highlightY == -1||highlightRange<=0) return;
        gc.setFill(Color.LIGHTGREEN);
        gc.setGlobalAlpha(0.3);
        gc.fillOval((highlightX *40 +20 - highlightRange), (highlightY * 40+20 - highlightRange), highlightRange * 2, highlightRange * 2);
        gc.setGlobalAlpha(1.0);
    }

    /**
     * Draws the base HP at the soldiers' spawn point.
     * @param gc the GraphicsContext to draw on
     * @param gameController the game controller
     */
    public static void drawSpawns(GraphicsContext gc, GameController gameController){
        int x = gameController.getStartXSoldiers();
        int y = gameController.getStartYSoldiers();
        gc.setFill(Color.BLACK);
        gc.fillText(gameController.getGame().getBaseHP()+"/"+gameController.getGame().getMaxBaseHP(),x+4,y-5);
    }

    /**
     * Draws a bullet line from the shooter to the zombie.
     * @param gc the GraphicsContext to draw on
     * @param x shooter X position
     * @param y shooter Y position
     * @param target the zombie target
     * @param color the bullet color
     */
    private static void drawBullets(GraphicsContext gc,int x,int y,Zombie target ,Color color){
        gc.setGlobalAlpha(0.3);
        gc.setStroke(color);
        gc.setLineWidth(2);
        gc.strokeLine(x  + SQUARE_SIZE / 2, y  + SQUARE_SIZE / 2,
                target.getXPosition() + SQUARE_SIZE / 2,
                target.getYPosition()  + SQUARE_SIZE / 2
        );
        gc.setGlobalAlpha(1.0);
    }
}
