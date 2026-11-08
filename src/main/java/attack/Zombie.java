package attack;

import game.GameController;
import interfaces.Damageable;
import interfaces.Movable;
import interfaces.ZombieBehavior;

import java.util.List;

public abstract class Zombie  implements Damageable, Movable, ZombieBehavior {
    private String name;
    private int lvl;
    private int hp;
    private int maxHp;
    private int speed;
    private int xPosition;
    private int yPosition;
    private boolean invisibility;
    private int pathProgress=0;
    private boolean left=true;

    public Zombie(int xPosition, int yPosition, int maxHp ,int lvl, int speed, boolean invisibility ) {
        this.speed = speed;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.invisibility = invisibility;
        this.lvl = lvl;
        this.hp=maxHp;
        this.maxHp=maxHp;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getXPosition() {
        return xPosition;
    }

    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isInvisible() {
        return invisibility;
    }

    @Override
    public boolean isDead() {
        return hp <= 0;
    }
    @Override
    public int getHp() {
        return hp;
    }

    @Override
    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getMaxHp() {
        return maxHp;
    }
    @Override
    public void move(List<int[]> road) {
        if (road.size() - pathProgress - 1 < road.size()) {
            int[] position = road.get(road.size() - pathProgress - 1);
            pathProgress++;
            xPosition = position[1];
            yPosition = position[0];
        }
    }

    @Override
    public int getPathProgress() {
        return pathProgress;
    }

    public boolean isLookingLeft() {
        return left;
    }

    public void setLookingLeft(boolean left) {
        this.left = left;
    }

    public void setPathProgress(int pathProgress) {
        this.pathProgress = pathProgress;
    }

    public void update(GameController gameController){}


}
