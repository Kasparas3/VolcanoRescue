package org.example.entities;

import org.example.model.Position;

public final class SlowResident extends Resident {
    public SlowResident(int id, Position start) { super(id, start); }
    @Override public boolean shouldMoveOnTick(int tick) { return tick % 2 == 0; }
}
