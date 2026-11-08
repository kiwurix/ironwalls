package attackStrategies;

import attack.Zombie;
import defense.Bomber;
import interfaces.BombPlacingStrategy;

import java.util.List;
import java.util.logging.Logger;

/**
 * Default bomb placing strategy for Bombers.
 * Places a bomb at the position of the first target zombie.
 */
public class DefaultBombPlacingStrategy implements BombPlacingStrategy {
    Logger logger = Logger.getLogger(DefaultBombPlacingStrategy.class.getName());

    /**
     * Places a bomb near the first zombie target if available.
     * @param bomber  the bomber placing the bomb
     * @param targets list of zombies in range
     * @param bombs   list to add the new bomb into
     */
    @Override
    public void placeBomb(Bomber bomber, List<Zombie> targets, List<Bomb> bombs) {
        logger.info("DefaultBombPlacingStrategy used");
        if(targets.isEmpty()){
            return;
        }
        Zombie firstZombie = targets.getFirst();
        Bomb bomb = new Bomb(firstZombie.getXPosition(),firstZombie.getYPosition(),bomber.getBombTimer(),bomber.getBlastRange(),bomber.getDamage(), bomber.getAttackStrategy());
        bombs.add(bomb);
    }
}
