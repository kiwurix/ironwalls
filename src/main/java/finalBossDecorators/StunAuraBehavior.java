package finalBossDecorators;

import Effects.ZombieDeathEffect;
import game.GameController;
import interfaces.Stunnable;
import interfaces.Stunner;
import interfaces.ZombieBehavior;
import ui.ZombieDeathEffectView;

import java.util.List;


/**
 * Final boss behavior that stuns nearby units with a freeze aura effect.
 */
public class StunAuraBehavior extends FinalBossBehavior implements Stunner {
    private final int freezeDuration = 5;
    private final int freezeRange = 3*40;


    /**
     * Constructs a StunAuraBehavior decorator.
     * @param behavior the base behavior to decorate
     */
    public StunAuraBehavior(ZombieBehavior behavior) {
        super(behavior);
    }

    /**
     * Updates the boss behavior by stunning nearby units.
     * @param gameController the game controller
     */
    @Override
    public void update(GameController gameController) {
        super.update(gameController);
        int x = getXPosition();
        int y = getYPosition();
        gameController.blueZombieStun(x,y,freezeRange,freezeDuration);
        ZombieDeathEffectView.addEffect(new ZombieDeathEffect(x,y,freezeRange, ZombieDeathEffect.EffectType.BLUE_FREEZE));
    }

    @Override
    public void stun(List<? extends Stunnable> units) {
        for (Stunnable target : units) {
            target.stun(freezeDuration);
        }
    }

    /**
     * @return freeze duration in seconds
     */
    @Override
    public int getFreezeDuration() {
        return freezeDuration;
    }
}
