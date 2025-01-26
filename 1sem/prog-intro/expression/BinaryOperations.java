package expression;

public abstract class BinaryOperations extends Operation {
    public BinaryOperations(FatherExpression l, FatherExpression r, char t) {
        super(l, r, t);
    }

    @Override
    public String toString() {
        return "(" + left.toString() + " " + TypeOFOperation + " " + right.toString() + ")";
    }

    @Override
    public String toMiniString() {

        String l = left.toMiniString();
        String r = right.toMiniString();

        if (left.getPriority() < this.getPriority()) {
            l = "(" + l + ")";
        }

        if (right.getPriority() < this.getPriority()
                || (right.getPriority() == this.getPriority() && (isLeftAs()
                || this instanceof Multiply && right instanceof Divide))) {
            r = "(" + r + ")";
        }

        return l + " " + TypeOFOperation + " " + r;
    }
}
