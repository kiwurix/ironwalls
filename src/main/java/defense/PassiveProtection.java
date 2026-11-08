package defense;

/**
 * Base class for all defense units..
 */
public abstract class PassiveProtection  {
    /** X coordinate of the unit. */
    private int xPosition;

    /** Y coordinate of the unit. */
    private int yPosition;

    /** Name of the unit. */
    private String name;

    /** Current level. */
    private int lvl=1;

    /** Price to place the unit. */
    private int price;

    /**
     * Constructs a PassiveProtection with position and price.
     * @param xPosition the x coordinate
     * @param yPosition the y coordinate
     * @param price the price of the unit
     */
    public PassiveProtection(int xPosition, int yPosition, int price) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.price=price;
    }

    /**
     * Returns the x coordinate.
     * @return x position
     */
    public int getXPosition() {
        return xPosition;
    }

    /**
     * Returns the y coordinate.
     * @return y position
     */
    public int getYPosition() {
        return yPosition;
    }

    /**
     * Sets the x coordinate.
     * @param xPosition new x position
     */
    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    /**
     * Sets the y coordinate.
     * @param yPosition new y position
     */
    public void setYPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    /**
     * Returns the unit name.
     * @return name of the unit
     */
    public String getName() {
        return name;
    }


    /**
     * Sets the unit name.
     * @param name unit name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the current level.
     * @return level
     */
    public int getLvl() {
        return lvl;
    }

    /**
     * Sets the upgrade level.
     * @param lvl new upgrade level
     */
    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    /**
     * Returns the unit's price.
     * @return price
     */
    public int getPrice() {
        return price;
    }
}
