package finalBossDecorators;

import Effects.ZombieDeathEffect;
import game.GameController;
import game.Maps;
import interfaces.ZombieBehavior;
import ui.ZombieDeathEffectView;

import java.util.List;

/**
 * Adds periodic explosions to the final boss behavior.
 * Every 3 seconds, triggers explosions along the path to damage defenses.
 */
public class ExplosionsBehavior extends FinalBossBehavior {
    private final int range = 40;
    private final int damage = 10;

    /** Counter to control explosion timing. */
    private int tick=0;


    /**
     * Constructs an ExplosionsBehavior decorator.
     * @param behavior the base boss behavior to decorate
     */
    public ExplosionsBehavior(ZombieBehavior behavior) {
        super(behavior);
    }

    /**
     * Updates the boss behavior and triggers explosions every 180 ticks.
     * @param gameController the game controller
     */
    @Override
    public void update(GameController gameController) {
        super.update(gameController);
        tick++;
        if(tick == 180){
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
                gameController.redZombieExplosion(x,y,range,damage);
                ZombieDeathEffectView.addEffect(new ZombieDeathEffect(x,y,range, ZombieDeathEffect.EffectType.RED_EXPLOSION));
            }
            tick = 0 ;
        }
    }


}
