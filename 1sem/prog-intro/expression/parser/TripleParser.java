package expression.parser;

import expression.TripleExpression;

@FunctionalInterface
public interface TripleParser {
    TripleExpression parse(String expression);
}
