package expression;

public interface FatherExpression extends Expression, LongTripleExpression, TripleExpression {
    int getPriority();
    boolean isLeftAs();
}
