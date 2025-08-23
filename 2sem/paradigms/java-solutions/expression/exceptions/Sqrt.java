package expression.exceptions;

import expression.*;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class Sqrt extends UnaryOperations {
    public Sqrt(FatherExpression arg) {
        super(arg, "sqrt");
    }

    @Override
    protected ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> (int) Math.sqrt(l.evaluate(i));
    }

    @Override
    protected ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> {

            int leftValue = l.evaluate(x, y, z);

            if (leftValue < 0) {
                throw new ArithmeticException("Operand is Integer.MAX_VALUE. Overflow risk!");
            }

            return (int) Math.sqrt(leftValue);
        };
    }

    @Override
    protected ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y, long z) {
        return (l, r) -> (int) Math.sqrt(l.evaluateL(x, y, z));
    }

    @Override
    public int getPriority() {
        return 5;
    }
}
