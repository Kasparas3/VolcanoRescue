package org.example.logic;

import org.example.entities.Resident;
import org.example.game.GameConfig;
import org.example.game.GameState;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public final class MapGenerator {
    private MapGenerator() {}

    public static void initRandom(GameState state, long seed) {
        Random rng = new Random(seed);
        Grid g = state.grid;
        int W = g.width(), H = g.height();

        Position safe = randomCorner(rng, W, H);
        Position lava = oppositeCorner(safe, W, H);

        g.set(safe, TileType.SAFE);
        g.set(lava, TileType.LAVA);
        state.lavaFrontier.clear();
        state.lavaFrontier.add(lava);

        for (int y = 0; y < H; y++) {
            for (int x = 0; x < W; x++) {
                Position p = new Position(x, y);
                if (p.equals(safe) || p.equals(lava)) continue;
                if (g.get(p) != TileType.ROAD) continue;
                if (rng.nextDouble() < GameConfig.PROB_BLOCKED) {
                    g.set(p, TileType.BLOCKED);
                }
            }
        }

        Set<Position> used = new HashSet<>();
        for (int i = 1; i <= GameConfig.NUM_RESIDENTS; i++) {
            Position home = pickHouseSpot(rng, g, safe, used);
            g.set(home, TileType.HOUSE);
            state.residents.add(new Resident(i, home));
            used.add(home);

        }
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

        for (int tries = 0; tries < 1000; tries++) {
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
