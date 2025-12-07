package org.example.logic;

import org.example.entities.*;
import org.example.game.GameConfig;
import org.example.game.GameState;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class MapGenerator {
    private MapGenerator() { }

    public static void initRandom(GameState state, long seed) {
        Random rng = new Random(seed);
        Grid g = state.grid();

        Position safe = placeSafeAndLava(state, g, rng);
        sprinkleBlocked(g, rng, safe);
        spawnResidents(state, g, rng, safe);
    }

    private static Position placeSafeAndLava(GameState state, Grid g, Random rng) {
        int W = g.width(), H = g.height();
        Position safe = randomCorner(rng, W, H);
        Position lava = oppositeCorner(safe, W, H);

        g.set(safe, TileType.SAFE);
        g.set(lava, TileType.LAVA);

        state.lavaFrontier().clear();
        state.lavaFrontier().add(lava);

        return safe;
    }

    private static void sprinkleBlocked(Grid g, Random rng, Position safe) {
        for (int y = 0; y < g.height(); y++) {
            for (int x = 0; x < g.width(); x++) {
                Position p = new Position(x, y);
                if (p.equals(safe)) continue;
                if (g.get(p) == TileType.LAVA) continue;
                if (g.get(p) != TileType.ROAD) continue;

                if (rng.nextDouble() < GameConfig.PROB_BLOCKED) {
                    g.set(p, TileType.BLOCKED);
                }
            }
        }
    }

    private static void spawnResidents(GameState state, Grid g, Random rng, Position safe) {
        ResidentFactory factory = ResidentFactory.defaultFactory();
        Set<Position> used = new HashSet<>();

        for (int i = 1; i <= GameConfig.NUM_RESIDENTS; i++) {
            Position home = pickHouseSpot(rng, g, safe, used);
            g.set(home, TileType.HOUSE);

            ResidentType type = decideResidentType(rng);
            state.addResident(factory.create(type, i, home));

            used.add(home);
        }
    }

    private static ResidentType decideResidentType(Random rng) {
        return (rng.nextDouble() < GameConfig.FAST_SHARE)
                ? ResidentType.FAST : ResidentType.SLOW;
    }

    private static Position randomCorner(Random rng, int W, int H) {
        int pick = rng.nextInt(4);
        return switch (pick) {
            case 0 -> new Position(0, 0);
            case 1 -> new Position(W - 1, 0);
            case 2 -> new Position(0, H - 1);
            default -> new Position(W - 1, H - 1);
        };
    }

    private static Position oppositeCorner(Position p, int W, int H) {
        int x = (p.x() == 0) ? W - 1 : 0;
        int y = (p.y() == 0) ? H - 1 : 0;
        return new Position(x, y);
    }

    private static Position pickHouseSpot(Random rng, Grid g, Position safe, Set<Position> used) {
        int W = g.width(), H = g.height();

        for (int tries = 0; tries < GameConfig.MAX_HOUSE_PICK_TRIES; tries++) {
            Position p = new Position(rng.nextInt(W), rng.nextInt(H));
            if (used.contains(p)) continue;
            if (!g.inBounds(p)) continue;
            if (p.equals(safe)) continue;

            TileType t = g.get(p);
            if (t != TileType.ROAD && t != TileType.BLOCKED) continue;

            int dist = Math.abs(p.x() - safe.x()) + Math.abs(p.y() - safe.y());
            if (dist < GameConfig.MIN_HOUSE_DIST) continue;

            if (t == TileType.LAVA) continue;

            return p;
        }
        return new Position(1, H / 2);
    }
}
