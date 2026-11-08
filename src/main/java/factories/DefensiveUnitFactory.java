package factories;

import defense.*;
/**
 * Factory for creating different defensive units.
 */
public class DefensiveUnitFactory {
    private static int SQUARE_SIZE = 40;

    /**
     * Creates a Sniper unit.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new Sniper
     */
    public static Sniper createSniper(int x,int y) {
        return new Sniper(x, y,100,10,20*SQUARE_SIZE,2,135);
    }

    /**
     * Creates a Wall unit.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new Wall
     */
    public static Wall createWall(int x,int y) {
        return new Wall(x, y, 100, 20);
    }

    /**
     * Creates a Bomber unit.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new Bomber
     */
    public static Bomber createBomber(int x,int y) {
        return new Bomber(x,y,50,5,5*SQUARE_SIZE,2,2*SQUARE_SIZE,0.5,90);
    }
}

