package expression;

import java.util.Objects;

abstract class Operands implements FatherExpression {
    private final String num;

    public Operands(int i) {
        this.num = String.valueOf(i);
    }

    public Operands(long i) {
        this.num = String.valueOf(i);
    }

    public Operands(String i) {
        this.num = i;
    }

    @Override
    public String toString() {
        return num;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        return Objects.equals(num, ((Operands) o).num);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(num);
    }

    protected String getNumber() {
        return num;
    }
}
