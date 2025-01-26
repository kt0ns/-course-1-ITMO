package Reverse;

import java.io.IOException;
import MyScanner.MySSScanner;

public class Reverse {
    public static void main(String[] args) throws IOException{
        MySSScanner scanner = new MySSScanner(System.in);
        int[] numbers = new int[8];
        int[] indexes = new int[8];
        int line_index = 1;
        int number_index = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            MySSScanner num = new MySSScanner(line);
            if (!line.trim().isEmpty()) {
                while (num.hasNextInt()) {
                    int i = num.nextInt();
                    numbers[number_index] = i;
                    indexes[number_index] = line_index;
                    number_index++;
                    if (number_index >= numbers.length) {
                        indexes = resizeArrayOfIndexes(indexes);
                        numbers = resizeArrayOfNumbers(numbers);
                    }
                }
                line_index++;
            } else {
                numbers[number_index] = Integer.MAX_VALUE - 5;
                indexes[number_index] = line_index;
                number_index++;
                line_index++;
                if (number_index >= numbers.length) {
                    indexes = resizeArrayOfIndexes(indexes);
                    numbers = resizeArrayOfNumbers(numbers);
                }
            }
        }
        print(numbers, indexes);
    }

    public static int[] resizeArrayOfNumbers(int[] original) {
        int[] temp = new int[(int) (original.length * 2.7)];
        System.arraycopy(original, 0, temp, 0, original.length);
        return temp;
    }

    public static int[] resizeArrayOfIndexes(int[] original) {
        int[] temp = new int[(int) (original.length * 2.7)];
        System.arraycopy(original, 0, temp, 0, original.length);
        return temp;
    }

    public static void print(int[] numbers, int[] indexes) {
        for (int i = numbers.length - 1; i > 0; i--) {
            if (indexes[i] != 0) {
                if (numbers[i] != (Integer.MAX_VALUE - 5)) {
                    if (indexes[i] == indexes[i - 1]) {
                        System.out.print(numbers[i]);
                        System.out.print(" ");
                    } else System.out.println(numbers[i]);
                } else System.out.println();
            }
        }
        if (numbers[0] != Integer.MAX_VALUE - 5) {
            System.out.println(numbers[0]);
        } else System.out.println();
    }
}