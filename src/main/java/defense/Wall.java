package defense;
import game.Game;
import interfaces.Damageable;
import interfaces.Upgradable;


/**
 * Represents a defensive wall that blocks zombies.
 * Can take damage and be upgraded to increase durability.
 */
public class Wall extends PassiveProtection implements Damageable, Upgradable {
    /** Current HP of the wall. */
    private int hp;

    /** Maximum HP of the wall. */
    private int maxHp;

    /**
     * Creates a new Wall at the specified position with starting HP and price.
     * @param xPosition x coordinate
     * @param yPosition y coordinate
     * @param startHp initial health points
     * @param price price to place the wall
     */
    public Wall(int xPosition, int yPosition, int startHp, int price) {
        super(xPosition, yPosition, price);
        this.maxHp=startHp;
        hp = maxHp;
        setName("Wall");
    }

    @Override
    public String toString() {
        return "Wall{"+getName()+" "+getLvl()+" "+getHp()+" "+getPrice()+"}";
    }

    /**
     * Checks if the wall is destroyed.
     * @return true if HP is zero or below, false otherwise
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
     * Upgrades the wall, increasing HP if enough money.
     * @param game current game instance
     */
    @Override
    public void upgrade(Game game) {
        if((game.getMoney()>=getPrice())&&(getLvl()<5)){
            game.setMoney(game.getMoney()-getPrice());
            setLvl(getLvl()+1);
            setHp(getMaxHp()+getMaxHp()*(getLvl()-1)/4);
            setMaxHp(getMaxHp()+getMaxHp()*(getLvl()-1)/4);
        }
    }

    @Override
    public int getUpgradePrice() {
        return 30;
    }
}
