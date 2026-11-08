package interfaces;

import java.util.List;

public interface Attacker {
    int getDamage();
    AttackStrategy getAttackStrategy();
    default void attack(List<? extends Damageable> targets) {
        getAttackStrategy().attack(targets, getDamage());
    }
}
