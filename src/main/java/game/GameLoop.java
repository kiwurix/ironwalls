package game;

import ui.ZombieDeathEffectView;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Manages the main game loop with a fixed update rate.
 */
public class GameLoop {

    /** Target frames per second. */
    private double FPS;

    /** Thread pool for concurrent updates. */
    private ExecutorService executor = Executors.newFixedThreadPool(2);

    /** Game controller handling game logic. */
    private GameController gameController;

    /** Time delta between frames. */
    private double delta;

    /**
     * Creates a new GameLoop.
     * @param FPS target frames per second
     * @param gameController game controller instance
     */
    public GameLoop(double FPS, GameController gameController) {
        this.FPS = FPS;
        this.gameController = gameController;
        this.delta = 1 / FPS;
    }

    /**
     * Executes a single game tick.
     */
    public void tick() {
        try {
            Future<?> f1 = executor.submit(() -> {
                gameController.checkBaseHP();
                gameController.updateBossBehavior();
            });

            Future<?> f2 = executor.submit(() -> {
                gameController.defenseDeath();
                gameController.zombieDeath();
                gameController.move();
                gameController.updateWave();
                gameController.handleCollisions();
                gameController.reduceStan(delta);
                gameController.attacks();
                gameController.updateCooldowns(delta);
                gameController.updateBombs(delta);
                ZombieDeathEffectView.updateAll(delta);
            });

            f1.get();
            f2.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Shuts down the executor service.
     */
    public void shutdown() {
        executor.shutdown();
    }
}
