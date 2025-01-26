package MNKGame;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class HumanPlayer implements Player {
    private final PrintStream out;
    private Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");
            int[] cord = getValidMove();

            final Move move = new Move(cord[0], cord[1], cell);
            if (position.isValid(move)) {
                return move;
            }
            out.println("Move " + move + " is invalid");
        }
    }

    public int[] getValidMove() throws NoSuchElementException {
        boolean f = false;
        int[] move = new int[2];
        do {
            try {
                move[0] = in.nextInt() - 1;
                move[1] = in.nextInt() - 1;
                f = true;
            } catch (InputMismatchException e) {
                System.out.println("Incorrect input type. Write digits :/");
                in.nextLine();
            }
        } while (!f);
        return move;
    }
}
