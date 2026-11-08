package finalBossDecorators;

import game.GameController;
import interfaces.ZombieBehavior;

/**
 * Abstract decorator for final boss behaviors.
 * Allows extending boss functionality without modifying the base behavior.
 */
public abstract class FinalBossBehavior implements ZombieBehavior {
    protected ZombieBehavior decoratedBehavior;

    /**
     * Constructs a FinalBossBehavior decorator.
     * @param decoratedBehavior the base behavior to decorate
     */
    public FinalBossBehavior(ZombieBehavior decoratedBehavior) {
        this.decoratedBehavior = decoratedBehavior;
    }

    /**
     * Updates the decorated behavior.
     * @param gameController the game controller
     */
    @Override
    public void update(GameController gameController){
        decoratedBehavior.update(gameController);
    }

    @Override
    public int getXPosition() {
        return decoratedBehavior.getXPosition();
    }

    @Override
    public int getYPosition() {
        return decoratedBehavior.getYPosition();
    }

    @Override
    public int getPathProgress() {
        return decoratedBehavior.getPathProgress();
    }
}
