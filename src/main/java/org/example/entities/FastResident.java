package org.example.entities;

import org.example.model.Position;

public final class FastResident extends Resident {
    public FastResident(int id, Position start) { super(id, start); }
    @Override public int stepsPerTick() { return 2; }
}
