package org.example.logic;

import org.example.game.GameState;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LavaSpreadTest {

    @Test
    void lavaExpandsThroughRoadHouseBlocked() {
        GameState s = new GameState(new Grid(3,1, TileType.ROAD));
        s.grid().set(new Position(0,0), TileType.LAVA);
        s.lavaFrontier().add(new Position(0,0));

        s.grid().set(new Position(1,0), TileType.BLOCKED);

        LavaSpread.expand(s);

        assertEquals(TileType.LAVA, s.grid().get(new Position(1,0)));
    }

    @Test
    void lavaDoesNotCrossBarricadeOrSafe() {
        GameState s = new GameState(new Grid(3,1, TileType.ROAD));
        s.grid().set(new Position(0,0), TileType.LAVA);
        s.grid().set(new Position(1,0), TileType.BARRICADE);
        s.grid().set(new Position(2,0), TileType.SAFE);
        s.lavaFrontier().add(new Position(0,0));

        LavaSpread.expand(s);

        assertEquals(TileType.BARRICADE, s.grid().get(new Position(1,0)));
        assertEquals(TileType.SAFE, s.grid().get(new Position(2,0)));
    }
}
