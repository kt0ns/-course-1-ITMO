package expression.generic.utilities;

import expression.generic.ClassesTypes.TypeForCalculate;

import java.math.BigInteger;
import java.util.function.BiFunction;

public class GenericArea<T extends Number> extends GenericBinaryOperation<T> {
    private final TypeForCalculate<T> type;

    public GenericArea(GenericExpression<T> l, GenericExpression<T> r, TypeForCalculate<T> type) {
        super(l, r, '◣');
        this.type = type;
    }

    @Override
    protected BiFunction<T, T, T> getOperation() throws ArithmeticException, IllegalArgumentException {
        return getAdditionFunction(type);
    }

    private static <T extends Number> BiFunction<T, T, T> getAdditionFunction(TypeForCalculate<T> type) throws ArithmeticException, IllegalArgumentException {
        return type::area;
    }
}
