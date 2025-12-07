package org.example.logic;

import org.example.model.Grid;
import org.example.model.Position;

import java.util.List;

public interface PathfindingStrategy {
    List<Position> computePath(Grid grid, Position start);
}
