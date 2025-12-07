package org.example.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GridPassabilityTest {

    @Test
    void isPassableForLavaRules() {
        Grid g = new Grid(1,1, TileType.ROAD);
        Position p = new Position(0,0);

        g.set(p, TileType.ROAD);
        assertTrue(g.isPassableForLava(p));
        g.set(p, TileType.HOUSE);
        assertTrue(g.isPassableForLava(p));
        g.set(p, TileType.BLOCKED);
        assertTrue(g.isPassableForLava(p));

        g.set(p, TileType.BARRICADE);
        assertFalse(g.isPassableForLava(p));
        g.set(p, TileType.SAFE);
        assertFalse(g.isPassableForLava(p));
        g.set(p, TileType.LAVA);
        assertFalse(g.isPassableForLava(p));
    }
}
