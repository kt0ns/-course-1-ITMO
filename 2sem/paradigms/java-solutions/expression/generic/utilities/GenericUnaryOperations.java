package expression.generic.utilities;

public abstract class GenericUnaryOperations<T extends Number> extends GenericOperation<T> {
    public GenericUnaryOperations(GenericExpression<T> arg, char t) {
        super(arg, t);
    }

    @Override
    public String toString() {
        return TypeOFOperation + "(" + left.toString() + ")";
    }
}
