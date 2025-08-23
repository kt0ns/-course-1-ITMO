package expression.exceptions;

public class BaseParser {
    private static final char END = '\0';
    private CharSource source;
    private char ch = 0xffff;

    protected void setSource(CharSource source) {
        this.source = source;
        takeChar();
    }

    protected char takeChar() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }

    protected String takeWord() {
        StringBuilder sb = new StringBuilder();
        ch = getChar();
        if (!Character.isLetter(ch)) {
            return "";
        }

        while (!eof() && Character.isLetter(ch)) {
            sb.append(ch);
            ch = source.checkNext() ? source.saveNext() : END;
//            source.movePTR();
        }

        source.movePOS();

        return sb.toString();
    }

    protected boolean testChar(final char expected) {
        return ch == expected;
    }

    protected boolean testWord(String expected) {
        int i = 0;
        source.movePTR();
        char c = ch;
        while (!eof() && i < expected.length() && c == expected.charAt(i)) {
            if (i == expected.length() - 1) {
                c = source.checkNext() ? source.saveNext() : END;
                if (Character.isWhitespace(c) || c == '-' || c == '(' || c == '{' || c == '[' || eof()) {
                    source.movePTR();
                    return true;
                }
                break;
            }
            c = source.checkNext() ? source.saveNext() : END;
            i++;
        }

        source.movePTR();
        return false;
    }

    protected boolean take(final char expected) {
        if (testChar(expected)) {
            takeChar();
            return true;
        }
        return false;
    }

    protected char getChar() {
        return ch;
    }

    protected int getPos() {
        return source.getPos();
    }

    protected void expect(final char expected) {
        if (!take(expected)) {
            throw error("Expected '" + expected + "', found '" + ch + "'");
        }
    }

    protected void expect(final String value) {
        for (final char c : value.toCharArray()) {
            expect(c);
        }
    }

    protected boolean eof() {
        return take(END);
    }

    protected IllegalArgumentException error(final String message) {
        return source.error(message);
    }

    protected boolean between(final char from, final char to) {
        return from <= ch && ch <= to;
    }
}
