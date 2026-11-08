package defenseTest;

import defense.Bomber;
import defense.Sniper;
import defense.Wall;
import factories.DefensiveUnitFactory;
import factories.SoldierFactory;
import game.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeTest {

    private static Game game;


    @BeforeEach
    void setUpGame() {
        game = new Game();
        game.setMoney(100000);
    }

    @Test
    void sniperUpgrade() {
        Sniper sniper = new Sniper(0,0,5,5,5,5,90);
        int cost = sniper.getPrice();
        int startDamage = sniper.getDamage();
        int startRange  = sniper.getRange();
        int beforeMoney = game.getMoney();

        sniper.upgrade(game);

        assertEquals(2, sniper.getLvl());
        assertEquals(beforeMoney - cost, game.getMoney());
        assertTrue(sniper.getDamage() > startDamage);
        assertTrue(sniper.getRange()  > startRange);

        sniper.upgrade(game);
        sniper.upgrade(game);
        assertTrue(sniper.hasVision());
    }

    @Test
    void bomberUpgrade() {
        Bomber bomber = DefensiveUnitFactory.createBomber(0,0);
        int cost = bomber.getPrice();
        int startBlast = bomber.getBlastRange();
        int beforeMoney = game.getMoney();

        bomber.upgrade(game);

        assertEquals(2, bomber.getLvl());
        assertEquals(beforeMoney - cost, game.getMoney());
        assertTrue(bomber.getBlastRange() > startBlast);
    }

    @Test
    void wallUpgrade() {
        Wall wall = DefensiveUnitFactory.createWall(0,0);
        int cost = wall.getPrice();
        int startMaxHp = wall.getMaxHp();
        int beforeMoney = game.getMoney();

        wall.upgrade(game);

        assertEquals(2, wall.getLvl());
        assertEquals(beforeMoney - cost, game.getMoney());
        assertTrue(wall.getMaxHp() > startMaxHp);
        assertEquals(wall.getMaxHp(), wall.getHp());
    }

    @Test
    void upgrade_whenNotEnoughMoney() {
        Sniper sniper = DefensiveUnitFactory.createSniper(0,0);
        game.setMoney(0);
        int lvlBefore = sniper.getLvl();
        sniper.upgrade(game);
        assertEquals(lvlBefore, sniper.getLvl());
    }

    @Test
    void soldierFactoryUpdateTest(){
        SoldierFactory soldierFactory = new SoldierFactory(0,0);
        int lvl = soldierFactory.getLvl();

        soldierFactory.upgrade(game);

        assertTrue(soldierFactory.getLvl()>lvl);
    }

    @Test
    void soldierFactoryUpdateTestNoMoney(){
        game.setMoney(0);
        SoldierFactory soldierFactory = new SoldierFactory(0,0);
        int lvl = soldierFactory.getLvl();

        soldierFactory.upgrade(game);

        assertFalse(soldierFactory.getLvl()>lvl);
    }
}
