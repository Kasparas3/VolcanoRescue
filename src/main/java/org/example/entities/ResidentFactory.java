package org.example.entities;

import org.example.model.Position;

@FunctionalInterface
public interface ResidentFactory {
    Resident create(ResidentType type, int id, Position start);

    static ResidentFactory defaultFactory() {
        return (type, id, start) -> switch (type) {
            case SLOW -> new SlowResident(id, start);
            case FAST -> new FastResident(id, start);
        };
    }
}