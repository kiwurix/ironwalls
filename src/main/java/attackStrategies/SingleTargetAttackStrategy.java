package attackStrategies;

import interfaces.AttackStrategy;
import interfaces.Damageable;

import java.util.List;
import java.util.logging.Logger;

/**
 * An attack strategy that deals damage to a single (first) target.
 */
public class SingleTargetAttackStrategy implements AttackStrategy {
    Logger logger = Logger.getLogger(SingleTargetAttackStrategy.class.getName());

    /**
     * Attacks the first target in the list with the given damage value.
     * @param targets list of potential targets
     * @param damage  amount of damage to deal
     */
    @Override
    public void attack(List<? extends Damageable> targets, int damage) {
        logger.info("SingleTargetAttackStrategy used");
        if (!targets.isEmpty()) {
            targets.get(0).takeDamage(damage);
        }
    }
}
