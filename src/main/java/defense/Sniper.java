package defense;

import attackStrategies.SingleTargetAttackStrategy;
import game.Game;
import interfaces.AttackStrategy;
import interfaces.Upgradable;

/**
 * Represents a Sniper defensive unit with long-range single-target attacks.
 * Can be upgraded to improve damage, range, and unlock vision ability.
 */
public class Sniper extends ActiveProtection implements Upgradable {
    private boolean vision ;

    /**
     * Creates a new Sniper unit at the specified position.
     * @param xPosition     the X coordinate
     * @param yPosition     the Y coordinate
     * @param price         the purchase price
     * @param startDamage   the base damage
     * @param startRange    the base range
     * @param attackDelay   delay between attacks
     * @param imageOffset   offset for image
     */
    public Sniper(int xPosition, int yPosition,int price, int startDamage, int startRange,double attackDelay ,int imageOffset) {
        super(xPosition, yPosition, price, startDamage, startRange,attackDelay,imageOffset);
        vision=false;
        setName("Sniper");
    }
    @Override
    public AttackStrategy getAttackStrategy() {
        return new SingleTargetAttackStrategy();
    }

    public boolean hasVision() {
        return vision;
    }


    /**
     * Upgrades the sniper if enough money is available.
     * Improves damage, range, and grants vision at level 3.
     * @param game the game instance to check and reduce money
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
            if(getLvl()>=3){
                vision = true;
            }
        }
    }

    /**
     * Returns the cost to upgrade the sniper.
     * @return upgrade price
     */
    @Override
    public int getUpgradePrice() {
        return 60;
    }

}
