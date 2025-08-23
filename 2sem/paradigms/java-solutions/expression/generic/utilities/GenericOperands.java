package expression.generic.utilities;

import expression.generic.ClassesTypes.TypeForCalculate;

import java.util.Objects;

abstract class GenericOperands<T extends Number> implements GenericExpression<T> {
    protected final String num;
    protected final TypeForCalculate<T> type;

    public GenericOperands(String i, TypeForCalculate<T> type) {
        this.num = i;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.valueOf(num);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(num, ((GenericOperands<? extends Number>) o).num);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(num);
    }

    protected String getNumber() {
        return num;
    }
}
