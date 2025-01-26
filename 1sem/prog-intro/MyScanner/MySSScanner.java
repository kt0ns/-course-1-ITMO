package MyScanner;

import java.io.*;
import java.util.NoSuchElementException;

public class MySSScanner implements AutoCloseable {
    private final String lineSep = System.lineSeparator();
    private final Reader reader;
    private final char[] buffer = new char[64];
    private int pos = -1;
    private int cntChar = -1;
    private String tempWord = "";
    private String tempTrueWord = "";
    private String tempLine = "";
    private boolean hasNextToken = false;

    private enum Token {WORD, LINE, TRUE_WORD}

    public MySSScanner(String line) {
        this.reader = new StringReader(line);
    }

    public MySSScanner(File f) throws FileNotFoundException {
        this.reader = new FileReader(f);
    }

    public MySSScanner(InputStream source) throws IOException {
        this.reader = new InputStreamReader(source);
    }

    private String readT(Token type) throws IOException {
        StringBuilder token = new StringBuilder();
        updateBuffer();
        while (cntChar != -1) {

            switch (type) {
                case WORD:
                    if (!Character.isWhitespace(buffer[pos])) {
                        token.append(buffer[pos]);

                    } else if (!token.isEmpty()) {
                        pos++;
                        return token.toString();
                    }
                    break;
                case LINE:
                    token.append(buffer[pos]);
                    if (token.charAt(token.length() - 1) == lineSep.charAt(lineSep.length() - 1)
                            && token.length() >= lineSep.length()
                            && token.substring(token.length() - lineSep.length()).equals(lineSep)) {
                        pos++;
                        return token.substring(0, token.length() - lineSep.length());
                    }
                    break;
                case TRUE_WORD:
                    if (Character.isLetter(buffer[pos]) || buffer[pos] == '\'' || Character.DASH_PUNCTUATION == Character.getType(buffer[pos])) {
                        token.append(buffer[pos]);
                    } else if (!token.isEmpty()) {
                        pos++;
                        return token.toString();
                    }
                    break;
                default:
                    throw new IOException("Unknown token");
            }
            pos++;
            updateBuffer();
        }

        return switch (type) {
            case WORD, LINE, TRUE_WORD -> token.toString();
            default -> throw new NoSuchElementException();
        };
    }

    public boolean hasNext() throws IOException {
        if (!hasNextToken) {
            String word = readT(Token.WORD);
            hasNextToken = !word.isEmpty();
            tempWord = word;
        }
        return hasNextToken;
    }

    public String next() throws IOException {
        if (hasNext()) {
            hasNextToken = false;
            return tempWord;
        } else throw new NoSuchElementException();
    }

    public boolean hasNextLine() throws IOException {
        if (!hasNextToken) {
            String line = readT(Token.LINE);
            hasNextToken = !(cntChar == -1) || !line.isEmpty();
            tempLine = line;
        }
        return hasNextToken;
    }

    public String nextLine() throws IOException {
        if (hasNextLine()) {
            hasNextToken = false;
            return tempLine;
        } else throw new NoSuchElementException();
    }

    public int nextInt() throws IOException {
        if (hasNextInt()) {
            hasNextToken = false;
            return Integer.parseInt(tempWord);
        } else throw new NoSuchElementException();
    }

    public boolean hasNextInt() throws IOException {
        if (!hasNextToken) {
            String word = readT(Token.WORD);
            tempWord = word;
            hasNextToken = !word.isEmpty();
        }
        return isInt(tempWord);
    }

    private boolean isInt(String word) {
        if (!word.isEmpty() && (Character.isDigit(word.charAt(0)) || (word.charAt(0) == '-' && word.length() > 1))) {
            for (int i = 1; i < word.length(); i++) {
                if (!Character.isDigit(word.charAt(i))) {
                    return false;
                }
            }
        }
        return !word.isEmpty();
    }

    private boolean isIntRad(String word, int radix) {
        if (!word.isEmpty() && ((Character.isDigit(word.charAt(0)) && (int) word.charAt(0) <= radix) || (word.charAt(0) == '-' && word.length() > 1))) {
            for (int i = 1; i < word.length(); i++) {
                if (!Character.isDigit(word.charAt(i)) && (int) word.charAt(i) <= radix) {
                    return false;
                }
            }
        }
        return !word.isEmpty();
    }

    public String nextWord() throws IOException {
        if (hasNextWord()) {
            hasNextToken = false;
            return tempTrueWord;
        } else throw new NoSuchElementException();
    }

    public boolean hasNextWord() throws IOException {
        if (!hasNextToken) {
            String word = readT(Token.TRUE_WORD);
            hasNextToken = !word.isEmpty();
            tempTrueWord = word;
        }
        return hasNextToken;
    }

    public void close() throws IOException {
        reader.close();
    }

    private void updateBuffer() throws IOException {
        if (pos == cntChar) {
            pos = 0;
            cntChar = reader.read(buffer);
        }
    }
}
