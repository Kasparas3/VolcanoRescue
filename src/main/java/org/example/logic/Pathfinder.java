package org.example.logic;

import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;

import java.util.*;

public final class Pathfinder {

    private Pathfinder() {}

    public static List<Position> shortestPathToAnySafe(Grid grid, Position start) {
        if (grid.inBounds(start) && grid.get(start) == TileType.SAFE) {
            return Collections.emptyList();
        }
        ArrayDeque<Position> q = new ArrayDeque<>();
        Map<Position, Position> prev = new HashMap<>();
        Set<Position> visited = new HashSet<>();

        q.add(start);
        visited.add(start);
        while (!q.isEmpty()) {
            Position cur = q.removeFirst();

            for (Position nb : grid.neighbors4(cur)) {
                if (visited.contains(nb)) continue;

                if (!grid.isWalkableForResident(nb)) continue;

                visited.add(nb);
                prev.put(nb, cur);

                if (grid.get(nb) == TileType.SAFE) {
                    return reconstructPath(prev, start, nb);
                }
                q.addLast(nb);
            }
        }
        return Collections.emptyList();
    }

    private static List<Position> reconstructPath(Map<Position, Position> prev, Position start, Position goal) {
        ArrayList<Position> path = new ArrayList<>();
        Position cur = goal;
        while (cur != null) {
            path.add(cur);
            cur = prev.get(cur);
        }
        if (!path.isEmpty() && !path.get(path.size() - 1).equals(start)) {
            path.add(start);
        }
        Collections.reverse(path);
        return path;
    }
}