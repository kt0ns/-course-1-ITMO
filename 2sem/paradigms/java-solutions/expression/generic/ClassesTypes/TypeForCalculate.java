package expression.generic.ClassesTypes;

public interface TypeForCalculate<T> {
    T add(T left, T right);
    T subtract(T left, T right);
    T multiply(T left, T right);
    T divide(T left, T right);
    T area(T left, T right);
    T perimeter(T left, T right);
    T getValue(String val);
    T negate(T left, T right);
}
