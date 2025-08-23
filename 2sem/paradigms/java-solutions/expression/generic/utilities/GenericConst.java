package expression.generic.utilities;

import expression.generic.ClassesTypes.TypeForCalculate;

public class GenericConst<T extends Number> extends GenericOperands<T> {
    public GenericConst(String i, TypeForCalculate<T> type) {
        super(i, type);

    }

    @Override
    public T evaluate(int x, int y, int z) {
        return type.getValue(num);
    }

    public int getPriority() {
        return 10;
    }

    public boolean isLeftAs() {
        return false;
    }
}
