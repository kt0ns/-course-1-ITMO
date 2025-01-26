package expression;

import java.util.Objects;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

abstract class Operation implements FatherExpression {
    protected final char TypeOFOperation;
    protected FatherExpression left;
    protected FatherExpression right;

    public Operation(FatherExpression l, FatherExpression r, char t) {
        this.TypeOFOperation = t;
        this.left = l;
        this.right = r;
    }

    public Operation(FatherExpression arg, char t) {
        this.TypeOFOperation = t;
        this.left = arg;
    }

    protected abstract ToIntBiFunction<Expression, Expression> getOperation(int i);

    protected abstract ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z);

    protected abstract ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y,
                                                                                                  long z);

    public int evaluate(int i) {
        return getOperation(i).applyAsInt(left, right);
    }

    public int evaluate(int x, int y, int z) {
        return getOperation(x, y, z).applyAsInt(left, right);
    }

    public long evaluateL(long x, long y, long z) {
        return getOperationL(x, y, z).applyAsLong(left, right);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        return Objects.equals(TypeOFOperation, ((Operation) o).TypeOFOperation)
                && Objects.equals(left, ((Operation) o).left)
                && Objects.equals(right, ((Operation) o).right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(TypeOFOperation, left.toString(), right.toString());
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

