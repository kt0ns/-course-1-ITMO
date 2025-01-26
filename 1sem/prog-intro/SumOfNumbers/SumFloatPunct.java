package SumOfNumbers;

public class SumFloatPunct {
    public static void main(String[] args) {
        float sum = 0;
        int temp = -1;
        for (String numbers : args) {
            for (int i = 0; i <= numbers.length(); i++) {
                if ((i < numbers.length()) && (!Character.isWhitespace(numbers.charAt(i)) &&
                        (Character.getType(numbers.charAt(i)) != Character.END_PUNCTUATION) &&
                        (Character.getType(numbers.charAt(i)) != Character.START_PUNCTUATION))
                        && (temp == -1)) {
                    temp = i;
                } else if ((i == numbers.length() ||
                        Character.isWhitespace(numbers.charAt(i)) ||
                        (Character.getType(numbers.charAt(i)) == Character.END_PUNCTUATION) ||
                        (Character.getType(numbers.charAt(i)) == Character.START_PUNCTUATION)) &&
                        temp != -1) {
                    sum += Float.parseFloat(numbers.substring(temp, i));
                    temp = -1;
                }
            }
        }
        System.out.println(sum);
    }
}
