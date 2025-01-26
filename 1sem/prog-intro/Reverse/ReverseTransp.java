package Reverse;

import java.io.IOException;

public class ReverseTransp {
    public static void main(String[] args) throws IOException {
        MySSScanner scanner = new MySSScanner(System.in);
        int[] numbers = new int[32];
        int[] lineLenght = new int[32];
        int lineIndex = 0;
        int countOfNumbersInLine;
        int number_index = 0;
        while (scanner.hasNextLine()) {
            countOfNumbersInLine = 0;
            String line = scanner.nextLine();
            MySSScanner num = new MySSScanner(line);
            while (num.hasNextInt()) {
                countOfNumbersInLine++;
                int i = num.nextInt();
                numbers[number_index] = i;
                number_index++;
                if (number_index >= numbers.length) {
                    numbers = resizeArray(numbers);
                }
            }
            lineLenght[lineIndex] = countOfNumbersInLine;
            lineIndex++;
            if (lineIndex >= lineLenght.length) {
                lineLenght = resizeArray(lineLenght);
            }
        }

        numbers = cutArray(numbers, lineLenght);
        lineLenght = deleteZero(lineLenght, lineIndex);
        transposeAndPrint(numbers, lineLenght);
    }



    public static void transposeAndPrint(int[] numbers, int[] lineSizes) {
        int maxLen = 0;
        for (int len : lineSizes) {
            if (len > maxLen) {
                maxLen = len;
            }
        }
        int pos;
        for (int j = 0; j < maxLen; j++) {
            pos = j;
            for (int lineSize : lineSizes) {
                if (j < lineSize) {
                    if (pos < numbers.length) {
                        if (numbers[pos] % 2 != 0) {
                            System.out.print(numbers[pos] + " ");
                        }
                    }
                }
                pos += lineSize;
            }
            System.out.println();
        }
    }


    public static int[] cutArray(int[] numbers, int[] lenLines) {
        int len = 0;
        for (int i : lenLines) {
            len += i;
        }
        int[] temp = new int[len];
        System.arraycopy(numbers, 0, temp, 0, len);
        return temp;
    }
    public static int[] deleteZero(int[] original, int len) {
        int[] t = new int[len];
        System.arraycopy(original, 0, t,0, len);
        return t;
    }

    public static int[] resizeArray(int[] original) {
        int[] temp = new int[original.length * 2];
        System.arraycopy(original, 0, temp, 0, original.length);
        return temp;
    }
}