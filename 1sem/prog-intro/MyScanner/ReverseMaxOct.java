package MyScanner;

import java.io.IOException;
import java.util.Arrays;
import java.lang.Math;


public class ReverseMaxOct {
    public static void main(String[] args) throws IOException {
        int posInLine = 0;
        int posInRow;
        int[] cntOfNumInLine = new int[1];
        int[] maxInLine = new int[1];
        int[] maxOfRow = new int[1];
        Arrays.fill(maxInLine, Integer.MIN_VALUE);
        Arrays.fill(maxOfRow, Integer.MIN_VALUE);
        try (MySSScanner scan = new MySSScanner(System.in)) {
            while (scan.hasNextLine()) {
                posInRow = 0;
                String line = scan.nextLine();
                if (!line.isEmpty()) {
                    try (MySSScanner num = new MySSScanner(line)) {
                        while (num.hasNext()) {
                            String s = num.next();
                            int i = Integer.parseUnsignedInt(s, 8);

                            if (i > maxOfRow[posInRow]) {
                                maxOfRow[posInRow] = i;
                            }

                            if (i > maxInLine[posInLine]) {
                                maxInLine[posInLine] = i;
                            }

                            posInRow++;

                            if (maxOfRow.length == posInRow) {
                                maxOfRow = resizeArr(maxOfRow);
                            }

                        }

                        cntOfNumInLine[posInLine] = posInRow;
                        posInLine++;

                        if (cntOfNumInLine.length == posInLine) {
                            cntOfNumInLine = resizeArr(cntOfNumInLine);
                            maxInLine = resizeArr(maxInLine);
                        }

                    } catch (IOException e) {
                        System.err.println(e);
                    }
                } else {
                    cntOfNumInLine[posInLine] = -1;
                    posInLine++;
                    if (cntOfNumInLine.length == posInLine) {
                        cntOfNumInLine = resizeArr(cntOfNumInLine);
                        maxInLine = resizeArr(maxInLine);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        print(cntOfNumInLine, maxInLine, maxOfRow);
    }

    public static int[] resizeArr(int[] arr) {
        int[] newArr = new int[arr.length * 2];
        Arrays.fill(newArr, Integer.MIN_VALUE);
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        return newArr;
    }

    public static void print(int[] cntOfNumInLine, int[] maxOfLine, int[] maxOfRow) {
        for (int i = 0; i < cntOfNumInLine.length; i++) {
            if (cntOfNumInLine[i] != Integer.MIN_VALUE) {
                for (int j = 0; j < cntOfNumInLine[i]; j++) {
                    System.out.print(Integer.toOctalString(Math.max(maxOfLine[i], maxOfRow[j])) + " ");
                }
                System.out.println();
            } else break;
        }
    }
}
