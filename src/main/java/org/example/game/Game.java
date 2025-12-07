package org.example.game;

import org.example.entities.Resident;
import org.example.io.ConsoleUI;
import org.example.logic.BfsPathfindingStrategy;
import org.example.logic.LavaSpread;
import org.example.logic.PathfindingStrategy;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;

import java.util.Scanner;

public class Game {
    private final ConsoleUI ui = new ConsoleUI();
    private final GameState state = new GameState(new Grid(GameConfig.WIDTH, GameConfig.HEIGHT));
    private final PathfindingStrategy pathfinder = new BfsPathfindingStrategy();

    public void run() {
        initDemoMapAndResidents();

        try (Scanner sc = new Scanner(System.in)) {
            while (!state.allEvacuatedOrDead()) {
                ui.print(state);

                if (state.actionsLeft() <= 0) {
                    pause(GameConfig.AUTO_TICK_DELAY_MS);
                }
                handlePlayerAction(sc);
                step();
            }
            ui.print(state);
            System.out.println("Game end. Saved: " + state.savedCount()
                    + ", Died: " + state.deadCount());
        }
    }

    private void pause(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    private void handlePlayerAction(Scanner sc) {
        if (state.actionsLeft() <= 0) return;

        var maybe = ui.readCommand(sc);
        if (maybe.isEmpty()) return;
        var cmd = maybe.get();

        switch (cmd.action()) {
            case BUILD_BARRICADE -> tryBuildBarricade(cmd.pos());
            case OPEN_ROAD      -> tryOpenRoad(cmd.pos());
            case SKIP           -> {}
        }
    }

    private void step() {
        state.nextTick();
        LavaSpread.expand(state);

        for (Resident r : state.residents()) {
            if (!r.alive() || r.evacuated()) continue;

            var here = state.grid().get(r.pos());
            if (here == TileType.LAVA) { r.die(); continue; }
            if (here == TileType.SAFE) { r.evacuate(); continue; }

            if (!r.shouldMoveOnTick(state.tick())) continue;

            for (int s = 0; s < r.stepsPerTick() && r.alive() && !r.evacuated(); s++) {
                var path = pathfinder.computePath(state.grid(), r.pos());
                if (path.size() < 2) break;

                var next = path.get(1);
                var nextTile = state.grid().get(next);

                if (nextTile == TileType.LAVA) { r.die(); break; }

                r.moveTo(next);
                if (nextTile == TileType.SAFE) { r.evacuate(); break; }
            }
        }
    }

    private void tryBuildBarricade(Position p) {
        if (!state.grid().inBounds(p)) return;
        if (state.grid().get(p) == TileType.ROAD && !isResidentOn(p)) {
            state.grid().set(p, TileType.BARRICADE);
            state.consumeAction();
        }
    }

    private void tryOpenRoad(Position p) {
        if (!state.grid().inBounds(p)) return;
        TileType t = state.grid().get(p);
        if (t == TileType.BLOCKED || t == TileType.HOUSE) {
            state.grid().set(p, TileType.ROAD);
            state.consumeAction();
        }
    }

    private boolean isResidentOn(Position p) {
        for (Resident r : state.residents()) {
            if (r.alive() && r.pos().equals(p)) return true;
        }
        return false;
    }

    private void initDemoMapAndResidents() {
        long seed = System.currentTimeMillis();
        org.example.logic.MapGenerator.initRandom(state, seed);
    }
}
