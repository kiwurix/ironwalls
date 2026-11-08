package gameTest;

import attack.GreenZombie;
import defense.Wall;
import factories.DefensiveUnitFactory;
import factories.ZombieFactory;
import attack.Zombie;
import defense.PassiveProtection;
import game.GameController;
import game.Maps;
import interfaces.Damageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerHandleCollisionsTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        Maps.setSelectedMap(1);
    }

    @Test
    void collision_betweenZombieAndWall_appliesDamage() {
        GreenZombie z = new GreenZombie(0,0,5,1,1);
        gameController.getZombies().add(z);


        Wall wall = DefensiveUnitFactory.createWall(5, 5);
        gameController.getDefense().add(wall);

        int wallHpBefore   = wall.getHp();
        int zombieHpBefore = z.getHp();

        gameController.handleCollisions();

        int wallHpAfter   = wall.getHp();
        int zombieHpAfter = z.getHp();

        assertTrue(wallHpAfter < wallHpBefore);
        assertTrue(zombieHpAfter < zombieHpBefore);

        assertTrue(gameController.getZombies().contains(z));
        assertTrue(gameController.getDefense().contains(wall));
    }

    @Test
    void collision_RemovesZombie() {
        int bx = gameController.getStartXSoldiers();
        int by = gameController.getStartYSoldiers();
        GreenZombie z = new ZombieFactory().createGreenZombie(bx,by);
        gameController.getZombies().add(z);

        int baseHpBefore = gameController.getGame().getBaseHP();

        gameController.handleCollisions();

        assertFalse(gameController.getZombies().contains(z));

        assertEquals(baseHpBefore - z.getHp(),gameController.getGame().getBaseHP());
    }

    @Test
    void collision_unitAtZombieSpawn_removedFromDefense() {
        int zx = gameController.getStartXZombies();
        int zy = gameController.getStartYZombies();
        PassiveProtection wall = DefensiveUnitFactory.createWall(zx, zy);
        gameController.getDefense().add(wall);

        gameController.handleCollisions();

        assertFalse(gameController.getDefense().contains(wall));
    }
}
