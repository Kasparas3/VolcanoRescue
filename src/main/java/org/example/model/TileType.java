package org.example.model;

public enum TileType {
    ROAD('.'),
    HOUSE('H'),
    SAFE('S'),
    LAVA('~'),
    BARRICADE('#'),
    BLOCKED('X');

    private final char glyph;
    TileType(char glyph) { this.glyph = glyph; }
    public char glyph() { return glyph; }
}
