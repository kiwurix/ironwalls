package gameTest;

import defense.PassiveProtection;
import defense.Wall;
import factories.DefensiveUnitFactory;
import factories.SoldierFactory;
import factories.ZombieFactory;
import game.GameController;
import game.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerPlaceDefensiveUnitTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        Maps.setSelectedMap(1);
        gameController.getGame().setMoney(1000);
    }

    @Test
    void whenNotEnoughMoney_doesNothing() {
        gameController.getGame().setMoney(0);
        PassiveProtection sniper = DefensiveUnitFactory.createSniper(0, 0);

        gameController.placeDefensiveUnit(sniper);

        assertTrue(gameController.getDefense().isEmpty());
    }

    @Test
    void whenInvalidTileForWall_doesNothing() {
        int x = 0, y = 0;
        assertNotEquals(1, Maps.getMap()[y/40][x/40]);

        Wall wall = DefensiveUnitFactory.createWall(x, y);
        int beforeMoney = gameController.getGame().getMoney();

        gameController.placeDefensiveUnit(wall);

        assertTrue(gameController.getDefense().isEmpty());
        assertEquals(beforeMoney, gameController.getGame().getMoney());
    }

    @Test
    void whenInvalidTileForNonWall_doesNothing() {
        Maps.getMap()[0][0]=1;


        PassiveProtection soldier = new SoldierFactory(2, 2).createSoldier(0, 0);
        int beforeMoney = gameController.getGame().getMoney();

        gameController.placeDefensiveUnit(soldier);

        assertTrue(gameController.getDefense().isEmpty());
        assertEquals(beforeMoney, gameController.getGame().getMoney());
    }

    @Test
    void whenValidTileForWall_placesWallAndKeepsMoney() {
        Maps.getMap()[0][0]=1;

        Wall wall = DefensiveUnitFactory.createWall(0, 0);
        int price = wall.getPrice();
        int beforeMoney = gameController.getGame().getMoney();

        gameController.placeDefensiveUnit(wall);

        assertEquals(1, gameController.getDefense().size());
        assertEquals(beforeMoney - price, gameController.getGame().getMoney());
        assertEquals(1, Maps.getMap()[0][0]);
    }

    @Test
    void whenValidTileForNonWall_placesUnitAndMarksMap() {
        Maps.getMap()[0][0]=0;
        PassiveProtection sniper = DefensiveUnitFactory.createSniper(0, 0);
        int price = sniper.getPrice();
        int beforeMoney = gameController.getGame().getMoney();

        gameController.placeDefensiveUnit(sniper);

        assertEquals(1, gameController.getDefense().size());
        assertEquals(beforeMoney - price,gameController.getGame().getMoney());
        assertEquals(4, Maps.getMap()[0][0]);
    }
}
