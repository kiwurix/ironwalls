package ui;

import exceptions.ImgNotFoundException;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import game.Maps;

/**
 * A Canvas that renders the game map.
 * Draws terrain, spawn points, and highlights selected tiles.
 */
public class MapView extends Canvas {
    private static Image roadImg;
    private static final int SQUARE_SIZE = 40;

    /**
     * Constructs a new MapView and draws the initial map.
     */
    public MapView() {
        super(1280, 640);
        drawMap(-1,-1);
    }

    static {
        try {
            findZombieImages();
        } catch (ImgNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Loads road images for the map.
     * @throws ImgNotFoundException if the image cannot be loaded
     */
    private static void findZombieImages() throws ImgNotFoundException {
        roadImg = new Image("ui/img/road.png");
        if (roadImg.isError()) throw new ImgNotFoundException("road");
    }

    /**
     * Draws the entire map including the highlight if present.
     * @param highlightX the x tile to highlight
     * @param highlightY the y tile to highlight
     */
    public void drawMap(int highlightX, int highlightY) {
        GraphicsContext gc = getGraphicsContext2D();
        int[][] map = Maps.getMap();

        for (int y = 0; y < map.length; y++) {
            for (int x = 0; x < map[y].length; x++) {
                switch (map[y][x]) {
                    case 0 -> gc.setFill(Color.LIGHTGRAY);
                    case 1 -> gc.drawImage(roadImg, x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                    case 2 -> gc.setFill(Color.RED);
                    case 3 -> gc.setFill(Color.BLUE);
                    case 4 -> gc.setFill(Color.DARKGREEN);
                }
                if(map[y][x]!=1) {
                    gc.fillRect(x * SQUARE_SIZE, y * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
                }
            }
        }
        if (highlightX >= 0 && highlightY >= 0) {
            gc.setStroke(Color.CYAN);
            gc.setLineWidth(2);
            gc.strokeRect(highlightX * 40, highlightY * 40, 40, 40);
        }
    }

    /**
     * Returns the GraphicsContext for custom drawing.
     * @return the GraphicsContext2D object
     */
    public GraphicsContext getGraphicsContext() {
        return getGraphicsContext2D();
    }

}
