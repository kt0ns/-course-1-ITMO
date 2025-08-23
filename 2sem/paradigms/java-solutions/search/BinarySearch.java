package search;

public class BinarySearch {
    /* P:
    Через терминал подаётся непустой список аргументов размера n (n ≥ 1), представляющий собой числа.

    - Первый аргумент — искомое число x.
    - Следующие n - 1 аргументов — элементы массива numbers, такие что для любых i, j (i < j < n - 1) выполняется:

    numbers[i] > numbers[j]

    то есть массив упорядочен по убыванию.
    */

    /* Q:
    void System.out.println(result)
    Значение result выбирается из множества индексов

    A = { ind | numbers[ind] <= x }
    такое, что

    result = min(A) && 0 <= result < numbers.length.
     */
    public static void main(String[] args) {
        //  a = Integer.parseInt(args[0])
        int x = Integer.parseInt(args[0]);
        // x = a

        // a = new int[args.length - 1]
        int[] numbers = new int[args.length - 1];
        // numbers = a && numbers.length = args.length - 1
        for (int i = 1; i < args.length; i++) {
            numbers[i - 1] = Integer.parseInt(args[i]);
        }

//        System.out.println(iterativeSearch(numbers, x));
        System.out.println(recursiveSearch(0, numbers.length, x, numbers));

    }

    /* P:
        Вызван main и переданы int[] arr :
        для любого натурального i и j: 0 <= i < arr.length 0 <= j < arr.length && i < j && arr[i] <= arr[j],
        int x
     */
    /* Q:
        R = min(A) из множества A, для которого выполняется :
        (x >= arr[a] && 0 <= a < arr.length && arr.length != 0)
        || (arr.length == 0 && A = {0} && R == 0)
    */
    private static int iterativeSearch(int[] arr, int x) { // I: left <= mid < right && left < right - 1 && right <= arr.length
        if (arr.length == 0) {
            // arr.length == 0
            return 0;
        }
        // arr.length > 0
        int left = 0;
        int right = arr.length;
        int mid = 0;
        // left == 0 && right >= 0 && mid == 0 && arr.length > 0
        while (left < right - 1) { // I: left + 1 < right
            // left < right - 1 && left >= 0
            mid = left + (right - left) / 2;
            // left < right - 1 && left >= 0 && mid = left + (right - left) / 2
            if (arr[mid] <= x) {
                // left <= mid < right && right <= arr.length && arr[mid] <= x && arr.length != 0
                right = mid;
                // right` = mid
            } else {
                // left <= mid < right && right <= arr.length && arr[mid] > x && arr.length != 0
                left = mid;
                // left` = mid
            }
            // left <= mid && right >= mid
        }
        // left - 1 == right && arr.length != 0 && left < arr.length
        if (arr[left] > x) {
            // left - 1 == right && arr.length != 0 && left < arr.length && arr[left] > x
            return right;
        } else {
            // left - 1 == right && arr.length != 0 && left < arr.length && arr[left] <= x
            return left;
        }
    }


    /* P:
        Вызван main и переданы int[] arr :
        для любого натурального i и j: 0 <= i < arr.length 0 <= j < arr.length && i < j && arr[i] <= arr[j],
        int x
    */
    /* Q:
        R = min(A) из множества A, для которого выполняется :
        (x >= arr[a] && 0 <= a < arr.length && arr.length != 0)
        || (arr.length == 0 && A = {0} && R == 0)
    */
    private static int recursiveSearch(int left, int right, int x, int[] arr) {
        if (arr.length == 0) {
            // arr.length == 0
            return 0;
        }
        // left` = left && right` = right && left < right
        int mid = left + (right - left) / 2;

        if (left == right - 1) {
            // left - 1 == right && arr.length != 0 && left < arr.length
            if (arr[left] > x) {
                // left - 1 == right && arr.length != 0 && left < arr.length && arr[left] > x
                return right;
            } else {
                // left - 1 == right && arr.length != 0 && left < arr.length && arr[left] <= x
                return left;
            }
        }

        if (arr[mid] <= x) {
            // arr[mid] <= x && left <= mid < right
            return recursiveSearch(left, mid, x, arr); // left` = left && right` = mid && left` < right`
        } else {
            // arr[mid] > x && left <= mid < right
            return recursiveSearch(mid, right, x, arr); // left` = mid  && right` = right && left` < right`
        }
    }


}
