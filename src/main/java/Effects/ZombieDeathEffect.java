package Effects;

import attack.BlueZombie;
import attack.RedZombie;
import attack.Zombie;


/**
 * Represents a visual effect after a zombie's death.
 */
public class ZombieDeathEffect {


    /**
     * Available types of death effects.
     */
    public enum EffectType {
        RED_EXPLOSION, BLUE_FREEZE
    }

    private final int x;
    private final int y;
    private final EffectType type;
    private double timeLeft;
    private int range;

    /**
     * Creates an effect based on the type of zombie.
     * @param zombie the dead zombie
     */
    public ZombieDeathEffect(Zombie zombie) {
        this.x = zombie.getXPosition();
        this.y = zombie.getYPosition();

        if (zombie instanceof RedZombie red) {
            this.type = EffectType.RED_EXPLOSION;
            this.timeLeft = 0.2;
            this.range = red.getBombRange();
        } else if (zombie instanceof BlueZombie blue) {
            this.type = EffectType.BLUE_FREEZE;
            this.timeLeft = 0.2;
            this.range = blue.getFreezeRange();
        } else {
            this.type = null;
            this.timeLeft = 0;
            this.range=0;
        }
    }

    /**
     * Creates a custom effect with specified parameters.
     * @param x x coordinate
     * @param y y coordinate
     * @param range effect range
     * @param type effect type
     */
    public ZombieDeathEffect(int x,int y,int range ,EffectType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        timeLeft = (type == EffectType.BLUE_FREEZE) ?  1.0/59.9 : 0.2;
        this.range = range;
    }


    /**
     * Updates the remaining time of the effect.
     * @param delta time passed since last update
     */
    public void update(double delta) {
        timeLeft -= delta;
    }

    public boolean isFinished() {
        return timeLeft <= 0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public EffectType getType() {
        return type;
    }

    public int getRange() {
        return range;
    }
}
