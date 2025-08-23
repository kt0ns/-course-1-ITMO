package expression.generic.utilities;

abstract class GenericBinaryOperation<T extends Number> extends GenericOperation<T> {

    public GenericBinaryOperation(GenericExpression<T> l, GenericExpression<T> r, char t) {
        super(l, r, t);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + TypeOFOperation + " " + right.toString() + ")";
    }
}
