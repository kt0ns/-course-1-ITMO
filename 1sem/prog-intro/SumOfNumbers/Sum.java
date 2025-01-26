package SumOfNumbers;

public class Sum {
    public static void main(String[] args) {
        int sum = 0;
        int temp = -1;
        for (String numbers : args) {
            for (int i = 0; i <= numbers.length(); i++) {
                if (i < numbers.length() && !Character.isWhitespace(numbers.charAt(i)) && temp == -1) {
                    temp = i;
                } else if ((i == numbers.length() || Character.isWhitespace(numbers.charAt(i))) && temp != -1) {
                    sum += Integer.parseInt(numbers.substring(temp, i));
                    temp = -1;
                }
            }
        }
        System.out.println(sum);
    }
}