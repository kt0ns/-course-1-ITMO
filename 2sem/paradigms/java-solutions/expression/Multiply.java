package expression;

import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class Multiply extends BinaryOperations {

    public Multiply(FatherExpression l, FatherExpression r) {
        super(l, r, '*');
    }

    @Override
    public ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> l.evaluate(i) * r.evaluate(i);
    }

    @Override
    public ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> l.evaluate(x, y, z) * r.evaluate(x, y, z);
    }

    @Override
    public ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y, long z) {
        return (l, r) -> l.evaluateL(x, y, z) * r.evaluateL(x, y, z);
    }

}
