package defense;

import attackStrategies.SingleTargetAttackStrategy;
import interfaces.AttackStrategy;
import interfaces.Damageable;
import interfaces.Movable;

import java.util.List;

/**
 * Represents a moving Soldier unit on the battlefield.
 */
public class Soldier extends ActiveProtection implements Damageable, Movable {
    private int speed;
    private int hp;
    private int maxHp;
    private int pathProgress = 0;

    /**
     * Constructs a Soldier unit.
     * @param xPosition   the initial X coordinate
     * @param yPosition   the initial Y coordinate
     * @param Damage      initial damage
     * @param Range       attack range
     * @param maxHp       maximum health
     * @param attackDelay delay between attacks
     * @param speed       movement speed
     * @param lvl         initial level
     * @param imageOffset image offset
     */
    public Soldier(int xPosition, int yPosition, int Damage, int Range,int maxHp,double attackDelay, int speed,int lvl,int imageOffset) {
        super(xPosition, yPosition, 0, Damage, Range, attackDelay,imageOffset);
        this.speed = speed;
        this.maxHp=maxHp;
        this.hp=maxHp;
        this.setLvl(lvl);
        setName("Soldier");
    }

    /**
     * Returns the attack strategy (single target attack).
     * @return {@link SingleTargetAttackStrategy}
     */
    @Override
    public AttackStrategy getAttackStrategy() {
        return new SingleTargetAttackStrategy();
    }

    /**
     * Checks if the soldier is dead.
     * @return true if HP is 0 or below
     */
    @Override
    public boolean isDead() {
        return getHp()<=0;
    }

    @Override
    public int getHp() {
        return this.hp;
    }
    @Override
    public void setHp(int hp){
        this.hp=hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    /**
     * Moves the soldier along the road.
     * @param road the list of road points
     */
    @Override
    public void move(List<int[]> road){
        if (pathProgress < road.size()) {
            int[] position = road.get(pathProgress);
            setXPosition(position[1]);
            setYPosition(position[0]);
            pathProgress++;
        }
    }
    @Override
    public int getPathProgress() {
        return pathProgress;
    }

    @Override
    public void setPathProgress(int pathProgress){
        this.pathProgress = pathProgress;
    }

}
