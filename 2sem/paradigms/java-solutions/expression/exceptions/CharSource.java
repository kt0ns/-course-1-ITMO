package expression.exceptions;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface CharSource {
    boolean hasNext();
    boolean checkNext();
    char next();
    char saveNext();
    void movePOS();
    void movePTR();
    IllegalArgumentException error(String message);
    int getPos();
}
