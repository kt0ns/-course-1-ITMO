package MNKGame;

public interface Position {
    boolean isValid(Move move);
    int getRows();
    int getCols();
    Cell getCell(int r, int c);
}
