package game;

import attackStrategies.Bomb;
import factories.SoldierFactory;
import factories.ZombieFactory;
import finalBossDecorators.ExplosionsBehavior;
import finalBossDecorators.SpawnZombieBehavior;
import finalBossDecorators.StunAuraBehavior;
import windows.WindowsManager;
import attack.*;
import defense.*;
import interfaces.Damageable;
import interfaces.Movable;
import interfaces.Stunnable;
import interfaces.ZombieBehavior;
import ui.ZombieDeathEffectView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Controls the core gameplay logic.
 * Manages waves, unit displacement, movement, attacks and death events.
 */

public class GameController {
    /** Logger for game events.*/
    private final Logger logger = Logger.getLogger(GameController.class.getName());

    /**List of all zombies on the map.*/
    private List<Zombie> zombies = new ArrayList<>();

    /**Main Game Object.*/
    private List<PassiveProtection> defense = new ArrayList<>();
    private final Game game = new Game();
    private int startXZombies=game.getXZombies();
    private int startYZombies=game.getYZombies();
    private int startXSoldiers= game.getXSoldiers();
    private int startYSoldiers=game.getYSoldiers();

    /**Factory for zombie creation.*/
    private final ZombieFactory zombieFactory = new ZombieFactory();

    /**List of Soldier factories.*/
    private final List<SoldierFactory> soldierFactories = new ArrayList<>();

    /**List of placed Bombs.*/
    private final List<Bomb> bombs = new ArrayList<>();

    /**Final boss behaviour decorator.*/
    private  ZombieBehavior bossBehavior = null;

    /**Wave number, when the boss appears.*/
    private int lastWave;

    /**
     * Places a soldier factory on the map and creates two soldiers.
     * @param x X coordinate of placement
     * @param y Y coordinate of placement
     */
    public void placeSoldierFactory(int x, int y) {
        if (Maps.getMap()[y/40][x/40] != 0) {
            logger.warning("Invalid placement location for unit: Soldier base");
            return;
        }
        if (game.getMoney() < SoldierFactory.getPrice()) {
            logger.warning("Not enough money to place Soldier base");
            return;
        }

        SoldierFactory factory = new SoldierFactory(x, y);
        game.setMoney(game.getMoney() - factory.getPrice());
        soldierFactories.add(factory);
        defense.add(factory.createSoldier(startXSoldiers,startYSoldiers));
        defense.add(factory.createSoldier(startXSoldiers,startYSoldiers));
        logger.info("Placed Soldier Factory at (" + x/40 + ", " + y/40 + ")");
        logger.info("Spawned two initial soldiers from the new factory");

    }

    /** Moves all zombies along the path. */
    private void moveZombies() {
        logger.fine("Moving zombies");
        int f;
        for (Zombie zombie : zombies) {
            f=1;
            for(Zombie next : zombies){
                if(next.getPathProgress()>zombie.getPathProgress()){
                    if(next.getPathProgress()-zombie.getPathProgress()<10&&(next.getSpeed()>=zombie.getSpeed())){
                        f=0;
                        break;
                    }
                }
            }
            if(f==1) {
                int[] point = Maps.getRoad().get(Maps.getRoad().size()-zombie.getPathProgress()-1);
                for (int i = 0; i < zombie.getSpeed(); i++) {
                    zombie.move(Maps.getRoad());
                    if(zombie.getXPosition()==startXSoldiers&&zombie.getYPosition()==startYSoldiers){
                        break;
                    }
                }
                if (zombie.getPathProgress() < Maps.getRoad().size()) {
                    int[] pointNext = Maps.getRoad().get(Maps.getRoad().size()-zombie.getPathProgress()-1);
                    rotateZombie(point[1], pointNext[1], zombie);
                }
            }
        }
    }

    /** Moves all soldiers if they have no targets in range. */
    private void moveSoldiers() {
        logger.fine("Moving soldiers");
        int f;
        for (PassiveProtection unit : defense) {
            if (unit instanceof Soldier soldier) {
                if(getZombiesInRange(soldier).isEmpty()&&!soldier.isStunned()){
                    f=1;
                    for(PassiveProtection next :defense){
                        if(next instanceof Movable&&(((Movable) next).getPathProgress()>soldier.getPathProgress())){
                            if(((Movable) next).getPathProgress()-soldier.getPathProgress()<10){
                                f=0;
                                break;
                            }
                        }
                    }
                    if(f==1){
                    soldier.move(Maps.getRoad());}
                    int[] point =Maps.getRoad().get(soldier.getPathProgress());
                    rotateActiveProtection(point[1],point[0],soldier);
                }
            }
        }
    }

    /** Updates the boss's behavior if it's currently present. */
    public void updateBossBehavior() {
        if(bossBehavior!=null) bossBehavior.update(this);
    }

    /** Spawns the final boss zombie. */
    public void spawnFinalBoss() {
        BossZombie boss = new BossZombie(startXZombies, startYZombies, Game.getDifficulty());
        bossBehavior = new ExplosionsBehavior(new StunAuraBehavior(new SpawnZombieBehavior(boss)));
        zombies.add(boss);


    }

    /** Checks the base's health and triggers game over if health is zero or below. */
    public void checkBaseHP(){
        if(game.getBaseHP()<=0){
            defense.clear();
            zombies.clear();
            WindowsManager.loseWindow();}
    }

    /**Starts a new wave*/
    public void startWave() {
        int wave = zombieFactory.getWave();
        zombieFactory.nextWave();
        lastWave = switch(Game.getDifficulty()){
            case 1 -> 11;
            case 2 ->16;
            case 3 ->21;
            default -> 16;
        };
        logger.info("Starting wave " + wave);
        if (wave == lastWave) {
            spawnFinalBoss();
            logger.info("Final boss is spawning");
        } else if(wave<lastWave){
            int numberOfZombies = 3 + zombieFactory.getWave() * 2;
            spawnZombies(startXZombies,startYZombies,wave,numberOfZombies);
        }
            for (SoldierFactory factory : soldierFactories) {
                defense.add(factory.createSoldier(startXSoldiers, startYSoldiers));
                defense.add(factory.createSoldier(startXSoldiers, startYSoldiers));
            }

    }

    /**
     * Spawns the specified number of zombies based on the wave.
     * @param x X coordinate of spawn
     * @param y Y coordinate of spawn
     * @param wave wave number
     * @param numberOfZombies number of zombies
     */
    public void spawnZombies(int x,int y,int wave,int numberOfZombies){
        logger.info("Spawning " + numberOfZombies + " zombies at (" + x/40 + ", " + y/40 + ") for wave " + wave);
        Random rand = new Random();
        for (int i = 0; i < numberOfZombies; i++) {
            if (wave <= 2) {
                zombies.add(zombieFactory.createGreenZombie(x, y));
            } else if (wave <= 5) {
                int roll = rand.nextInt(3);
                if (roll < 2) zombies.add(zombieFactory.createGreenZombie(x, y));
                else zombies.add(zombieFactory.createBlueZombie(x, y));
            } else if (wave <= 8) {
                int roll = rand.nextInt(4);
                if (roll == 0) zombies.add(zombieFactory.createBlueZombie(x, y));
                else if (roll == 1) zombies.add(zombieFactory.createRedZombie(x, y));
                else zombies.add(zombieFactory.createGreenZombie(x, y));
            } else {
                int roll = rand.nextInt(5);
                if (roll == 0) zombies.add(zombieFactory.createBlackZombie(x, y));
                else if (roll == 1) zombies.add(zombieFactory.createRedZombie(x, y));
                else if (roll == 2) zombies.add(zombieFactory.createBlueZombie(x, y));
                else zombies.add(zombieFactory.createGreenZombie(x, y));
            }
        }
    }

    /**
     * Returns a list of zombies within the attack range of a unit.
     * @param unit the attacking unit
     * @return list of zombies in range
     */
    public List<Zombie> getZombiesInRange(ActiveProtection unit){
        List<Zombie> zombiesInRange = new ArrayList<>();
        int ux = unit.getXPosition();
        int uy = unit.getYPosition();
        int range = unit.getRange();
        for (Zombie zombie:zombies){
            int zx = zombie.getXPosition();
            int zy = zombie.getYPosition();
            double distance = Math.sqrt(Math.pow(ux - zx, 2) + Math.pow(uy - zy, 2));
            if(range>=distance){
                if (!zombie.isInvisible() || (unit instanceof Sniper && ((Sniper) unit).hasVision())) {
                    zombiesInRange.add(zombie);
                }
            }
        }
        zombiesInRange.sort(Comparator.comparingDouble(zombie ->
                Math.sqrt(Math.pow(ux - zombie.getXPosition(), 2) + Math.pow(uy - zombie.getYPosition(), 2))
        ));

        return zombiesInRange;
    }

    /**
     * Rotates an active protection unit towards a target zombie.
     * @param target target zombie
     * @param unit active protection unit
     */
    public void rotateActiveProtection(Zombie target, ActiveProtection unit){
        double angle = calculateDegree(unit.getXPosition(), unit.getYPosition(), target.getXPosition(), target.getYPosition());
        unit.setDegree(angle);
    }

    /**
     * Rotates an active protection unit towards a given point.
     * @param x X coordinate
     * @param y Y coordinate
     * @param unit active protection unit
     */
    public void rotateActiveProtection(int x, int y, ActiveProtection unit){
        double angle = calculateDegree(unit.getXPosition(), unit.getYPosition(), x, y);
        unit.setDegree(angle);
    }

    /**
     * Rotates a zombie based on its movement direction.
     * @param x current X coordinate
     * @param xNext next X coordinate
     * @param zombie the zombie
     */
    public void rotateZombie(int x, int xNext, Zombie zombie) {
        zombie.setLookingLeft(xNext < x);
    }

    /**
     * Returns a list of zombies within a specified range from a point.
     * @param ux center X coordinate
     * @param uy center Y coordinate
     * @param range radius
     * @return list of zombies within range
     */
    public List<Zombie> getZombiesInRange(int ux,int uy,int range){
        List<Zombie> zombiesInRange = new ArrayList<>();
        for (Zombie zombie : zombies){
            int zx = zombie.getXPosition();
            int zy = zombie.getYPosition();
            double distance = Math.sqrt(Math.pow(ux - zx, 2) + Math.pow(uy - zy, 2));
            if(range>=distance){
                    zombiesInRange.add(zombie);
            }
        }
        return zombiesInRange;
    }

    /**
     * Calculates the angle between two points.
     * @param fromX starting X coordinate
     * @param fromY starting Y coordinate
     * @param toX target X coordinate
     * @param toY target Y coordinate
     * @return angle in degrees
     */
    private double calculateDegree(int fromX, int fromY, int toX, int toY) {
        double dx = toX - fromX;
        double dy = toY - fromY;
        return Math.toDegrees(Math.atan2(dy, dx));
    }

    /**
     * Handles the death of a blue zombie and applies stun effect.
     * @param blueZombie the blue zombie
     */
    public void blueZombieDeath(BlueZombie blueZombie) {
        int zx = blueZombie.getXPosition();
        int zy = blueZombie.getYPosition();

        blueZombieStun(zx,zy,blueZombie.getFreezeRange(),blueZombie.getFreezeDuration());

        logger.info("Blue Zombie died. Stunning nearby units in range "+blueZombie.getFreezeRange() + " for "+blueZombie.getFreezeDuration()+" seconds");

    }

    /**
     * Applies a stun effect from a blue zombie.
     * @param zx X coordinate
     * @param zy Y coordinate
     * @param range stun range
     * @param duration stun duration
     */
    public void blueZombieStun(int zx,int zy,int range,double duration){
        for (PassiveProtection unit : defense) {
            int ux = unit.getXPosition();
            int uy = unit.getYPosition();

            double distance = Math.sqrt(Math.pow(ux - zx, 2) + Math.pow(uy - zy, 2));

            if (distance <= range && unit instanceof Stunnable stunnable) {
                stunnable.stun(duration);
            }
        }
    }

    /**
     * Handles the death of a red zombie and applies explosion damage.
     * @param redZombie the red zombie
     */
    public void redZombieDeath(RedZombie redZombie) {
        int zx = redZombie.getXPosition();
        int zy = redZombie.getYPosition();

        redZombieExplosion(zx,zy,redZombie.getBombRange(),redZombie.getBombDamage());

        logger.info("Red Zombie died. Exploding nearby units in range "+redZombie.getBombRange()+" with "+redZombie.getDamage()+" damage");

    }

    /**
     * Deals explosion damage to all defensive units within range.
     * @param zx X coordinate
     * @param zy Y coordinate
     * @param range explosion radius
     * @param damage damage dealt
     */
    public void redZombieExplosion(int zx,int zy,int range,int damage){
        for (PassiveProtection unit : defense) {
            int ux = unit.getXPosition();
            int uy = unit.getYPosition();

            double distance = Math.sqrt(Math.pow(ux - zx, 2) + Math.pow(uy - zy, 2));

            if (distance <= range && unit instanceof Damageable damageable) {
                damageable.takeDamage(damage);
            }
        }
    }

    /*** Handles collisions between zombies, defenses, and the base.*/
    public void handleCollisions() {
        List<Zombie> toRemove = new ArrayList<>();
        List<PassiveProtection> toRemoveDefense = new ArrayList<>();

        for (Zombie zombie : zombies) {
            for (PassiveProtection unit : defense) {
                if (unit instanceof Damageable damageable
                        && Math.abs(zombie.getXPosition() - unit.getXPosition())<10
                        && Math.abs(zombie.getYPosition() - unit.getYPosition())<10) {
                    damageable.takeDamage(zombie.getHp());
                    zombie.takeDamage(damageable.getHp());
                    logger.info("Collision detected between " + zombie.getName() + " and " + unit.getName());
                }
            }
            if (zombie.getXPosition() == game.getXSoldiers()&& zombie.getYPosition() == game.getYSoldiers()) {
                game.damageBase(zombie.getHp());
                toRemove.add(zombie);
            }
        }
        for (PassiveProtection unit : defense) {
            if (unit.getXPosition() == game.getXZombies() && unit.getYPosition() == game.getYZombies()) {
                toRemoveDefense.add(unit);
            }
        }

        defense.removeAll(toRemoveDefense);
        zombies.removeAll(toRemove);
    }

    /**
     * @return the current zombie wave
     */
    public int getWave(){
        return zombieFactory.getWave();
    }

    /**
     * Places a defensive unit on the map.
     * @param unit the defensive unit
     */
    public void placeDefensiveUnit(PassiveProtection unit) {
        int x = unit.getXPosition();
        int y = unit.getYPosition();
        if (game.getMoney() < unit.getPrice()) {
            logger.warning("Not enough money to place " + unit.getName());
            return;
        }
        if (unit instanceof Wall) {
            if(Maps.getMap()[y/40][x/40] != 1){
                logger.warning("Invalid placement location for unit: " + unit.getName());
                return;
            }
        }else{
            if(Maps.getMap()[y/40][x/40] != 0){
                logger.warning("Invalid placement location for unit: " + unit.getName());
                return;
            }
        }
        defense.add(unit);
        game.setMoney(game.getMoney() - unit.getPrice());
        if(!(unit instanceof Wall)){
            Maps.getMap()[y/40][x/40] = 4;
        }

        logger.info("Placed unit: " + unit.getName() + " at (" + x/40 + ", " + y/40 + ")");
    }

    /**
     * Reduces stun durations for all defensive units.
     * @param delta time reduction
     */
    public void reduceStan(double delta){
        for(PassiveProtection unit : defense){
            if(unit instanceof Stunnable){
                ((Stunnable) unit).reduceStun(delta);
            }
        }
    }

    /**
     * Handles zombie deaths and awards rewards.
     */
    public void zombieDeath(){
        for(Zombie zombie :zombies){
            if(zombie.isDead()){
                game.setMoney(game.getMoney()+50);
                if (zombie instanceof BossZombie) {
                    bossBehavior = null;
                    logger.info("Boss Zombie defeated");
                    logger.info("Victory triggered");
                    WindowsManager.victoryWindow();
                }
                ZombieDeathEffectView.addEffect(zombie);
                if(zombie instanceof RedZombie redZombie){
                    redZombieDeath(redZombie);
                }
                if(zombie instanceof BlueZombie blueZombie){
                    blueZombieDeath(blueZombie);
                }
            }
        }
        zombies.removeIf(Zombie::isDead);
    }

    /*** Removes dead defensive units.*/
    public void defenseDeath(){
        defense.removeIf(unit -> unit instanceof Damageable damageable && damageable.isDead());
    }

    /*** Moves all zombies and soldiers.*/
    public void move (){
        moveZombies();
        moveSoldiers();
    }

    /**
     * Updates cooldowns for all active units.
     * @param delta elapsed time
     */
    public void updateCooldowns(double delta) {
        for (PassiveProtection unit : defense) {
            if (unit instanceof ActiveProtection active) {
                active.updateCooldown(delta);
            }
        }
    }

    /*** Handles attacks from all active units.*/
    public void attacks(){
        List<Zombie> inRange;
        for (PassiveProtection unit : defense) {
            if (unit instanceof ActiveProtection active) {
                inRange=getZombiesInRange(active);
                if(active.canAttack()&&!inRange.isEmpty()){
                    rotateActiveProtection(inRange.getFirst(),active);
                    if(unit instanceof Bomber bomber){
                        bomber.getBombPlacingStrategy().placeBomb(bomber,inRange,bombs);
                        bomber.resetCooldown();
                        logger.info("Bomber placed a bomb");
                    }else {
                        active.attack(inRange);
                        active.resetCooldown();
                        logger.info(unit.getName() + " is attacking zombies");
                    }
                }
            }
        }
    }

    /**
     * Updates the state of bombs and triggers their explosions.
     * @param delta elapsed time
     */
    public void updateBombs(double delta){
        for(Bomb bomb :bombs){
            bomb.update(delta);
            if(bomb.isExploded()){
            bomb.getAttackStrategy().attack(getZombiesInRange(bomb.getX(),bomb.getY(),bomb.getRange()),bomb.getDamage());
            }
        }
        bombs.removeIf(Bomb::isExploded);
    }

    /**
     * @return list of all zombies
     */
    public List<Zombie> getZombies() {
        return zombies;
    }

    /**
     * @return list of all defensive units
     */
    public List<PassiveProtection> getDefense() {
        return defense;
    }

    /**
     * @return the game object
     */
    public Game getGame() {
        return game;
    }

    /**
     * @return list of all soldier factories
     */
    public List<SoldierFactory> getSoldierFactories() {
        return soldierFactories;
    }

    /*** Starts a new wave if all zombies are defeated.*/
    public void updateWave(){
        if(zombies.isEmpty()){
            logger.info("All zombies died. Preparing next wave");
            startWave();
        }
    }

    /**
     * @return initial Y coordinate for zombies
     */
    public int getStartYZombies() {
        return startYZombies;
    }

    /**
     * @return initial X coordinate for soldiers
     */
    public int getStartXSoldiers() {
        return startXSoldiers;
    }

    /**
     * @return initial Y coordinate for soldiers
     */
    public int getStartYSoldiers() {
        return startYSoldiers;
    }

    /**
     * @return initial X coordinate for zombies
     */
    public int getStartXZombies() {
        return startXZombies;
    }

    /**
     * @return list of all bombs
     */
    public List<Bomb> getBombs() {
        return bombs;
    }

    /**
     * @return last wave counter
     */
    public int getLastWave() {
        return lastWave;
    }
}
