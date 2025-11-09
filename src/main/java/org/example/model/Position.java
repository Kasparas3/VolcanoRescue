package org.example.model;

public record Position(int x, int y) {
    public Position add(int dx, int dy) {
        return new Position(x + dx, y + dy);
    }
}
