package org.example.entities;

import org.example.model.Position;

public class Resident {
    private final int id;
    private Position pos;
    private boolean alive = true;
    private boolean evacuated = false;

    public Resident(int id, Position start) {
        this.id = id;
        this.pos = start;
    }

    public int id() { return id; }
    public Position pos() { return pos; }
    public boolean alive() { return alive; }
    public boolean evacuated() { return evacuated; }

    public void moveTo(Position next) {
        if (alive && !evacuated) {
            this.pos = next;
        }
    }

    public void die() {
        this.alive = false;
    }

    public void evacuate() {
        this.evacuated = true;
    }
}