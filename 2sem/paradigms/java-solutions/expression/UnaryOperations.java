package expression;

public abstract class UnaryOperations extends Operation {
    public UnaryOperations(FatherExpression arg, String t) {
        super(arg, t);
    }

    public UnaryOperations(FatherExpression arg, char t) {
        super(arg, t);
    }

    @Override
    public String toString() {
        return TypeOFOperation + "(" + left.toString() + ")";
    }

    @Override
    public String toMiniString() {

        String l = left.toMiniString();

        if (left.getPriority() < 3) {
            l = "(" + l + ")";
        } else {
            if (this.getPriority() == 4) {
                l = " " + l;
            }

        }
        return TypeOFOperation + l;
    }
}