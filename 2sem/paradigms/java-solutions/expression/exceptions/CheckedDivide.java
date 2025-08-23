package expression.exceptions;

import expression.*;
import expression.exceptions.excepts.OverflowException;

import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class CheckedDivide extends BinaryOperations {

    public CheckedDivide(FatherExpression l, FatherExpression r) {
        super(l, r, "/");
    }

    @Override
    public ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> l.evaluate(i) / r.evaluate(i);
    }

    @Override
    public ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> {
            int leftValue = l.evaluate(x, y, z);
            int rightValue = r.evaluate(x, y, z);

            if (rightValue == 0 || leftValue == Integer.MIN_VALUE & rightValue == -1) {
                throw new OverflowException("Operand is Integer.MAX_VALUE. Overflow risk!");
            }

            return leftValue / rightValue;
        };
    }

    @Override
    public ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y, long z) {
        return (l, r) -> l.evaluateL(x, y, z) / r.evaluateL(x, y, z);
    }

}
