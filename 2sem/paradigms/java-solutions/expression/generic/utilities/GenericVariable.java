package expression.generic.utilities;

import expression.generic.ClassesTypes.TypeForCalculate;

public class GenericVariable<T extends Number> extends GenericOperands<T> {
    public GenericVariable(String i, TypeForCalculate<T> type) {
        super(i, type);
    }

    @Override
    public T evaluate(int x, int y, int z) {
        String var = String.valueOf(getNumber().charAt(getNumber().length() - 1));

        if (var.equals("x")) {
            return type.getValue(String.valueOf(x));
        } else if (var.equals("y")) {
            return type.getValue(String.valueOf(y));
        }
        return type.getValue(String.valueOf(z));
    }
}
