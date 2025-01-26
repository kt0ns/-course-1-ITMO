package expression;

import java.lang.Math;
import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class Sqrt extends UnaryOperations {
    public Sqrt(FatherExpression arg) {
        super(arg, '√');
    }

    @Override
    protected ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> (int) Math.sqrt(l.evaluate(i));
    }

    @Override
    protected ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> (int) Math.sqrt(l.evaluate(x, y, z));
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
