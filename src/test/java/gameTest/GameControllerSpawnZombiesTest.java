package gameTest;

import attack.*;
import game.Game;
import game.GameController;
import game.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerSpawnZombiesTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        Maps.setSelectedMap(1);
        Game.setDifficulty(1);
    }

    @Test
    void spawnZombies_wave1_allGreen() {
        int count = 10;
        gameController.spawnZombies(0, 0, 1, count);

        List<Zombie> zs = gameController.getZombies();
        assertEquals(count, zs.size());

        for (Zombie z : zs) {
            assertTrue(z instanceof GreenZombie);
        }
    }

    @Test
    void spawnZombies_wave4_onlyGreenOrBlue() {
        int count = 20;
        gameController.spawnZombies(0, 0, 4, count);

        List<Zombie> zs = gameController.getZombies();
        assertEquals(count, zs.size());

        for (Zombie z : zs) {
            assertTrue(z instanceof GreenZombie || z instanceof BlueZombie);
        }
    }

    @Test
    void spawnZombies_wave7_onlyGreenBlueRed() {
        int count = 15;
        gameController.spawnZombies(0, 0, 7, count);

        List<Zombie> zs = gameController.getZombies();
        assertEquals(count, zs.size());

        for (Zombie z : zs) {
            assertTrue(z instanceof GreenZombie || z instanceof BlueZombie || z instanceof RedZombie);
        }
    }

    @Test
    void spawnZombies_wave10_onlyAllTypes() {
        int count = 30;
        gameController.spawnZombies(0, 0, 10, count);

        List<Zombie> zs = gameController.getZombies();
        assertEquals(count, zs.size());

        for (Zombie z : zs) {
            assertTrue(z instanceof GreenZombie || z instanceof BlueZombie || z instanceof RedZombie || z instanceof BlackZombie);
        }
    }

    @Test
    void startWave_initialWave_spawnsGreenZombies() {
        gameController.startWave();

        List<Zombie> list = gameController.getZombies();

        assertEquals(5, list.size());
        for (Zombie z : list) {
            assertInstanceOf(GreenZombie.class,z);
        }
    }

    @Test
    void startWave_lastWave_spawnsBossOnly() throws Exception {
        int lastWave = 11;

        Field zf = GameController.class.getDeclaredField("zombieFactory");
        zf.setAccessible(true);
        Object factory = zf.get(gameController);

        Field wf = factory.getClass().getDeclaredField("wave");
        wf.setAccessible(true);
        wf.set(factory, lastWave);

        gameController.startWave();

        List<Zombie> list = gameController.getZombies();
        assertEquals(1, list.size());
        assertInstanceOf(BossZombie.class,list.getFirst());
    }

    @Test
    void deadBossZombie_clearsBossBehaviorAndTriggersVictory() throws Exception {
        gameController.spawnFinalBoss();
        gameController.getZombies().getFirst().takeDamage(50000);

        int moneyBefore = gameController.getGame().getMoney();

        gameController.zombieDeath();

        assertEquals(moneyBefore + 50, gameController.getGame().getMoney());

        Field behavior = GameController.class.getDeclaredField("bossBehavior");
        behavior.setAccessible(true);
        assertNull(behavior.get(gameController));

    }
}
