package game;

/**
 * Represents the game state, including base health, money, and difficulty settings.
 */
public class Game {
    private int baseHP;
    private int money;
    private int maxBaseHP;
    private static int difficulty =1;

    /**
     * Initializes the game with base health and money depending on difficulty.
     */
    public Game() {
        baseHP = switch (difficulty){
            case 1-> 300;
            case 2->100;
            case 3-> 1;
            default -> 100;
        };
        money = switch (difficulty){
            case 1-> 1000;
            case 2->500;
            case 3-> 300;
            default -> 100;
        };
        maxBaseHP = baseHP;
    }

    /**
     * Returns current base HP.
     * @return base hit points
     */
    public int getBaseHP() {
        return baseHP;
    }

    /**
     * Damages the base by a given amount.
     * @param damage the amount of damage
     */
    public void damageBase(int damage) {
        this.baseHP = Math.max(baseHP-damage,0);
        System.out.println("Base took " + damage + " damage. Remaining HP: " + baseHP);

    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getXSoldiers() {
        return Maps.getXSoldiersSpawn();
    }

    public int getYSoldiers() {
        return Maps.getYSoldiersSpawn();
    }

    public int getXZombies() {
        return Maps.getXZombiesSpawn();
    }

    public int getYZombies() {
        return Maps.getYZombiesSpawn();
    }

    public int getMaxBaseHP() {
        return maxBaseHP;
    }

    public static int getDifficulty() {
        return difficulty;
    }

    public static void setDifficulty(int difficulty) {
        Game.difficulty = difficulty;
    }

    public void setBaseHP(int baseHP) {
        this.baseHP = baseHP;
    }
}