package org.example.game;

import org.example.entities.Resident;
import org.example.model.Grid;
import org.example.model.Position;

import java.util.*;

public final class GameState {
    private final Grid grid;
    private final List<Resident> residents = new ArrayList<>();
    private final Set<Position> lavaFrontier = new HashSet<>();
    private int tick = 0;
    private int actionsLeft = GameConfig.MAX_PLAYER_ACTIONS;

    public GameState(Grid grid){ this.grid=grid; }
    public Grid grid(){ return grid; }
    public List<Resident> residents(){ return residents; }
    public Set<Position> lavaFrontier(){ return lavaFrontier; }
    public int tick(){ return tick; }
    public void nextTick(){ tick++; }
    public int actionsLeft(){ return actionsLeft; }
    public void consumeAction(){ if (actionsLeft>0) actionsLeft--; }
    public void addResident(Resident r){ residents.add(r); }

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