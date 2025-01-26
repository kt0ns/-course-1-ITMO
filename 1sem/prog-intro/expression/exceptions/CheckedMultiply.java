package expression.exceptions;

import exceptions.excepts.OverflowException;
import expression.*;

import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class CheckedMultiply extends BinaryOperations {

    public CheckedMultiply(FatherExpression l, FatherExpression r) {
        super(l, r, '*');
    }

    @Override
    public ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> l.evaluate(i) * r.evaluate(i);
    }

    @Override
    public ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> {
            int leftValue = l.evaluate(x, y, z);
            int rightValue = r.evaluate(x, y, z);

            if (leftValue != 0) {
                int lg = leftValue == -1 ? Integer.MAX_VALUE : Integer.MIN_VALUE / leftValue;
                if (leftValue > 0
                        && (lg > rightValue || rightValue > Integer.MAX_VALUE / leftValue)
                        || leftValue < 0
                        && (Integer.MAX_VALUE / leftValue > rightValue || rightValue > lg)) {
                    throw new OverflowException("Overflow risk.");
                }
            }

            return leftValue * rightValue;
        };
    }

    @Override
    public ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y, long z) {
        return (l, r) -> l.evaluateL(x, y, z) * r.evaluateL(x, y, z);
    }

}
