package expression;

import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class Triangle extends BinaryOperations {

    public Triangle(FatherExpression l, FatherExpression r) {
        super(l, r, '◣');
    }

    @Override
    public ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> {

            int leftValue = l.evaluate(i);
            int rightValue = r.evaluate(i);

            if (leftValue < 0 || rightValue <0 || (leftValue > 1 && rightValue > Integer.MAX_VALUE / leftValue * 2)) {
                throw new ArithmeticException("Overflow risk!");
            }

            return (leftValue * rightValue) / 2;
        };
    }

    @Override
    public ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> {

            int leftValue = l.evaluate(x, y, z);
            int rightValue = r.evaluate(x, y, z);

            if (leftValue < 0 || rightValue < 0 || (leftValue > 1 && rightValue > (Integer.MAX_VALUE / leftValue) * 2)) {
                throw new ArithmeticException("Overflow risk!");
            }
            if (leftValue % 2 == 0) {
                return (leftValue / 2) * rightValue;
            } else if (rightValue % 2 == 0) {
                return leftValue * (rightValue / 2);
            }
            return leftValue > rightValue ? leftValue / 2 * rightValue : leftValue * rightValue / 2;
        };
    }

    @Override
    public ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y, long z) {
        return (l, r) -> {
            long leftValue = l.evaluateL(x, y, z);
            long rightValue = r.evaluateL(x, y, z);

            if (leftValue < 0 || rightValue < 0 || (leftValue > 1 && rightValue > Long.MAX_VALUE / leftValue * 2)) {
                throw new ArithmeticException("Overflow risk!");
            }

            return (leftValue * rightValue) / 2;
        };
    }

}
