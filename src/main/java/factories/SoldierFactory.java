package factories;

import defense.Soldier;
import game.Game;
import interfaces.Upgradable;

import java.util.List;

/**
 * Factory for creating and upgrading Soldiers.
 */
public class SoldierFactory implements Upgradable {
    private int lvl =1;
    private static int price = 100 ;
    int x;
    int y;

    public SoldierFactory(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void upgrade(Game game) {
        if((game.getMoney()>=getPrice())&&getLvl()<5){
            game.setMoney(game.getMoney()-getPrice());
            this.lvl = getLvl()+1;
        }
    }

    private static class Stats {
        int maxHp, damage, range, speed;

        Stats(int hp, int damage, int range, int speed) {
            this.maxHp = hp;
            this.damage = damage;
            this.range = range;
            this.speed = speed;
        }
    }

    private List<Stats> levels = List.of(
            new Stats(20, 5, 5, 1),
            new Stats(25, 7, 3, 1),
            new Stats(30, 10, 4, 1),
            new Stats(40, 15, 4, 1),
            new Stats(50, 29, 5, 1)
    );

    /**
     * Creates a Soldier based on current factory level.
     * @param x x coordinate
     * @param y y coordinate
     * @return a new Soldier
     */
    public Soldier createSoldier(int x, int y) {
        Stats current = levels.get(lvl-1);
        return new Soldier(x,y,current.damage,current.range*40,current.maxHp,0.1, current.speed,lvl,-90);
    }

    @Override
    public int getLvl() {
        return lvl;
    }

    @Override
    public int getUpgradePrice() {
        return 100;
    }

    public static int getPrice() {
        return price;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
