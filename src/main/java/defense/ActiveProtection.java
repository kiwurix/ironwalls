package defense;

import attack.Zombie;
import interfaces.AttackStrategy;
import interfaces.Attacker;
import interfaces.Damageable;
import interfaces.Stunnable;

import java.util.List;
import java.util.logging.Logger;

/**
 * Abstract class representing an active defensive unit that can attack and be stunned.
 * It extends {@link PassiveProtection} and implements {@link Attacker} and {@link Stunnable}.
 */
public abstract class ActiveProtection extends PassiveProtection implements Attacker, Stunnable {
    private static final Logger logger = Logger.getLogger(ActiveProtection.class.getName());
    private int damage;
    private int range;
    private int startDamage;
    private int startRange;
    private double stunLeft=0;
    private double degree = 0;
    private int imageOffset=0;
    private double attackDelay;
    private Zombie firstTarget = null;
    private double cooldown = 0;

    /**
     * Constructs an ActiveProtection unit.
     * @param xPosition   the x coordinate
     * @param yPosition   the y coordinate
     * @param price       the price of the unit
     * @param startDamage the initial damage
     * @param startRange  the initial attack range
     * @param attackDelay the time between attacks
     * @param imageOffset the offset for image
     */
    public ActiveProtection(int xPosition, int yPosition, int price, int startDamage, int startRange,double attackDelay,int imageOffset) {
        super(xPosition, yPosition,price);
        this.startDamage = startDamage;
        this.damage = startDamage;
        this.range = startRange;
        this.startRange = startRange;
        this.imageOffset = imageOffset;
        this.attackDelay = attackDelay;
    }
    @Override
    public int getDamage() {
        return damage;
    }

    public int getRange() {
        return range;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getStartDamage() {
        return startDamage;
    }

    public int getStartRange() {
        return startRange;
    }

    /**
     * Returns the attack strategy used by this unit.
     * @return the {@link AttackStrategy}
     */
    @Override
    public abstract AttackStrategy getAttackStrategy();
    @Override
    public void stun(double duration){
        this.stunLeft=duration;{
            logger.info(getName() + " stunned for " + duration + " seconds");
        }
    }
    @Override
    public boolean isStunned(){
        if(stunLeft<=0){
            return false;
        }else{return true;}
    }
    @Override
    public void reduceStun(double delta){
        if(this.stunLeft>0){this.stunLeft -=delta;if(stunLeft<0){stunLeft=0;}}
    }

    /**
     * Performs an attack on the given list of targets.
     * @param targets the list of {@link Damageable} units to attack
     */
    @Override
    public void attack(List<? extends Damageable> targets) {
        getAttackStrategy().attack(targets, getDamage());
        if(targets.isEmpty()){
            firstTarget=null;
            logger.info(getName() + " attacked but no targets in range");
        }else{
            firstTarget = (Zombie) targets.getFirst();
        }
        if(this instanceof Sniper){
            logger.info(getName() + " sniped a Zombie for " + getDamage() + " damage. Target HP: " + targets.getFirst().getHp());
            return ;
        }
        for (Damageable target : targets) {
            logger.info(getName() + " attacked a Zombie for " + getDamage() + " damage. Target HP: " + target.getHp());
        }
    }
    public double getDegree() {
        return degree;
    }

    public void setDegree(double degree) {
        this.degree = degree;
    }

    public int getImageOffset() {
        return imageOffset;
    }

    public Zombie getFirstTarget() {
        return firstTarget;
    }

    /**
     * Checks if the unit is ready to attack.
     * @return true if ready, false otherwise
     */
    public boolean canAttack() {
        if(cooldown>0||isStunned()){firstTarget=null;}
        return (cooldown <= 0&&!(isStunned()));

    }

    /**
     * Updates the cooldown timer by decreasing it.
     * @param delta time passed
     */
    public void updateCooldown(double delta) {
        cooldown -= delta;
        if (cooldown < 0) cooldown = 0;
    }

    public void resetCooldown() {
        cooldown = attackDelay;
    }

    public double getAttackDelay() {
        return attackDelay;
    }
}
