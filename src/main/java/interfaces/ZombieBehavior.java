package interfaces;

import game.GameController;

public interface ZombieBehavior {
    void update(GameController gameController);
    int getXPosition();
    int getYPosition();
    int getPathProgress();
}

