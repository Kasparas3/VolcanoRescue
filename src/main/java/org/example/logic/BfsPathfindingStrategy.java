package org.example.logic;

import org.example.model.Grid;
import org.example.model.Position;

import java.util.List;

public final class BfsPathfindingStrategy implements PathfindingStrategy {
    @Override
    public List<Position> computePath(Grid grid, Position start) {
        return Pathfinder.shortestPathToAnySafe(grid, start);
    }
}
