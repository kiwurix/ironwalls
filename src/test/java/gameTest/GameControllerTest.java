package gameTest;

import attack.BlueZombie;
import attack.GreenZombie;
import attack.RedZombie;
import attack.Zombie;
import attackStrategies.Bomb;
import attackStrategies.DefaultBombPlacingStrategy;
import defense.*;
import factories.DefensiveUnitFactory;
import factories.SoldierFactory;
import factories.ZombieFactory;
import game.Game;
import game.GameController;
import game.Maps;
import interfaces.Damageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import windows.WindowsManager;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class GameControllerTest {

    private GameController gameController;

    @BeforeEach
    void setUp() {
        gameController = new GameController();
        Maps.setSelectedMap(1);
        Game.setDifficulty(1);
    }


    @Test
    void placeSoldierFactory_WithEnoughMoney_AddsTwoSoldiersAndDeductsPrice() {
        Maps.getMap()[0][0]=0;
        int initialMoney = gameController.getGame().getMoney();
        int price = SoldierFactory.getPrice();

        gameController.placeSoldierFactory(0, 0);


        assertEquals(1, gameController.getSoldierFactories().size());
        List<PassiveProtection> defense = gameController.getDefense();
        assertEquals(2, defense.size());
        assertEquals(initialMoney - price, gameController.getGame().getMoney());
    }

    @Test
    void placeSoldierFactory_WithoutEnoughMoney_DoesNothing() {
        Maps.getMap()[0][0]=0;
        gameController.getGame().setMoney(0);

        int beforeFactories = gameController.getSoldierFactories().size();
        int beforeDefense = gameController.getDefense().size();

        gameController.placeSoldierFactory(0, 0);

        assertEquals(beforeFactories, gameController.getSoldierFactories().size());
        assertEquals(beforeDefense, gameController.getDefense().size());
    }

    @Test
    void placeSoldierFactory_WithEnoughSpace() {
        Maps.getMap()[0][0]=1;
        int initialMoney = gameController.getGame().getMoney();

        gameController.placeSoldierFactory(0, 0);

        assertEquals(0, gameController.getSoldierFactories().size());
        List<PassiveProtection> defense = gameController.getDefense();
        assertEquals(0, defense.size());
        assertEquals(initialMoney, gameController.getGame().getMoney());
    }

    @Test
    void move_singleGreenZombie_shouldAdvanceAlongRoad() {
        int startX = gameController.getStartXZombies();
        int startY = gameController.getStartYZombies();
        GreenZombie z = new GreenZombie(startX,startY,5,5,1);
        gameController.getZombies().add(z);

        int beforeProgress = z.getPathProgress();
        int beforeX = z.getXPosition();
        int beforeY = z.getYPosition();

        gameController.move();
        gameController.move();


        assertTrue(z.getPathProgress() > beforeProgress);

        assertFalse(z.getXPosition() == beforeX && z.getYPosition() == beforeY);
    }

    @Test
    void move_twoZombies_oneBlocked_shouldNotAdvanceSlower() {
        int startX = gameController.getStartXZombies();
        int startY = gameController.getStartYZombies();

        GreenZombie fast = new GreenZombie(startX, startY,5,5,3);

        GreenZombie slow = new GreenZombie(startX, startY,5,5,1);


        gameController.getZombies().add(fast);
        gameController.getZombies().add(slow);


        gameController.move();
        int fastProgAfterFirst = fast.getPathProgress();
        assertTrue(fast.getPathProgress() > slow.getPathProgress());


        int slowProgBeforeSecond = slow.getPathProgress();


        gameController.move();


        assertEquals(slowProgBeforeSecond, slow.getPathProgress());
        assertTrue(fast.getPathProgress() > fastProgAfterFirst);
    }

    @Test
    void move_singleSoldier_shouldAdvancePathProgress() {

        int startX = gameController.getStartXSoldiers();
        int startY = gameController.getStartYSoldiers();
        Soldier soldier = new SoldierFactory(0, 0).createSoldier(startX, startY);
        gameController.getDefense().add(soldier);


        int progressBefore = soldier.getPathProgress();

        gameController.move();

        assertTrue(soldier.getPathProgress() > progressBefore);
    }

    @Test
    void move_twoSoldiers_oneAhead_blocksTheOther() {
        int startX = gameController.getStartXSoldiers();
        int startY = gameController.getStartYSoldiers();
        Soldier soldier = new SoldierFactory(startX, startY).createSoldier(startX, startY);
        Soldier blockedSoldier = new SoldierFactory(startX, startY).createSoldier(startX, startY);
        gameController.getDefense().add(soldier);
        gameController.getDefense().add(blockedSoldier);


        soldier.setPathProgress(1);
        assertEquals(0, blockedSoldier.getPathProgress());

        gameController.move();

        assertTrue(soldier.getPathProgress() > 1);
        assertEquals(0, blockedSoldier.getPathProgress());
    }

    @Test
    void move_stunnedSoldier() {

        int startX = gameController.getStartXSoldiers();
        int startY = gameController.getStartYSoldiers();
        Soldier soldier = new SoldierFactory(0, 0).createSoldier(startX, startY);
        gameController.getDefense().add(soldier);
        soldier.stun(5);


        int progressBefore = soldier.getPathProgress();

        gameController.move();

        assertFalse(soldier.getPathProgress() > progressBefore);
    }

    @Test
    void move_Soldier_but_Zombie_near() {
        int startX = gameController.getStartXSoldiers();
        int startY = gameController.getStartYSoldiers();
        Soldier soldier = new SoldierFactory(0, 0).createSoldier(startX, startY);
        gameController.getDefense().add(soldier);
        GreenZombie greenZombie = new GreenZombie(0,0,5,5,1);
        gameController.getZombies().add(greenZombie);
        greenZombie.setPathProgress(14);
        soldier.setPathProgress(14);
        soldier.setRange(10000000);


        gameController.move();
        int progressBefore = soldier.getPathProgress();
        gameController.move();

        assertFalse(soldier.getPathProgress() > progressBefore);
    }

    @Test
    void startWave_withSoldierFactory_addsTwoSoldiersEach() {
        SoldierFactory f1 = new SoldierFactory(0, 0);
        SoldierFactory f2 = new SoldierFactory(1, 0);
        gameController.getSoldierFactories().add(f1);
        gameController.getSoldierFactories().add(f2);

        gameController.startWave();

        assertEquals( 2 * 2, gameController.getDefense().size());
    }

    @Test
    void getZombiesInRange_withNoZombie_returnsEmpty() {
        List<Zombie> result = gameController.getZombiesInRange(0, 0, 100);
        assertTrue(result.isEmpty());
    }

    @Test
    void getZombiesInRange_singleZombieInRange_returnsThatZombie() {
        GreenZombie z = new GreenZombie(3, 4, 10, 1, 1);
        gameController.getZombies().add(z);

        List<Zombie> result = gameController.getZombiesInRange(0, 0, 100);
        assertEquals(1, result.size());
        assertSame(z, result.getFirst());
    }

    @Test
    void getZombiesInRange_singleZombieOutOfRange_notIncluded() {
        GreenZombie z = new GreenZombie(6, 8, 10, 1, 1);
        gameController.getZombies().add(z);

        List<Zombie> result = gameController.getZombiesInRange(0, 0, 9);
        assertTrue(result.isEmpty());
    }

    @Test
    void blueZombieDeath_shouldStunOnlyUnitsInRange() {
        BlueZombie bz = new ZombieFactory().createBlueZombie(0,0);

        int range = bz.getFreezeRange();

        Soldier inRange = new SoldierFactory(0, 0).createSoldier(0, 0);
        Soldier outOfRange = new SoldierFactory(range * 2, 0).createSoldier(range * 2, 0);

        gameController.getDefense().add(inRange);
        gameController.getDefense().add(outOfRange);


        assertFalse( inRange.isStunned());
        assertFalse(outOfRange.isStunned());


        gameController.blueZombieDeath(bz);


        assertTrue( inRange.isStunned());
        assertFalse( outOfRange.isStunned());
    }

    @Test
    void redZombieDeath_shouldDamageOnlyUnitsInRange() {
        RedZombie rz = new ZombieFactory().createRedZombie(0, 0);
        gameController.getZombies().add(rz);

        int range  = rz.getBombRange();
        int damage = rz.getBombDamage();

        Wall inRange = DefensiveUnitFactory.createWall(0, 0);
        Wall outOfRange = DefensiveUnitFactory.createWall(range * 2, 0);
        gameController.getDefense().add(inRange);
        gameController.getDefense().add(outOfRange);

        int hpBeforeIn  = inRange.getHp();
        int hpBeforeOut =  outOfRange.getHp();


        gameController.redZombieDeath(rz);


        assertEquals(hpBeforeIn - damage, inRange.getHp());
        assertEquals(hpBeforeOut, outOfRange.getHp());
    }

    @Test
    void reduceStunTest(){
        Soldier soldier = new Soldier(0,0,5,5,5,5,1,5,90);
        gameController.getDefense().add(soldier);
        int stun = 1;

        soldier.stun(stun);
        gameController.reduceStan(2);

        assertTrue(soldier.canAttack());
    }

    @Test
    void sniperAttack_shouldDamageFirstZombieAndResetCooldown() {
        Sniper sniper = new Sniper(0,0,5,5,60,2,90);
        gameController.getDefense().add(sniper);

        GreenZombie z = new GreenZombie(5,5,10,1,1);
        gameController.getZombies().add(z);
        int zHpBefore= z.getHp();

        assertTrue(sniper.canAttack());
        gameController.attacks();


        assertEquals( zHpBefore - sniper.getDamage(), z.getHp() );
        assertFalse(sniper.canAttack());

        gameController.updateCooldowns(1000);
        assertTrue(sniper.canAttack());
    }

    @Test
    void bomberAttack_shouldPlaceOneBombAndResetCooldown() {
        Bomber bomber = new Bomber(100,100,50,5,60,2,60,2,90);
        gameController.getDefense().add(bomber);

        GreenZombie z = new GreenZombie(105,105,5,1,1);
        gameController.getZombies().add(z);
        int hpBefore = z.getHp();

        assertTrue(bomber.canAttack());
        assertTrue(bomber.canAttack());

        gameController.attacks();

        List<Bomb> bombs = gameController.getBombs();
        assertEquals(1, bombs.size());

        Bomb bomb = bombs.getFirst();

        assertEquals(105, bomb.getX());
        assertEquals(105, bomb.getY());

        assertFalse(bomber.canAttack());

        gameController.updateBombs(1000);

        assertTrue(z.getHp() < hpBefore);

        assertTrue(gameController.getBombs().isEmpty());
    }

    @Test
    void updateWaveTest_WithNoZombies(){
        gameController.updateWave();
        assertFalse(gameController.getZombies().isEmpty());
    }
    @Test
    void updateWaveTest_WithOneZombie(){
        gameController.getZombies().add(new ZombieFactory().createGreenZombie(0,0));
        gameController.updateWave();
        assertEquals(1,gameController.getZombies().size());
    }

    @Test
    void defenseDeathTest(){
        Wall wall = new Wall(0,0,5,5);
        gameController.getDefense().add(wall);

        wall.takeDamage(100);

        gameController.defenseDeath();

        assertTrue(gameController.getDefense().isEmpty());
    }
    @Test
    void getWaveTest(){
        assertEquals(0,gameController.getWave());
    }

    @Test
    void lastWaveTest_dif2(){
        Game.setDifficulty(2);
        gameController.startWave();
        assertEquals(16,gameController.getLastWave());
    }

    @Test
    void lastWaveTest_dif3(){
        Game.setDifficulty(3);
        gameController.startWave();
        assertEquals(21,gameController.getLastWave());
    }

    @Test
    void lastWaveTest_dif10(){
        Game.setDifficulty(10);
        gameController.startWave();
        assertEquals(16,gameController.getLastWave());
    }

    @Test
    void BossBehaviorUpdateTest(){
        gameController.spawnFinalBoss();
        Sniper sniper = new Sniper(gameController.getStartXZombies()+2, gameController.getStartYZombies(),
                5,5,5,5,90);
        gameController.getDefense().add(sniper);

        gameController.updateBossBehavior();

        assertTrue(sniper.isStunned());
    }

    @Test
    void BossBehaviorUpdateTestNoBoss(){
        Sniper sniper = new Sniper(gameController.getStartXZombies()+2, gameController.getStartYZombies(),
                5,5,5,5,90);
        gameController.getDefense().add(sniper);

        gameController.updateBossBehavior();

        assertFalse(sniper.isStunned());
    }

    @Test
    void checkBaseHPTest_0Hp(){
        Game game = gameController.getGame();
        game.setBaseHP(0);
        gameController.getZombies().add(new ZombieFactory().createGreenZombie(0,0));
        gameController.getDefense().add(new Wall(5,5,5,5));

        gameController.checkBaseHP();

        assertTrue(gameController.getDefense().isEmpty());
        assertTrue(gameController.getZombies().isEmpty());
    }

    @Test
    void checkBaseHPTest(){
        Game game = gameController.getGame();
        game.setBaseHP(5);
        gameController.getZombies().add(new ZombieFactory().createGreenZombie(0,0));
        gameController.getDefense().add(new Wall(5,5,5,5));

        gameController.checkBaseHP();

        assertFalse(gameController.getDefense().isEmpty());
        assertFalse(gameController.getZombies().isEmpty());
    }

}
