package gameTest;

import defense.ActiveProtection;
import defense.Sniper;
import attack.GreenZombie;
import game.GameController;
import game.Maps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameControllerRotateTest {
    private GameController gameController;
    private ActiveProtection unit;
    private GreenZombie zombie;

    @BeforeEach
    void setUp() {
        Maps.setSelectedMap(1);
        gameController = new GameController();
        unit = new Sniper(0, 0,5,5,1000,1,90);
        zombie = new GreenZombie(0, 0,10,1,1);
    }

    @Test
    void rotate_toRight_setsZeroDegrees() {
        GreenZombie target = new GreenZombie(10, 0, 10, 1, 1);
        gameController.rotateActiveProtection(target, unit);

        assertEquals(0.0, unit.getDegree());
    }

    @Test
    void rotate_up_setsNinetyDegrees() {
        GreenZombie target = new GreenZombie(0, 10, 10, 1, 1);
        gameController.rotateActiveProtection(target, unit);

        assertEquals(90.0, unit.getDegree());
    }

    @Test
    void rotate_toLeft_setsOneEightyDegrees() {
        GreenZombie target = new GreenZombie(-10, 0, 10, 1, 1);
        gameController.rotateActiveProtection(target, unit);

        assertEquals(180.0, unit.getDegree());
    }

    @Test
    void rotate_down_setsMinusNinetyDegrees() {
        GreenZombie target = new GreenZombie(0, -10, 10, 1, 1);
        gameController.rotateActiveProtection(target, unit);

        assertEquals(-90.0, unit.getDegree());
    }

    @Test
    void rotateZombie_whenNextIsLeft_shouldSetLookingLeftTrue() {
        gameController.rotateZombie(5, 3, zombie);
        assertTrue(zombie.isLookingLeft());
    }

    @Test
    void rotateZombie_whenNextIsRight_shouldSetLookingLeftFalse() {
        gameController.rotateZombie(2, 8, zombie);
        assertFalse(zombie.isLookingLeft());
    }

    @Test
    void rotateZombie_whenNextEqualsX_shouldSetLookingLeftFalse() {
        gameController.rotateZombie(4, 4, zombie);
        assertFalse(zombie.isLookingLeft());
    }
}
