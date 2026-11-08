package interfaces;

public interface Damageable {
    default void takeDamage(int damage){
        setHp(Math.max(getHp()-damage,0));
    }
    boolean isDead();
    int getHp();
    void setHp(int hp);
    int getMaxHp();
}
