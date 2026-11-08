package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for managing game maps and paths.
 */
public class Maps {

    /** Size of the map */
        private static final int size = 32;
    /** 2D map grid */
        private static final int[][] map = new int[size][size];
    /** List of points forming the road path. */
        private static final List<int[]> mapRoad = new ArrayList<>();
    /** Spawn coordinates for soldiers. */
        private static int xSoldiersSpawn;
        private static int ySoldiersSpawn;
    /** Spawn coordinates for zombies. */
        private static int xZombiesSpawn;
        private static int yZombiesSpawn;
    /** Selected map ID. */
        private static int selectedMap = 1;

    /**
     * Creates the first map layout.
     */
        private static void createMap1 () {
            for (int i = 0; i < 16; i++) {
                for (int j = 0; j < 32; j++) {
                    map[i][j] = 0;
                }
            }

            mapRoad.clear();

            for (int x = 0; x < 32; x++) {
                map[9][x] = 1;
                mapRoad.add(new int[]{9, x});
            }
            map[9][0] = 3;
            map[9][31] = 2;

            xSoldiersSpawn = 0;
            ySoldiersSpawn = 9;
            xZombiesSpawn = 31;
            yZombiesSpawn = 9;

        }

    /**
     * Creates the second map layout.
     */
        private static void createMap2() {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 32; j++) {
                map[i][j] = 0;
            }
        }

        mapRoad.clear();

        int x = 0;
        int y = 2;

        // right
        for (; x < 30; x++) {
            map[y][x] = 1;
            mapRoad.add(new int[]{y, x});
        }

        // down
        for (; y < 6; y++) {
            map[y][x] = 1;
            mapRoad.add(new int[]{y, x});
        }

        // left
        for (; x > 2; x--) {
            map[y][x] = 1;
            mapRoad.add(new int[]{y, x});
        }

        // down
        for (; y < 10; y++) {
            map[y][x] = 1;
            mapRoad.add(new int[]{y, x});
        }

        // right
        for (; x < 31; x++) {
            map[y][x] = 1;
            mapRoad.add(new int[]{y, x});
        }

        map[2][0] = 3;
        map[10][30] = 2;

        xSoldiersSpawn = 0;
        ySoldiersSpawn = 2;
        xZombiesSpawn = 30;
        yZombiesSpawn = 10;
    }




    /**
     * @return 2D array representing the map
     */
    public static int[][] getMap(){
            return map;
        }

    /**
     * Returns a smoothed road path.
     * @return interpolated list of road points
     */
    public static List<int[]> getRoad() {
            return roadInterpolation(mapRoad,20);
    }

    /**
     * Returns the X coordinate for soldier spawn.
     * @return x-coordinate in pixels
     */
    public static int getXSoldiersSpawn() {
            return xSoldiersSpawn*40;
    }

    /**
     * Returns the Y coordinate for soldier spawn.
     * @return y-coordinate in pixels
     */
    public static int getYSoldiersSpawn() {
        return ySoldiersSpawn*40;
    }

    /**
     * Returns the X coordinate for zombie spawn.
     * @return x-coordinate in pixels
     */
    public static int getXZombiesSpawn() {
        return xZombiesSpawn *40;
    }

    /**
     * Returns the Y coordinate for zombie spawn.
     * @return y-coordinate in pixels
     */
    public static int getYZombiesSpawn() {
        return yZombiesSpawn *40;
    }

    /**
     * Smoothes the given road path by interpolating points.
     * @param road original road points
     * @param interpolationStep number of interpolation steps between points
     * @return list of interpolated points
     */
    public static List<int[]> roadInterpolation(List <int[]> road,int interpolationStep) {
        List<int[]> smoothPath = new ArrayList<>();

        for (int i = 0; i < road.size() - 1; i++) {
            int[] current = road.get(i);
            int[] next = road.get(i + 1);

            double startX = current[1] * 40 ;
            double startY = current[0] * 40 ;
            double endX = next[1] * 40 ;
            double endY = next[0] * 40 ;

            for (int step = 0; step < interpolationStep; step++) {
                double t = (double) step / interpolationStep;
                double additionalX = startX + t * (endX - startX);
                double additionalY = startY + t * (endY - startY);
                smoothPath.add(new int[]{(int)additionalY,(int)additionalX});
            }
        }

        int[] last = road.getLast();
        int lastX = last[1] * 40 ;
        int lastY = last[0] * 40 ;
        smoothPath.add(new int[]{lastY,lastX});
        return smoothPath;
    }


    /**
     * Sets the selected map ID and generates the map.
     * @param id map ID
     */
    public static void setSelectedMap(int id) {
        selectedMap = id;
        switch (selectedMap){
            case 1 -> createMap1();
            case 2 ->createMap2();
            default -> createMap1();
        };
    }

}
