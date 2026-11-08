package attack;

import java.util.List;

import interfaces.Stunnable;
import interfaces.Stunner;

public class BlueZombie extends Zombie implements Stunner {
    private int freezeDuration;
    private int freezeRange;

    public BlueZombie(int xPosition, int yPosition, int hp ,int lvl, int speed,int freezeDuration,int freezeRange) {
        super(xPosition, yPosition, hp,lvl, speed, false);
        this.freezeRange = freezeRange;
        this.freezeDuration = freezeDuration;
        setName("Ice Zombie");
    }

    @Override
    public int getFreezeDuration() {
        return freezeDuration;
    }

    public void setFreezeDuration(int freezeDuration) {
        this.freezeDuration = freezeDuration;
    }

    public int getFreezeRange() {
        return freezeRange;
    }

    public void setFreezeRange(int freezeRange) {
        this.freezeRange = freezeRange;
    }


    @Override
    public void stun(List<? extends Stunnable> units) {
        for (Stunnable target : units) {
            target.stun(freezeDuration);
        }
    }
}
