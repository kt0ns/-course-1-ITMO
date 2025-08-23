package expression.exceptions;

import expression.*;

import java.util.function.ToIntBiFunction;
import java.util.function.ToLongBiFunction;

public class Square extends BinaryOperations {

    public Square(FatherExpression l, FatherExpression r) {
        super(l, r, "perimeter");
    }

    @Override
    public ToIntBiFunction<Expression, Expression> getOperation(int i) {
        return (l, r) -> {

            int leftValue = l.evaluate(i);
            int rightValue = r.evaluate(i);


            if (leftValue < 0 || rightValue < 0 || (rightValue + leftValue > Integer.MAX_VALUE / 2)) {
                throw new ArithmeticException("Operand is Integer.MAX_VALUE. Overflow risk!");
            }

            return (leftValue + rightValue) * 2;
        };
    }

    @Override
    public ToIntBiFunction<TripleExpression, TripleExpression> getOperation(int x, int y, int z) {
        return (l, r) -> {

            int leftValue = l.evaluate(x, y, z);
            int rightValue = r.evaluate(x, y, z);


            if (leftValue < 0 || rightValue < 0 || ( Integer.MAX_VALUE - rightValue - leftValue < Integer.MAX_VALUE / 2)) {
                throw new ArithmeticException("Operand is Integer.MAX_VALUE. Overflow risk!");
            }

            return (leftValue + rightValue) * 2;
        };
    }

    @Override
    public ToLongBiFunction<LongTripleExpression, LongTripleExpression> getOperationL(long x, long y, long z) {
        return (l, r) -> {

            long leftValue = l.evaluateL(x, y, z);
            long rightValue = r.evaluateL(x, y, z);

            if (leftValue < 0 || rightValue < 0 || (rightValue + leftValue > Integer.MAX_VALUE / 2)) {
                throw new ArithmeticException("Operand is Integer.MAX_VALUE. Overflow risk!");
            }

            return (leftValue + rightValue) * 2;
        };
    }

}
