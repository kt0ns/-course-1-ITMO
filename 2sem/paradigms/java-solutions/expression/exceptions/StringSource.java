package expression.exceptions;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class StringSource implements CharSource {
    private final String data;
    private int ptr;
    private int pos;

    public StringSource(final String data) {
        this.data = data;
    }

    @Override
    public boolean hasNext() {
        return pos < data.length();
    }

    @Override
    public boolean checkNext() {
        return ptr < data.length();
    }

    @Override
    public char next() {
        movePTR();
        return data.charAt(pos++);
    }

    @Override
    public char saveNext() {
        return data.charAt(ptr++);
    }

    @Override
    public void movePOS() {
        pos = ptr;
    }

    @Override
    public void movePTR() {
        ptr = pos;
    }

    @Override
    public IllegalArgumentException error(final String message) {
        return new IllegalArgumentException(pos + ": " + message);
    }

    public int getPos() {
        return pos;
    }
}
