package attackStrategies;

import interfaces.AttackStrategy;
import interfaces.Damageable;

import java.util.List;
import java.util.logging.Logger;

/**
 * An attack strategy that deals damage to all targets in the list.
 */
public class SplashAttackStrategy implements AttackStrategy {
    Logger logger = Logger.getLogger(SplashAttackStrategy.class.getName());

    /**
     * Attacks all targets in the list with the given damage value.
     * @param targets list of targets to attack
     * @param damage  amount of damage to deal to each target
     */
    @Override
    public void attack(List<? extends Damageable> targets,int damage){
        logger.info("SplashAttackStrategy used");
        for(Damageable target : targets){
            target.takeDamage(damage);
        }
    }
}
