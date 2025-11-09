package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int width;
    private final int height;
    private final TileType[][] cells;

    public Grid(int width, int height) {
        this(width, height, TileType.ROAD);
    }

    public Grid(int width, int height, TileType fill) {
        if (width <= 0 || height <= 0) throw new IllegalArgumentException("Grid size must be > 0");
        this.width = width;
        this.height = height;
        this.cells = new TileType[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cells[y][x] = fill;
            }
        }
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean inBounds(Position p) {
        return p.x() >= 0 && p.x() < width && p.y() >= 0 && p.y() < height;
    }

    public TileType get(Position p) {
        if (!inBounds(p)) throw new IndexOutOfBoundsException(p.toString());
        return cells[p.y()][p.x()];
    }

    public void set(Position p, TileType t) {
        if (!inBounds(p)) throw new IndexOutOfBoundsException(p.toString());
        cells[p.y()][p.x()] = t;
    }

    public List<Position> neighbors4(Position p) {
        List<Position> out = new ArrayList<>(4);
        Position up = p.add(0, -1);
        Position down = p.add(0, 1);
        Position left = p.add(-1, 0);
        Position right = p.add(1, 0);
        if (inBounds(up)) out.add(up);
        if (inBounds(down)) out.add(down);
        if (inBounds(left)) out.add(left);
        if (inBounds(right)) out.add(right);
        return out;
    }

    public boolean isWalkableForResident(Position p) {
        if (!inBounds(p)) return false;
        TileType t = get(p);
        return t == TileType.ROAD || t == TileType.HOUSE || t == TileType.SAFE;
    }
}