package expression.exceptions;


public class Const extends Operands {
    private int i;
    private long il;

    public Const(int i) {
        super(i);
        this.i = i;
    }

    public Const(long il) {
        super(il);
        this.il = il;
    }

    @Override
    public int evaluate(int a) {
        return i;
    }

    @Override
    public int evaluate(int x, int y, int z) {
        return i;
    }

    @Override
    public long evaluateL(long x, long y, long z) {
        return il;
    }

    @Override
    public int getPriority() {
        return 10;
    }

    @Override
    public boolean isLeftAs() {
        return false;
    }

    public int getI() {
        return i;
    }
}
