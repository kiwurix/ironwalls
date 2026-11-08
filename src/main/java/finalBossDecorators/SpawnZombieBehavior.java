package finalBossDecorators;

import game.GameController;
import game.Maps;
import interfaces.ZombieBehavior;

import java.util.List;

/**
 * Final boss behavior that periodically spawns additional zombies along the path.
 */
public class SpawnZombieBehavior extends FinalBossBehavior{
    /** Counter for update ticks. */
    private int tick = 0 ;

    /** Number of spawn events that have occurred. */
    private int number=0;

    /**
     * Constructs a SpawnZombieBehavior decorator.
     * @param decoratedBehavior the base behavior to decorate
     */
    public SpawnZombieBehavior(ZombieBehavior decoratedBehavior) {
        super(decoratedBehavior);
    }

    /**
     * Updates the boss behavior and spawns zombies periodically.
     * @param gameController the game controller
     */
    @Override
    public void update(GameController gameController){
        super.update(gameController);
        tick++;
        if(tick == 180&&number<5){
            number++;
            int x;
            int y;
            List<int []> road = Maps.getRoad();
            for(int i = 1 ;i<4;i++){
                if(getPathProgress()+i*30>= road.size()){
                    break;
                }
                int[] point = road.get(road.size()-getPathProgress()-1-i*30);
                x = point[1];
                y=point[0];
                gameController.spawnZombies(x,y,11,1);
            }
            tick = 0 ;
        }
    }
}
