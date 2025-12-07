package org.example.entities;

import org.example.model.Position;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResidentTypesTest {

    @Test
    void slowMovesEverySecondTick() {
        var r = new SlowResident(1, new Position(0,0));
        assertTrue(r.shouldMoveOnTick(0));
        assertFalse(r.shouldMoveOnTick(1));
        assertTrue(r.shouldMoveOnTick(2));
    }

    @Test
    void fastHasTwoStepsPerTick() {
        var r = new FastResident(2, new Position(0,0));
        assertEquals(2, r.stepsPerTick());
    }
}
