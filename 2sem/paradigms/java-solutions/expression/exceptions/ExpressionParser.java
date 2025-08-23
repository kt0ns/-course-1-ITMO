package expression.exceptions;

import expression.*;
import expression.exceptions.excepts.BracketException;
import expression.exceptions.excepts.OperandException;
import expression.exceptions.excepts.OperationException;
import expression.exceptions.excepts.VariableException;
import expression.parser.TripleParser;

import java.util.HashMap;
import java.util.HashSet;

public class ExpressionParser extends BaseParser implements TripleParser {
    private final HashMap<Character, Character> bracketPairs = new HashMap<>() {{
        put('(', ')');
        put('[', ']');
        put('{', '}');
    }};

    private final HashSet<String> variables = new HashSet<>() {{
        add("x");
        add("y");
        add("z");
    }};

    public ExpressionParser() {
    }

    @Override
    public FatherExpression parse(String string) {
//        System.err.println(string);
        setSource(new StringSource(string));
        skipWhitespace();
        return startParse();
    }

    private FatherExpression startParse() {
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
        while ((testWord("area") || testWord("perimeter")) && !eof()) {
            String operation = takeWord();
            skipWhitespace();
            ch = getChar();
            if (eof() || ch == '*' || ch == '/' || ch == '+' || ch == ')' || ch == '}' || ch == ']') {
                throw new OperandException("No second operand after '" + left.toString() + " " + operation + "' at position: " + getPos());
            }
            FatherExpression right = parseAS();
            left = operation.equals("area") ? new Triangle(left, right) : new Square(left, right);
            skipWhitespace();
        }
        ch = getChar();
        if (eof() || ch == '*' || ch == '/' || ch == '+' || ch == '-' || ch == ')' || ch == '}' || ch == ']')
            return left;
        throw new OperationException("Incorrect operation after operand '" + left.toString() + "' at position: " + getPos());
    }

    private FatherExpression parseAS() {
        FatherExpression left = parseMD();
        skipWhitespace();
        char ch;
        while ((testChar('+') || testChar('-')) && !eof()) {
            char op = takeChar();
            skipWhitespace();
            ch = getChar();
            if (eof() || ch == '*' || ch == '/' || ch == '+' || ch == ')' || ch == '}' || ch == ']') {
                throw new OperandException("No second operand after '" + left.toString() + " " + op + "' at position: " + getPos());
            }
            FatherExpression right = parseMD();
            left = op == '+' ? new CheckedAdd(left, right) : new CheckedSubtract(left, right);
            skipWhitespace();
        }
        ch = getChar();
        if (eof() || ch == '*' || ch == '/' || ch == ')' || ch == '}' || ch == ']' || ch == 'a' || ch == 'p' )
            return left;
        throw new OperationException("Incorrect operation after operand '" + left.toString() + "' at position: " + getPos());
    }

    private FatherExpression parseMD() {
        FatherExpression left = parseN();
        skipWhitespace();
        char ch;
        while ((testChar('*') || testChar('/')) && !eof()) {
            char op = takeChar();
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
        if (eof() || ch == '+' || ch == '-' || ch == 'a' || ch == 'p' || ch == ')' || ch == '}' || ch == ']')
            return left;
        throw new OperationException("Incorrect operation after operand '" + left.toString() + "' at position: " + getPos());
    }

    private FatherExpression parseN() {
        skipWhitespace();
        char ch = getChar();
        if (eof() || ch == '*' || ch == '+' || ch == '/') {
            throw new OperandException("No operand at position '" + getPos() + "' before operation '" + ch + "'");
        } else if (ch == ')' || ch == '}' || ch == ']') {
            throw new BracketException("Invalid bracket sequence, no opening bracket. Wrong position bracket at: " + getPos());
        }
        if (testChar('(') || testChar('[') || testChar('{')) {
            char expectedClosedBracket = bracketPairs.get(takeChar());
            if (take(expectedClosedBracket)) {
                throw new OperandException("No operand at position " + getPos());
            }
            FatherExpression res = parseG();
            skipWhitespace();
            if (!take(expectedClosedBracket)) {
                throw new BracketException(
                        "Invalid bracket sequence, expected '" + expectedClosedBracket + "' at position: " + getPos()
                );
            }
            return res;
        }

        boolean hasMin = take('-');

        if (hasMin && !between('0', '9')) {
            return new CheckedNegate(parseN());
        }

        if (testWord("sqrt")) {
            takeWord();
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
        String var = takeWord();
        if (var.isEmpty()) {
            throw new BracketException("Invalid bracket sequence, no opening bracket. Wrong position bracket at: " + getPos());
        }
        if (!variables.contains(var)) {
            throw new VariableException("Wrong variable name");
        }
        return var;
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

//    private void takeId(final StringBuilder sb) {
//        while (Character.isLetter(getChar())) {
//            sb.append(takeChar());
//        }
//    }

    private void takeDigits(final StringBuilder sb) {
        while (between('0', '9')) {
            sb.append(takeChar());
        }
    }

    private void skipWhitespace() {
        while (Character.isWhitespace(getChar())) {
            takeChar();
        }

    }
}
