package org.example.game;

public final class GameConfig {
    private GameConfig() {}

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    public static final int MAX_PLAYER_ACTIONS = 1;
    public static final long AUTO_TICK_DELAY_MS = 350;

    public static final int NUM_RESIDENTS = 6;
    public static final double PROB_BLOCKED = 0.18;
    public static final int MIN_HOUSE_DIST = 4;
}