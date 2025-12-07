package org.example.game;

import org.example.entities.SlowResident;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameStateCountersTest {

    @Test
    void savedAndDeadCountersWork() {
        GameState s = new GameState(new Grid(3,1, TileType.ROAD));
        var r1 = new SlowResident(1, new Position(0,0));
        var r2 = new SlowResident(2, new Position(1,0));
        s.addResident(r1);
        s.addResident(r2);

        s.grid().set(new Position(0,0), TileType.SAFE);
        r1.evacuate();

        r2.die();

        assertEquals(1, s.savedCount());
        assertEquals(1, s.deadCount());
        assertTrue(s.allEvacuatedOrDead());
    }
}
