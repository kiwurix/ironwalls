package attack;

import attackStrategies.SplashAttackStrategy;
import interfaces.AttackStrategy;
import interfaces.Attacker;

public class RedZombie extends Zombie implements Attacker {
    private int bombRange;
    private int bombDamage;
    public RedZombie(int xPosition, int yPosition, int hp,int lvl, int speed, int bombRange,int bombDamage) {
        super(xPosition, yPosition,hp, lvl, speed, false);
        this.bombRange = bombRange;
        this.bombDamage=bombDamage;
        setName("Fire Zombie");
    }

    public int getBombRange() {
        return bombRange;
    }

    public void setBombRange(int bombRange) {
        this.bombRange = bombRange;
    }

    public int getBombDamage() {
        return bombDamage;
    }

    public void setBombDamage(int bombDamage) {
        this.bombDamage = bombDamage;
    }

    @Override
    public int getDamage() {
        return bombDamage;
    }

    @Override
    public AttackStrategy getAttackStrategy() {
        return new SplashAttackStrategy();
    }
}
