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
        Grid g = s.grid;
        int w = g.width(), h = g.height();

        System.out.print("    ");
        for (int x = 0; x < w; x++) System.out.print(x % 10);
        System.out.println();

        for (int y = 0; y < h; y++) {
            System.out.printf("%3d ", y);

            for (int x = 0; x < w; x++) {
                Position p = new Position(x, y);
                char ch = charFor(g.get(p));

                boolean coveredByResident = s.residents.stream()
                        .anyMatch(r -> r.alive() && !r.evacuated() && r.pos().equals(p));
                if (coveredByResident) ch = 'R';

                System.out.print(ch);
            }
            System.out.println();
        }

        System.out.println();
        System.out.printf("Tick: %d | Moves Left: %d | Saved: %d | Died: %d%n",
                s.tick, s.actionsLeft, s.savedCount(), s.deadCount());
        System.out.println("Commands:  b x y  (barricade),  o x y  (open road),  s  (skip)");
        System.out.println("Legend: .=ROAD  H=HOUSE  S=SAFE  ~=LAVA  #=BARRICADE  X=BLOCKED  R=Resident");
        System.out.print("> ");
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

    private char charFor(TileType t) {
        return switch (t) {
            case ROAD      -> '.';
            case HOUSE     -> 'H';
            case SAFE      -> 'S';
            case LAVA      -> '~';
            case BARRICADE -> '#';
            case BLOCKED   -> 'X';
        };
    }

    public record UserCommand(PlayerAction action, Position pos) { }
}