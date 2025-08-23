package expression;

import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class Negate extends UnaryOperations {
    public Negate(FatherExpression arg) {
        super(arg, '-');
    }

    @Override
    protected ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> -l.evaluate(i);
    }

    @Override
    protected ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> -l.evaluate(x, y, z);
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
