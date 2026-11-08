package defense;

import attackStrategies.DefaultBombPlacingStrategy;
import attackStrategies.SplashAttackStrategy;
import game.Game;
import interfaces.AttackStrategy;
import interfaces.BombPlacingStrategy;
import interfaces.Upgradable;


/**
 * Represents a Bomber defensive unit that places bombs
 * dealing splash damage to multiple enemies.
 */
public class Bomber extends ActiveProtection implements Upgradable {
    private int blastRange;
    private double bombTimer;

    /**
     * Creates a new Bomber unit at the specified position.
     * @param xPosition   the X coordinate
     * @param yPosition   the Y coordinate
     * @param price       the purchase price
     * @param startDamage the base damage
     * @param startRange  the base range
     * @param attackDelay delay between attacks
     * @param blastRange  range of bomb explosion
     * @param bombTimer   time before bomb explodes
     * @param imageOffset offset for image
     */
    public Bomber(int xPosition, int yPosition,int price, int startDamage, int startRange,double attackDelay, int blastRange,double bombTimer ,int imageOffset) {
        super(xPosition, yPosition, price, startDamage, startRange,attackDelay,imageOffset);
        this.blastRange = blastRange;
        this.bombTimer = bombTimer;
        setName("Bomber");
    }

    /**
     * Returns the bomb blast range.
     * @return blast range
     */
    public int getBlastRange() {
        return blastRange;
    }

    /**
     * Sets a new blast range for bombs.
     * @param blastRange new blast range
     */
    public void setBlastRange(int blastRange) {
        this.blastRange = blastRange;
    }

    /**
     * Returns the attack strategy (splash attack).
     * @return {@link SplashAttackStrategy}
     */
    @Override
    public AttackStrategy getAttackStrategy() {
        return new SplashAttackStrategy();
    }

    /**
     * Upgrades the bomber unit if possible, increasing stats.
     * @param game the game instance to deduct money from
     */
    @Override
    public void upgrade(Game game) {
        if((game.getMoney()<getPrice())||getLvl()==5){
            System.out.println("Not enough money");
        }else{
            game.setMoney(game.getMoney()-getPrice());
            setLvl(getLvl()+1);
            setDamage(getStartDamage()+getStartDamage()*(getLvl()-1)/4);
            setRange(getStartRange()+getStartRange()*(getLvl()-1)/4);
            setBlastRange((int) (getBlastRange()*1.25));
        }
    }

    @Override
    public int getUpgradePrice() {
        return 50;
    }

    /**
     * Returns the bomb placing strategy.
     * @return {@link DefaultBombPlacingStrategy}
     */
    public BombPlacingStrategy getBombPlacingStrategy(){
        return new DefaultBombPlacingStrategy();
    }

    public double getBombTimer() {
        return bombTimer;
    }
}
