package org.example.io;

import org.example.game.GameState;
import org.example.game.PlayerAction;
import org.example.model.Grid;
import org.example.model.Position;
import org.example.model.TileType;

import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {

    public void print(GameState s) {
        printHeader(s.grid());
        printGrid(s);
        printStats(s);
        printHelp();
        System.out.print("> ");
    }

    private void printHeader(Grid g) {
        System.out.print("    ");
        for (int x = 0; x < g.width(); x++) System.out.print(x % 10);
        System.out.println();
    }

    private void printGrid(GameState s) {
        Grid g = s.grid();
        for (int y = 0; y < g.height(); y++) {
            System.out.printf("%3d ", y);
            for (int x = 0; x < g.width(); x++) {
                Position p = new Position(x, y);
                char ch = g.get(p).glyph();
                boolean hasResident = s.residents().stream()
                        .anyMatch(r -> r.alive() && !r.evacuated() && r.pos().equals(p));
                if (hasResident) ch = 'R';
                System.out.print(ch);
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printStats(GameState s) {
        System.out.printf("Tick: %d | Moves Left: %d | Saved: %d | Died: %d%n",
                s.tick(), s.actionsLeft(), s.savedCount(), s.deadCount());
    }

    private void printHelp() {
        System.out.println("Commands:  b x y  (barricade),  o x y  (open road),  s  (skip)");
        System.out.println(
                "Legend: " +
                        TileType.ROAD.glyph() + "=ROAD  " +
                        TileType.HOUSE.glyph() + "=HOUSE  " +
                        TileType.SAFE.glyph() + "=SAFE  " +
                        TileType.LAVA.glyph() + "=LAVA  " +
                        TileType.BARRICADE.glyph() + "=BARRICADE  " +
                        TileType.BLOCKED.glyph() + "=BLOCKED  R=Resident"
        );
    }

    public Optional<UserCommand> readCommand(Scanner sc) {
        if (!sc.hasNextLine()) return Optional.empty();
        String line = sc.nextLine().trim();
        if (line.isEmpty()) return Optional.empty();

        String[] parts = line.split("\\s+");
        String op = parts[0].toLowerCase();

        try {
            return switch (op) {
                case "b" -> {
                    if (parts.length < 3) yield Optional.empty();
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    yield Optional.of(new UserCommand(PlayerAction.BUILD_BARRICADE, new Position(x, y)));
                }
                case "o" -> {
                    if (parts.length < 3) yield Optional.empty();
                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    yield Optional.of(new UserCommand(PlayerAction.OPEN_ROAD, new Position(x, y)));
                }
                case "s" -> Optional.of(new UserCommand(PlayerAction.SKIP, null));
                default -> Optional.empty();
            };
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public record UserCommand(PlayerAction action, Position pos) { }
}
