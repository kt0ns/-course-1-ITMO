package MNKGame;

import java.util.*;

public class MNK implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.E, '.'
    );

    private final Cell[][] cells;
    private Cell turn;
    private int rows;
    private int cols;
    private int k;
    private int S;
    private int p;
    private int i;

    public MNK() {
        Scanner scanner = new Scanner(System.in);

        do {
            System.out.println("Enter m n k p");
            try {
                rows = scanner.nextInt();
                cols = scanner.nextInt();
                k = scanner.nextInt();
                p = scanner.nextInt();
                if (rows <= 0 || cols <= 0 || k <= 0 || k > Math.max(rows, cols) || p < 0) {
                    System.out.println("Invalid value. Enter a number greater than 0.");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
            } catch (NoSuchElementException e) {
                System.out.println("Game is crashed");
                System.exit(0);
                break;
            }
        } while (rows <= 0 || cols <= 0 || k <= 0);

        cells = new Cell[rows][cols];
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }

        this.S = cols * rows;
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
        S--;

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
        int a = -(rows % 2) + 2 * (i + 1);
        int b = -(cols % 2) + 2 * (i + 1);
        boolean bool = i > p
                || ((rows - a) / 2) <= move.getRow() && move.getRow() < ((rows + a) / 2) &&
                (((cols - b) / 2) <= move.getColumn() && move.getColumn() < ((cols + b) / 2));

        return 0 <= move.getRow() && move.getRow() < rows
                && 0 <= move.getColumn() && move.getColumn() < cols
                && cells[move.getRow()][move.getColumn()] == Cell.E
                && turn == getCell()
                && bool;
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int a = -(rows % 2) + 2 * (i + 1);
        int b = -(cols % 2) + 2 * (i + 1);

        int cl = (int) (Math.log10(cols)) + 1;
        int rw = (int) (Math.log10(rows)) + 1;

        sb.append(" ".repeat(rw + 1));
        for (int c = 0; c < cols; c++) {
            sb.append(String.format("%" + (cl + 1) + "d", c + 1));
        }
        sb.append("\n");

        sb.append(" ".repeat(rw + 1));
        for (int c = 0; c < cols; c++) {
            sb.append("-".repeat(cl + 1));
        }
        sb.append("\n");

        for (int r = 0; r < rows; r++) {
            sb.append(String.format("%" + rw + "d|", r + 1));
            for (int c = 0; c < cols; c++) {
                char i = SYMBOLS.get(cells[r][c]);
                if (i == '.' && ((rows - a) / 2) <= r && r < ((rows + a) / 2) &&
                        (((cols - b) / 2) <= c && c < ((cols + b) / 2))) {
                    sb.append(String.format("%" + (cl + 1) + "s", "*"));
                } else {
                    sb.append(String.format("%" + (cl + 1) + "s", i));
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    private boolean checkDirection(int x, int y, int[] dxA, int[] dyA) {
        int countOfCells = 1;
        for (int g = 0; g < dxA.length; g++){
            int dx = dxA[g];
            int dy = dyA[g];
            for (int i = -1; i < 2; i += 2) {
                for (int j = 1; j < k; j++) {
                    int newX = x + j * dx * i;
                    int newY = y + j * dy * i;

                    if (newX < 0 || newX >= rows || newY < 0 || newY >= cols) {
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

    @Override
    public int getRows() {
        return rows;
    }

    @Override
    public int getCols() {
        return cols;
    }
}