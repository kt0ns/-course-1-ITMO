package expression.generic;


import expression.generic.ClassesTypes.TypeForCalculate;
import expression.generic.utilities.*;

public class ParserGenerics<T extends Number> extends BaseParser {
    private TypeForCalculate<T> type;

    public ParserGenerics() {
    }

    public GenericExpression<T> parse(String string, TypeForCalculate<T> type) {
        this.type = type;
        return parse(new StringSource(string));
    }

    private GenericExpression<T> parse(final StringSource source) {
        setSource(source);
        skipWhitespace();
        return parseGeom();
    }

    private GenericExpression<T> parseGeom() {
        GenericExpression<T> left = parseAS();
        skipWhitespace();
//        while ((test('◣') || test('▯') || test('a') || test('p')) && !eof()) {
        while (test('◣', '▯', 'a', 'p') && !eof()) {
            if (test('◣', '▯')) {
                char op = take();
                GenericExpression<T> right = parseAS();
                left = op == '◣' ? new GenericArea<>(left, right, type) : new GenericPerimeter<>(left, right, type);
            } else {
                String op = parseId();
                GenericExpression<T> right = parseAS();
                left = op.equals("area") ? new GenericArea<>(left, right, type) : new GenericPerimeter<>(left, right, type);
            }
            skipWhitespace();
        }
        return left;
    }

    private GenericExpression<T> parseAS() {
        GenericExpression<T> left = parseMD();
        skipWhitespace();
        while (test('+', '-') && !eof()) {
            char op = take();
            GenericExpression<T> right = parseMD();
            left = op == '+' ? new GenericAdd<>(left, right, type) : new GenericSubtract<>(left, right, type);

            skipWhitespace();
        }
        return left;
    }

    private GenericExpression<T> parseMD() {
        GenericExpression<T> left = parseN();
        skipWhitespace();
        while (test('*', '/') && !eof()) {
            char op = take();
            GenericExpression<T> right = parseN();
            left = op == '*' ? new GenericMultiply<>(left, right, type) : new GenericDivide<>(left, right, type);

            skipWhitespace();
        }
        return left;
    }

    private GenericExpression<T> parseN() {
        skipWhitespace();
        if (take('(', '[', '{')) {
            GenericExpression<T> res = parseGeom();
            skipWhitespace();
            take();
            return res;
        }

        boolean hasMin = take('-');

        if (hasMin && !between('0', '9')) {
            return new GenericNegate<>(parseN(), type);
        }

        return between('0', '9') ? parseNumber(hasMin) : new GenericVariable<>(parseId(), type);
    }

    private GenericConst<T> parseNumber(boolean b) {
        final StringBuilder sb = new StringBuilder();
        if (b) sb.append('-');
        takeInteger(sb);
        return new GenericConst<>(sb.toString(), type);
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