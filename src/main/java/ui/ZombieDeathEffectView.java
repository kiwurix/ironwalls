package ui;

import Effects.ZombieDeathEffect;
import attack.Zombie;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the display and management of zombie death effects.
 * Supports adding, updating, and drawing visual effects on the canvas.
 */
public class ZombieDeathEffectView {
    private static final List<ZombieDeathEffect> effects = new ArrayList<>();

    /**
     * Adds a new death effect based on a zombie.
     * @param zombie the zombie to create an effect from
     */
    public static void addEffect(Zombie zombie) {
        effects.add(new ZombieDeathEffect(zombie));
    }

    /**
     * Adds an existing ZombieDeathEffect to the effects list.
     * @param effect the zombie death effect to add
     */
    public static void addEffect(ZombieDeathEffect effect){
        effects.add(effect);
    }

    /**
     * Updates all active death effects and removes finished ones.
     * @param delta the time elapsed since the last update
     */
    public static void updateAll(double delta) {
        for (ZombieDeathEffect effect : effects) {
            effect.update(delta);
        }
        effects.removeIf(ZombieDeathEffect::isFinished);
    }

    /**
     * Draws all active death effects on the canvas.
     * @param gc the GraphicsContext to draw on
     */
    public static void drawAll(GraphicsContext gc) {
        for (ZombieDeathEffect effect : effects) {
            draw(gc, effect);
        }
    }

    /**
     * Draws a single zombie death effect.
     * @param gc the GraphicsContext to draw on
     * @param effect the effect to draw
     */
    public static void draw(GraphicsContext gc, ZombieDeathEffect effect) {
        int x = effect.getX();
        int y = effect.getY();
        int range = effect.getRange();

        gc.setGlobalAlpha(0.3);
        switch (effect.getType()) {
            case RED_EXPLOSION -> {
                gc.setFill(Color.RED);
            }
            case BLUE_FREEZE -> {
                gc.setFill(Color.LIGHTBLUE);
            }
        }
        gc.fillOval(x-range+20, y-range+20, range*2,range*2 );
        gc.setGlobalAlpha(1.0);
    }

    /**
     * Returns the list of all current zombie death effects.
     * @return list of zombie death effects
     */
    public static List<ZombieDeathEffect> getZombieDeathEffects(){
        return effects;
    }

}
