package org.example.logic;

import org.example.game.GameConfig;
import org.example.game.GameState;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MapGeneratorTest {

    @Test
    void generatesSafeAndLavaInOppositeCornersAndResidentsCount() {
        GameState s = new GameState(new Grid(GameConfig.WIDTH, GameConfig.HEIGHT));
        long seed = 12345L;

        MapGenerator.initRandom(s, seed);

        Position safe = null, lava = null;
        for (int y = 0; y < s.grid().height(); y++) {
            for (int x = 0; x < s.grid().width(); x++) {
                Position p = new Position(x,y);
                if (s.grid().get(p) == TileType.SAFE) safe = p;
                if (s.grid().get(p) == TileType.LAVA) lava = p;
            }
        }
        assertNotNull(safe);
        assertNotNull(lava);
        assertTrue((safe.x()==0 || safe.x()==s.grid().width()-1) &&
                (safe.y()==0 || safe.y()==s.grid().height()-1));
        assertEquals(s.grid().width()-1 - safe.x(), lava.x());
        assertEquals(s.grid().height()-1 - safe.y(), lava.y());

        assertEquals(GameConfig.NUM_RESIDENTS, s.residents().size());
        s.residents().forEach(r ->
                assertNotEquals(TileType.SAFE, s.grid().get(r.pos()))
        );
    }
}
