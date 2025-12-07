package org.example.logic;

import org.example.game.GameState;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;

import java.util.HashSet;
import java.util.Set;

public final class LavaSpread {

    private LavaSpread() {}

    public static void expand(GameState state) {
        Grid grid = state.grid();

        Set<Position> currentFrontier = state.lavaFrontier().isEmpty()
                ? collectAllLava(grid)
                : new HashSet<>(state.lavaFrontier());

        Set<Position> nextFrontier = new HashSet<>();

        for (Position lava : currentFrontier) {
            for (Position nb : grid.neighbors4(lava)) {
                if (!grid.isPassableForLava(nb)) continue;

                grid.set(nb, TileType.LAVA);
                nextFrontier.add(nb);
            }
        }

        state.lavaFrontier().clear();
        state.lavaFrontier().addAll(nextFrontier);
    }


    private static Set<Position> collectAllLava(Grid grid) {
        Set<Position> all = new HashSet<>();
        for (int y = 0; y < grid.height(); y++) {
            for (int x = 0; x < grid.width(); x++) {
                Position p = new Position(x, y);
                if (grid.get(p) == TileType.LAVA) {
                    all.add(p);
                }
            }
        }
        return all;
    }
}