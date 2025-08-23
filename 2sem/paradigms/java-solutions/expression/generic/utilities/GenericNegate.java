package expression.generic.utilities;


import expression.generic.ClassesTypes.TypeForCalculate;

import java.util.function.BiFunction;

public class GenericNegate<T extends Number> extends GenericBinaryOperation<T> {
    private final TypeForCalculate<T> type;

    public GenericNegate(GenericExpression<T> l, TypeForCalculate<T> type) {
        super(l, null, '-');
        this.type = type;
    }

    @Override
    protected BiFunction<T, T, T> getOperation() throws ArithmeticException, IllegalArgumentException {
        return getAdditionFunction(type);
    }

    private static <T extends Number> BiFunction<T, T, T> getAdditionFunction(TypeForCalculate<T> type) throws ArithmeticException, IllegalArgumentException {
        return type::negate;
    }
}
