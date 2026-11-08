package interfaces;

import java.util.List;

public interface Stunner {
    void stun(List<? extends Stunnable> targets);
    int getFreezeDuration();
}
