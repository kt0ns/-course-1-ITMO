package expression.exceptions;


public class Variable extends Operands {
    public Variable(String i) {
        super(i);
    }

    @Override
    public int evaluate(int a) {
        return a;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        String num = String.valueOf(getNumber().charAt(getNumber().length() - 1));
        if (num.equals("x")) {
            return x;
        } else if (num.equals("y")) {
            return y;
        }
        return z;
    }

    @Override
    public long evaluateL(long x, long y, long z) {
        String num = getNumber();
        if (num.equals("x")) {
            return x;
        } else if (num.equals("y")) {
            return y;
        }
        return z;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean isLeftAs() {
        return false;
    }
}
