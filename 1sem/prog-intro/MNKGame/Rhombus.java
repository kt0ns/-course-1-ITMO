package MNKGame;

import java.util.*;

public class Rhombus implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.',
            Cell.G, ' '
    );

    private Cell[][] cells;
    private Cell turn;
    private int len;
    private int k;
    private int S;
    private int p;
    private int i;

    public Rhombus() {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Enter len k p");
            try {
                len = scanner.nextInt();
                k = scanner.nextInt();
                p = scanner.nextInt();
                if (len <= 0 || k <= 0 || p < 0) {
                    System.out.println("Invalid value. Enter a number greater than 0.");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Game is crashed");
                System.exit(0);
                break;
            }
        }
        while (len <= 0 || k <= 0 || k > len);

        fillTable();

        this.S = len * len;
        turn = Cell.X;

    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell() {
        return turn;
    }

    @Override
    public Result makeMove(final Move move) {
        if (!isValid(move)) {
            return Result.LOSE;
        }
        i++;
        S -= 1;

        int x = move.getRow();
        int y = move.getColumn();

        cells[x][y] = move.getValue();

        if (checkDirection(x, y, new int[] {1, 1, 0, 1}, new int[] {1, 0, 1, -1})) {
            return Result.WIN;
        }

        if (S == 0) {
            return Result.DRAW;
        }

        turn = turn == Cell.X ? Cell.O : Cell.X;
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        int size = 2 * len - 1;
        int a = -(len % 2) + 2 * (i + 1);
        int b = -(len % 2) + 2 * (i + 1);
        boolean bool = i > p
                || ((size - a) / 2) <= move.getRow() && move.getRow() < ((size + a) / 2) &&
                (((size - b) / 2) <= move.getColumn() && move.getColumn() < ((size + b) / 2));

        return 0 <= move.getRow() && move.getRow() < len * 2 - 1
                && 0 <= move.getColumn() && move.getColumn() < len * 2 - 1
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell()
                && bool;
    }

    private boolean checkDirection(int x, int y, int[] dxA, int[] dyA) {
        int countOfCells = 1;
        for (int g = 0; g < dxA.length; g++) {
            int dx = dxA[g];
            int dy = dyA[g];
            for (int i = -1; i < 2; i += 2) {
                for (int j = 1; j < k; j++) {
                    int newX = x + j * dx * i;
                    int newY = y + j * dy * i;

                    if (newX < 0 || newX >= len || newY < 0 || newY >= len || cells[newX][newY] == Cell.G) {
                        break;
                    }

                    if (cells[newX][newY] == turn) {
                        countOfCells++;
                    } else {
                        break;
                    }
                }
            }
        }
        return countOfCells >= k;
    }


    private void fillTable() {
        cells = new Cell[len * 2 - 1][len * 2 - 1];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.G);
        }

        for (int i = 0; i < len; i++) {
            for (int j = len - i - 1; j < len + i; j++) {
                cells[i][j] = Cell.E;
            }
        }
        for (int i = 2 * len - 1 - 1; i > len - 1; i--) {
            for (int j = len - (2 * len - 1 - 1 - i) - 1; j < len + (2 * len - 1 - 1 - i); j++) {
                cells[i][j] = Cell.E;
            }
        }
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        int a = -(len % 2) + 2 * (i + 1);
        int b = -(len % 2) + 2 * (i + 1);

        int n = (int) Math.log10(len * 2 - 1) + 1;

        sb.append(" ".repeat(n + 1));
        for (int c = 0; c < len * 2 - 1; c++) {
            sb.append(String.format("%" + (n + 1) + "d", c + 1));
        }
        sb.append("\n");

        sb.append(" ".repeat(n + 1));
        for (int c = 0; c < len * 2 - 1; c++) {
            sb.append("-".repeat(n + 1));
        }
        sb.append("\n");

        for (int r = 0; r < len * 2 - 1; r++) {
            sb.append(String.format("%" + n + "d|", r + 1));
            for (int c = 0; c < len * 2 - 1; c++) {
                char i = SYMBOLS.get(cells[r][c]);
                if (i == '.' && ((len * 2 - 1 - a) / 2) <= r && r <= ((len * 2 - 1 + a) / 2) &&
                        (((len * 2 - 1 - b) / 2) <= c && c <= ((len * 2 - 1 + b) / 2))) {
                    sb.append(String.format("%" + (n + 1) + "s", "*"));
                } else {
                    sb.append(String.format("%" + (n + 1) + "s", SYMBOLS.get(cells[r][c])));
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }


    @Override
    public int getRows() {
        return len * 2 - 1;
    }

    @Override
    public int getCols() {
        return len * 2 - 1;
    }
}