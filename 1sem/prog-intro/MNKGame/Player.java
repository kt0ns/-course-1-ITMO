package MNKGame;


public interface Player {
    Move move(Position position, Cell cell);

    default boolean willThrowException(Position position, Cell cell) {
        try {
            move(position, cell);
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
