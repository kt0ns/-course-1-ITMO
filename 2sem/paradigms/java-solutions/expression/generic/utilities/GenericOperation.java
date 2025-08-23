package expression.generic.utilities;

import java.util.Objects;
import java.util.function.BiFunction;

abstract class GenericOperation<T extends Number> implements GenericExpression<T> {
    protected final char TypeOFOperation;
    protected GenericExpression<T> left;
    protected GenericExpression<T> right;

    public GenericOperation(GenericExpression<T> l, GenericExpression<T> r, char t) {
        this.TypeOFOperation = t;
        this.left = l;
        this.right = r;
    }

    public GenericOperation(GenericExpression<T> arg, char t) {
        this.TypeOFOperation = t;
        this.left = arg;
    }

    protected abstract BiFunction<T, T, T> getOperation();

    public T evaluate(int x, int y, int z) {
        T leftOp = left.evaluate(x, y, z);
        T rightOp = right == null ? null : right.evaluate(x, y, z);
        return getOperation().apply(leftOp, rightOp);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenericOperation<? extends Number> obj = (GenericOperation<? extends Number>) o;
        return Objects.equals(TypeOFOperation, obj.TypeOFOperation)
                && Objects.equals(left, obj.left)
                && Objects.equals(right, obj.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TypeOFOperation, left, right);
    }

    public int getPriority() {
        return switch (this.TypeOFOperation) {
            case '+', '-' -> 1;
            case '*', '/' -> 2;
            default -> 0;
        };
    }

    public boolean isLeftAs() {
        return switch (this.TypeOFOperation) {
            case '-', '/' -> true;
            default -> false;
        };
    }

}

