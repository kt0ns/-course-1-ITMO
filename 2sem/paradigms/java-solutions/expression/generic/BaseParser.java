package expression.generic;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BaseParser {
    private static final char END = '\0';
    private CharSource source;
    private char ch = 0xffff;

    protected void setSource(CharSource source) {
        this.source = source;
        take();
    }

    protected char take() {
        final char result = ch;
        ch = source.hasNext() ? source.next() : END;
        return result;
    }


    protected boolean test(final char expected) {
        return ch == expected;
    }

    protected boolean test(final char... expected) {
        for (char operators : expected) {
            if (ch == operators) {
                return true;
            }
        }
        return false;
    }

    protected boolean test(final String expected) {
        for (int i = 0; i < expected.length(); i++) {
            if (source.saveNext() != expected.charAt(i)) {
                source.getPointerBack();
                return false;
            }
        }
        source.getPointerBack();
        return true;
    }


    protected boolean take(final char expected) {
        if (test(expected)) {
            take();
            return true;
        }
        return false;
    }

    protected boolean take(final char... expected) {
        for (char i : expected) {
            if (test(expected)) {
                take();
                return true;
            }
        }
        return false;
    }

    protected char getChar() {
        return ch;
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
