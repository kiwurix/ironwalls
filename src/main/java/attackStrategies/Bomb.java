package attackStrategies;

import interfaces.AttackStrategy;
import interfaces.Attacker;


/**
 * Represents a bomb placed on the field that explodes after a countdown.
 */
public class Bomb implements Attacker {
    private int x;
    private int y;
    private double timeLeft;
    private int range;
    private int damage;
    AttackStrategy attackStrategy;

    /**
     * Constructs a Bomb with the given parameters.
     * @param x              X position
     * @param y              Y position
     * @param timeLeft       time before explosion (seconds)
     * @param range          explosion range (pixels)
     * @param damage          bomb damage
     * @param attackStrategy strategy to attack nearby enemies
     */
    public Bomb(int x, int y, double timeLeft, int range, int damage,AttackStrategy attackStrategy) {
        this.x = x;
        this.y = y;
        this.timeLeft = timeLeft;
        this.range = range;
        this.damage = damage;
        this.attackStrategy = attackStrategy;
    }

    /**
     * Updates the bomb's countdown timer.
     * @param delta time passed since last update (seconds)
     */
    public void update(double delta){
        this.timeLeft-=delta;
        if(timeLeft<0){timeLeft=0;}
    }

    /**
     * Checks if the bomb has exploded.
     * @return true if timeLeft is 0 or less
     */
    public boolean isExploded(){
        return timeLeft<=0;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getTimeLeft() {
        return timeLeft;
    }

    public int getRange() {
        return range;
    }

    public int getDamage() {
        return damage;
    }

    /**
     * Returns the attack strategy used on explosion.
     * @return attack strategy
     */
    @Override
    public AttackStrategy getAttackStrategy() {
        return attackStrategy;
    }
}
