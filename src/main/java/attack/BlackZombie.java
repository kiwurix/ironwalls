package attack;

public class BlackZombie extends Zombie {
    public BlackZombie(int xPosition, int yPosition,int hp, int lvl, int speed) {
        super(xPosition, yPosition,hp, lvl, speed, true);
        setName("Ninja Zombie");
    }
}
