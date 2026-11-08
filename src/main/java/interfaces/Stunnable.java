package interfaces;

public interface Stunnable {
    void stun(double duration);
    boolean isStunned();          // проверка, оглушён ли юнит
    void reduceStun(double delta);
}
