package MNKGame;

import java.util.InputMismatchException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SettingsOfGame implements Board {
    private static final Map<String, Cell> TYPE_OF_MAP = Map.of(
            "Square", Cell.BASE,
            "Rhombus", Cell.MOD
    );

    public SettingsOfGame() {

    }

    public static Board chooseTheGame() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("What type of board u need? (Square, Rhombus)");
            try {
                Cell typeOfBoard = TYPE_OF_MAP.getOrDefault(scanner.next(), Cell.WRONG);

                switch (typeOfBoard) {
                    case BASE -> {
                        return new MNK();
                    }
                    case MOD -> {
                        return new Rhombus();
                    }
                    case WRONG -> scanner.nextLine();
                }

            } catch (InputMismatchException e) {
                scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Game is crashed");
                System.exit(0);
                break;
            } catch (Exception e) {
                System.out.println("Something going wrong ;/");
            }
        } while (true);
        scanner.close();
        return null;
    }

    @Override
    public Position getPosition() {
        return null;
    }

    @Override
    public Cell getCell() {
        return null;
    }

    @Override
    public Result makeMove(Move move) {
        return null;
    }
}
