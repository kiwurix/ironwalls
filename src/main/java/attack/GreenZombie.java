package attack;

public class GreenZombie extends Zombie {
    public GreenZombie(int xPosition, int yPosition, int hp,int lvl, int speed) {
        super(xPosition, yPosition,hp, lvl, speed, false);
        setName("Default Zombie");
    }
}
