package expression.exceptions;

import expression.exceptions.excepts.*;
import expression.*;
import expression.parser.TripleParser;

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
        FatherExpression expression = parseG();
        skipWhitespace();
        if (take(')')) {
            throw new BracketException("Invalid bracket sequence, no opening bracket. Wrong bracket position at: " + getPos());
        } else if (!eof()) {
            throw new OperationException("Wrong end of expression. Incorrect position: " + getPos());
        }
        return expression;
    }

    private FatherExpression parseG() {
        FatherExpression left = parseAS();
        skipWhitespace();
        char ch;
        while ((test('◣') || test('▯')) && !eof()) {
            char op = take();
            skipWhitespace();
            ch = getChar();
            if (eof() || ch == '*' || ch == '/' || ch == '+' || ch == ')' || ch == '▯' || ch == '◣') {
                throw new OperandException("No second operand after '" + left.toString() + " " + op + "' at position: " + getPos());
            }
            FatherExpression right = parseAS();
            left = op == '◣' ? new Triangle(left, right) : new Square(left, right);
            skipWhitespace();
        }
        ch = getChar();
        if (eof() || ch == '*' || ch == '/' || ch == '+' || ch == '-' || ch == ')') return left;
        throw new OperationException("Incorrect operation after operand '" + left.toString() + "' at position: " + getPos());
    }

    private FatherExpression parseAS() {
        FatherExpression left = parseMD();
        skipWhitespace();
        char ch;
        while ((test('+') || test('-')) && !eof()) {
            char op = take();
            skipWhitespace();
            ch = getChar();
            if (eof() || ch == '*' || ch == '/' || ch == '+' || ch == ')') {
                throw new OperandException("No second operand after '" + left.toString() + " " + op + "' at position: " + getPos());
            }
            FatherExpression right = parseMD();
            left = op == '+' ? new CheckedAdd(left, right) : new CheckedSubtract(left, right);
            skipWhitespace();
        }
        ch = getChar();
        if (eof() || ch == '*' || ch == '/' || ch == '◣' || ch == '▯' || ch == ')') return left;
        throw new OperationException("Incorrect operation after operand '" + left.toString() + "' at position: " + getPos());
    }

    private FatherExpression parseMD() {
        FatherExpression left = parseN();
        skipWhitespace();
        char ch;
        while ((test('*') || test('/')) && !eof()) {
            char op = take();
            skipWhitespace();
            ch = getChar();
            if (eof() || ch == '*' || ch == '/' || ch == '+') {
                throw new OperandException("No second operand after '" + left.toString() + " " + op + "' at position: " + getPos());
            } else if (ch == ')') {
                throw new BracketException("Invalid bracket sequence, no opening bracket. Wrong position bracket at: " + getPos());
            }
            FatherExpression right = parseN();
            left = op == '*' ? new CheckedMultiply(left, right) : new CheckedDivide(left, right);
            skipWhitespace();
        }
        ch = getChar();
        if (eof() || ch == '+' || ch == '-' || ch == '◣' || ch == '▯' || ch == ')') return left;
        throw new OperationException("Incorrect operation after operand '" + left.toString() + "' at position: " + getPos());
    }

    private FatherExpression parseN() {
        skipWhitespace();
        char ch = getChar();
        if (eof() || ch == '*' || ch == '+' || ch == '/' || ch == '◣' || ch == '▯') {
            throw new OperandException("No operand at position '" + getPos() + "' before operation '" + ch + "'");
        } else if (ch == ')') {
            throw new BracketException("Invalid bracket sequence, no opening bracket. Wrong position bracket at: " + getPos());
        }
        if (take('(')) {
            if (take(')')) {
                throw new OperandException("No operand at position " + getPos());
            }
            FatherExpression res = parseG();
            skipWhitespace();
            if (!take(')')) {
                throw new BracketException("Invalid bracket sequence, expected ')' at position: " + getPos());
            }
            return res;
        }

        boolean hasMin = take('-');

        if (hasMin && !between('0', '9')) {
            return new CheckedNegate(parseN());
        }

        if (take('√')) {
            return new Sqrt(parseN());
        }

        return between('0', '9') ? parseNumber(hasMin) : new Variable(parseId());
    }


    private Const parseNumber(boolean b) {
        final StringBuilder sb = b ? new StringBuilder("-") : new StringBuilder();
        takeInteger(sb);
        String string = sb.toString();
        int checkForMinus = b ? 1 : 0;
        if (string.length() > 10 + checkForMinus) {
            throw new ArithmeticException("OVERFLOW");
        } else if (string.length() == 10 + checkForMinus) {
            String mainBody = string.substring(checkForMinus, checkForMinus + 9);
            if ((b && mainBody.equals("214748364") && string.charAt(string.length() - 1) > '8') ||
                    (!b && mainBody.equals("214748364") && string.charAt(string.length() - 1) > '7') ||
                    mainBody.compareTo("214748364") > 0) {
                throw new ArithmeticException("OVERFLOW");
            }
        }
        return new Const(Integer.parseInt(string));
    }

    private String parseId() {
        skipWhitespace();
        final StringBuilder sb = new StringBuilder();
        takeId(sb);
        if (sb.isEmpty()) {
            throw new BracketException("Invalid bracket sequence, no opening bracket. Wrong position bracket at: " + getPos());
        }
        String string = sb.toString();
        char lastChar = string.charAt(string.length() - 1);
        if (lastChar != 'x' && lastChar != 'z' && lastChar != 'y') {
            throw new VariableException("Wrong variable ID");
        }
        return string;
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
