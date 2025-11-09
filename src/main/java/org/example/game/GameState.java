package org.example.game;

import org.example.entities.Resident;
import org.example.model.Grid;
import org.example.model.Position;

import java.util.*;

public class GameState {
    public final Grid grid;
    public final List<Resident> residents = new ArrayList<>();
    public final Set<Position> lavaFrontier = new HashSet<>();

    public int tick = 0;
    public int actionsLeft = GameConfig.MAX_PLAYER_ACTIONS;

    public GameState(Grid grid) {
        this.grid = grid;
    }

    public boolean allEvacuatedOrDead() {
        for (Resident r : residents) {
            if (r.alive() && !r.evacuated()) return false;
        }
        return true;
    }

    public long savedCount() {
        return residents.stream().filter(Resident::evacuated).count();
    }

    public long deadCount() {
        return residents.stream().filter(r -> !r.alive()).count();
    }
}