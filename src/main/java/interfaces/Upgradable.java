package interfaces;

import game.Game;

public interface Upgradable {
    void upgrade(Game game);
    int getLvl();
    int getUpgradePrice();
}
