package search;

public class BinarySearch3435 {

    /* P:
    Через терминал подаётся непустой список аргументов размера n (n ≥ 1), представляющий собой числа.

    - Следующие n аргументов — элементы массива numbers, такие что существует такой индекс F, что для любых i, j (i < j < F) и l, k (F < l < k < n) выполняется:

    numbers[i] < numbers[j] и numbers[l] < numbers[k]

    то есть массив упорядочен строго по возрастанию и имеет циклический сдвиг.
    */

    /* Q:
    void System.out.println(result)
    Значение result выбирается
    такое, что

    result = min(numbers)
     */
    public static void main(String[] args) {
        // a = new int[args.length - 1]
        int[] numbers = new int[args.length];
        // numbers = a && numbers.length = args.length - 1
        for (int i = 0; i < args.length; i++) {
            numbers[i] = Integer.parseInt(args[i]);
        }

        System.out.println(shiftSearchRecursive(numbers, 0, args.length - 1));

    }

    private static int shiftSearchIterative(int[] arr) {
        int length = arr.length;
        int left = 0;
        int right = length - 1;
        int start = 0;
        // left == 0 && right >= 0 && arr.length > 0
        if (arr[0] > arr[length - 1]) {
            // arr[0] > arr[lenght - 1]
            while (left < right) { // I: left <= right
                int m = left + (right - left) / 2;
                // left < right && left <= mid < right
                if (arr[m] > arr[right]) {
                    left = m + 1;
                    // arr[m] > arr[right] && left` = m + 1 && right` = right
                } else {
                    right = m;
                    // arr[m] > arr[right] && right` = m && left` = left
                }
                // left` <= right`
            }
            // left == right && arr[left] = min(arr)
            start = left;
            // start = left
        }

        return arr[start];
    }

    private static int shiftSearchRecursive(int[] arr, int left, int right) { // I: left < right
        // arr.length != 0 && left <= right
        if (left == right) {
            // left == right && arr[left] = min(arr)
            return arr[left]; // arr[left] = min(arr)
        }
        int mid = left + (right - left) / 2;
        // left <= mid < right
        if (arr[mid] > arr[right]) {
            // left <= mid < right && arr[mid] > arr[right]
            return shiftSearchRecursive(arr, mid + 1, right); // left` = mid + 1 && right` = right && left` <= right`
        } else {
            // left <= mid < right && arr[mid] <= arr[right]
            return shiftSearchRecursive(arr, left, mid); // left` = left && right` = mid && left` <= right`
        }
    }

}
