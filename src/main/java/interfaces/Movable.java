package interfaces;

import java.util.List;

public interface Movable {
    void move(List<int[]> road);
    int getPathProgress();
    void setPathProgress(int pathProgress);

}
