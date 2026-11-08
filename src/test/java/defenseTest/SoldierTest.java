package defenseTest;

import defense.Soldier;
import factories.SoldierFactory;
import interfaces.AttackStrategy;
import interfaces.Damageable;
import interfaces.Stunnable;
import interfaces.Upgradable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SoldierTest {

    private Soldier soldier;

    @BeforeEach
    void setUp() {
        soldier = new SoldierFactory(0, 0).createSoldier(0, 0);
    }

    @Test
    void testGetAndSetHp() {
        int maxHp = soldier.getMaxHp();
        assertEquals(maxHp, soldier.getHp());

        soldier.setHp(maxHp / 2);
        assertEquals(maxHp / 2, soldier.getHp());

        soldier.setHp(maxHp);
        assertEquals(maxHp, soldier.getHp());
    }

    @Test
    void testGetAndSetMaxHp() {
        int originalMax = soldier.getMaxHp();

        soldier.setMaxHp(originalMax + 20);
        assertEquals(originalMax + 20, soldier.getMaxHp());

        assertTrue(soldier.getHp() <= soldier.getMaxHp());
    }

    @Test
    void testIsDead() {
        soldier.setHp(1);
        assertFalse(soldier.isDead());

        soldier.setHp(0);
        assertTrue(soldier.isDead());

        soldier.setHp(-5);
        assertTrue(soldier.isDead());
    }

    @Test
    void testGetAttackStrategy() {
        AttackStrategy strategy = soldier.getAttackStrategy();
        assertNotNull(strategy);
        assertEquals("SingleTargetAttackStrategy",strategy.getClass().getSimpleName());
    }
}
