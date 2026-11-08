package factories;
import attack.*;
import game.Game;
import game.Maps;


/**
 * Factory for creating different types of zombies and managing zombie waves.
 */
public class ZombieFactory {
    private int wave=0;

    private int difficult = Game.getDifficulty();

    private int greenZombieHP=switch (difficult){
        case 1->6;
        case 2->12;
        case 3 ->18;
        default -> 10;
    };

    private int blueZombieHP=switch (difficult){
        case 1->5;
        case 2->10;
        case 3 ->15;
        default -> 10;
    };

    private int blackZombieHP=switch (difficult){
        case 1->3;
        case 2->6;
        case 3 ->9;
        default -> 10;
    };

    private int redZombieHP=switch (difficult){
        case 1->4;
        case 2->8;
        case 3 ->12;
        default -> 10;
    };

    /**
     * Returns the current wave number.
     * @return current wave
     */
    public int getWave() {
        return wave;
    }

    /**
     * Finds the path progress based on zombie's current position.
     * @param zombie the zombie to find progress for
     * @return path progress
     */
    public int foundPathProgress(Zombie zombie){
        int x = zombie.getXPosition();
        int y = zombie.getYPosition();

        int progress = 0;

        for(int[] point: Maps.getRoad()){
            if(point[1]==x&&point[0]==y){
                break;
            }
            progress++;
        }
        return Maps.getRoad().size() - progress -1;
    }

    /**
     * Creates a GreenZombie.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new GreenZombie
     */
    public GreenZombie createGreenZombie(int x,int y){
        GreenZombie greenZombie = new GreenZombie(x,y,greenZombieHP*wave,wave,1);
        greenZombie.setPathProgress(foundPathProgress(greenZombie));
        return greenZombie;
    }

    /**
     * Creates a BlackZombie.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new BlackZombie
     */
    public BlackZombie createBlackZombie(int x,int y){
        BlackZombie blackZombie =  new BlackZombie(x,y,blackZombieHP*wave,wave,2);
        blackZombie.setPathProgress(foundPathProgress(blackZombie));
        return blackZombie;
    }

    /**
     * Creates a BlueZombie.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new BlueZombie
     */
    public BlueZombie createBlueZombie(int x,int y){
        int freezeDuration;
        if (wave <=3){
            freezeDuration=1;
        }else if(wave<=7){
            freezeDuration=2;
        }else{
            freezeDuration=3;
        }
        BlueZombie blueZombie =  new BlueZombie(x,y,blueZombieHP*wave,wave,1,freezeDuration,5*40);
        blueZombie.setPathProgress(foundPathProgress(blueZombie));
        return blueZombie;
    }

    /**
     * Creates a RedZombie.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new RedZombie
     */
    public RedZombie createRedZombie(int x,int y){
        int bombDamage;
        if (wave <=3){
            bombDamage=3;
        }else if(wave<=7){
            bombDamage=5;
        }else{
            bombDamage=8;
        }
        RedZombie redZombie = new RedZombie(x,y,redZombieHP*wave,wave,2,2*40,bombDamage);
        redZombie.setPathProgress(foundPathProgress(redZombie));
        return redZombie;
    }

    /**
     * Advances to the next wave.
     */
    public void nextWave() {
        wave++;
    }
}
