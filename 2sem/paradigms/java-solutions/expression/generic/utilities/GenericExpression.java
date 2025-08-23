package expression.generic.utilities;

public interface GenericExpression<T extends Number> {
    T evaluate(int x, int y, int z);
    String toString();
}
