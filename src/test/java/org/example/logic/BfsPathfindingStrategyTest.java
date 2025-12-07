package org.example.logic;

import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BfsPathfindingStrategyTest {

    @Test
    void findsPathOnClearGrid() {
        Grid g = new Grid(5,5, TileType.ROAD);
        g.set(new Position(4,4), TileType.SAFE);

        var path = new BfsPathfindingStrategy().computePath(g, new Position(0,0));

        assertFalse(path.isEmpty());
        assertEquals(new Position(0,0), path.get(0));
        assertEquals(new Position(4,4), path.get(path.size()-1));
    }

    @Test
    void returnsEmptyWhenBlockedByBarricades() {
        Grid g = new Grid(3,3, TileType.ROAD);
        g.set(new Position(2,1), TileType.SAFE);
        g.set(new Position(1,0), TileType.BARRICADE);
        g.set(new Position(1,1), TileType.BARRICADE);
        g.set(new Position(1,2), TileType.BARRICADE);

        var path = new BfsPathfindingStrategy().computePath(g, new Position(0,1));
        assertTrue(path.isEmpty());
    }
}
