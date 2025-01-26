package expression.exceptions;

import exceptions.excepts.OverflowException;
import expression.*;

import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class CheckedNegate extends UnaryOperations {
    public CheckedNegate(FatherExpression arg) {
        super(arg, '-');
    }

    @Override
    protected ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> -l.evaluate(i);
    }

    @Override
    protected ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> {
            int val = l.evaluate(x, y, z);

            if (val == Integer.MIN_VALUE ) {
                throw new OverflowException("Overflow risk.");
            }
            return -val;
        };
    }

    @Override
    protected ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y, long z) {
        return (l, r) -> -l.evaluateL(x, y, z);
    }

    @Override
    public int getPriority() {
        return 4;
    }
}
