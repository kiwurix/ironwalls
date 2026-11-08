package gameTest;

import attack.*;
import defense.PassiveProtection;
import defense.Soldier;
import defense.Wall;
import factories.DefensiveUnitFactory;
import factories.SoldierFactory;
import factories.ZombieFactory;
import game.Game;
import game.GameController;
import game.Maps;
import interfaces.Damageable;
import interfaces.Stunnable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.ZombieDeathEffectView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerZombieDeathTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        Maps.setSelectedMap(1);
        gameController.getGame().setMoney(100);
    }

    @Test
    void deadGreenZombie_increasesMoneyAndAddsEffectAndRemovesZombie() {
        Zombie green = new ZombieFactory().createGreenZombie(0,0);

        green.takeDamage(green.getHp());
        assertTrue(green.isDead());

        gameController.getZombies().add(green);
        int moneyBefore = gameController.getGame().getMoney();

        gameController.zombieDeath();

        assertEquals(moneyBefore + 50, gameController.getGame().getMoney());
        List<Zombie> remaining = gameController.getZombies();
        assertFalse(remaining.contains(green));
    }

    @Test
    void deadRedZombie_damagesNearbyDefenses() {
        RedZombie red = new ZombieFactory().createRedZombie(0, 0);
        red.takeDamage(red.getHp());

        Wall wallIn = DefensiveUnitFactory.createWall(0, 0);
        Wall wallOut = DefensiveUnitFactory.createWall(red.getBombRange()*2, 0);

        gameController.getZombies().add(red);
        gameController.getDefense().add(wallIn);
        gameController.getDefense().add(wallOut);

        int hpInBefore  =  wallIn.getHp();
        int hpOutBefore =  wallOut.getHp();


        gameController.zombieDeath();


        assertTrue( wallIn.getHp() < hpInBefore);
        assertEquals(hpOutBefore,  wallOut.getHp());
    }

    @Test
    void deadBlueZombie_stunsNearbyDefenses() {
        BlueZombie blue = new ZombieFactory().createBlueZombie(0, 0);
        blue.takeDamage(blue.getHp());

        Soldier unitIn = new SoldierFactory(0, 0).createSoldier(0, 0);
        Soldier unitOut = new SoldierFactory(blue.getFreezeRange()*2, 0).createSoldier(blue.getFreezeRange()*2, 0);

        gameController.getZombies().add(blue);
        gameController.getDefense().add(unitIn);
        gameController.getDefense().add(unitOut);


        gameController.zombieDeath();

        assertTrue(unitIn.isStunned());
        assertFalse(unitOut.isStunned());
    }
}
