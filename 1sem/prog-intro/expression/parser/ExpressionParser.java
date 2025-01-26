package expression.parser;

import expression.*;

public class ExpressionParser extends BaseParser implements TripleParser {

    public ExpressionParser() {
    }

    @Override
    public FatherExpression parse(String string) {
        return parse(new StringSource(string));
    }

    private FatherExpression parse(final StringSource source) {
        setSource(source);
        skipWhitespace();
        return parseAS();
    }

    private FatherExpression parseAS() {
        FatherExpression left = parseMD();
        skipWhitespace();
        while ((test('+') || test('-')) && !eof()) {
            char op = take();
            FatherExpression right = parseMD();
            left = op == '+' ? new Add(left, right) : new Subtract(left, right);

            skipWhitespace();
        }
        return left;
    }

    private FatherExpression parseMD() {
        FatherExpression left = parseN();
        skipWhitespace();
        while ((test('*') || test('/')) && !eof()) {
            char op = take();
            FatherExpression right = parseN();
            left = op == '*' ? new Multiply(left, right) : new Divide(left, right);

            skipWhitespace();
        }
        return left;
    }

    private FatherExpression parseN() {
        skipWhitespace();
        if (take('(')) {
            FatherExpression res = parseAS();
            skipWhitespace();
            take();
            return res;
        }

        boolean hasMin = take('-');

        if (hasMin && !between('0', '9')) {
            return new Negate(parseN());
        }

        if (take('√')) {
            return new Sqrt(parseN());
        }
        return between('0', '9') ? parseNumber(hasMin) : new Variable(parseId());
    }

    private Const parseNumber(boolean b) {
        final StringBuilder sb = new StringBuilder();
        if (b) sb.append('-');
        takeInteger(sb);
        return new Const(Integer.parseInt(sb.toString()));
    }

    private String parseId() {
        final StringBuilder sb = new StringBuilder();
        takeId(sb);
        return sb.toString();
    }

    private void takeInteger(final StringBuilder sb) {
        if (take('0')) {
            sb.append('0');
        } else if (between('1', '9')) {
            takeDigits(sb);
        } else {
            throw error("Invalid number");
        }
    }

    private void takeId(final StringBuilder sb) {
        while (Character.isLetter(getChar())) {
            sb.append(take());
        }
    }

    private void takeDigits(final StringBuilder sb) {
        while (between('0', '9')) {
            sb.append(take());
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(getChar())) {
            take();
        }
    }
}