package interfaces;

import java.util.List;

public interface AttackStrategy {
    void attack(List<? extends Damageable> targets, int damage);
}
