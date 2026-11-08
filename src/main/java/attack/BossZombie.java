package attack;

public class BossZombie extends GreenZombie{

    public BossZombie(int xPosition, int yPosition,int lvl) {
        super(xPosition, yPosition, lvl*5, lvl, 1);
        setName("Final Boss");
    }

}
