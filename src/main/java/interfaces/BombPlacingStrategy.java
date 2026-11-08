package interfaces;

import attackStrategies.Bomb;
import attack.Zombie;
import defense.Bomber;

import java.util.List;

public interface BombPlacingStrategy {
    void placeBomb(Bomber bomber, List<Zombie> targets, List<Bomb> bombs);
}
