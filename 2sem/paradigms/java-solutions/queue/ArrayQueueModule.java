package queue;

import java.util.NoSuchElementException;

// :NOTE: в очереди n+1 элемент

// Model: queue[0]..queue[n]
// Inv: n >= 0 && forall i=first..last: queue[i] != null

/* Let:
        first = (length + last - size) % length;
        length = queue.length;
        immutable(first, last): forall i=first..last: queue'[i] = queue[i]
                               (elements from head to tail did not change);
        nothingChanged(): last` = last && size` = size && length` = length && immutable(first, last)
                                                                  (the queue status has not changed);
*/

public class ArrayQueueModule {
    private static Object[] queue = new Object[32];
    private static int last = 0;
    private static int size = 0;

    // Pre: size > 0 && 0 <= index < size && value != null
    // Post: queue[last - index] = value && last` = last && first` = first && size` = size && length` = length && immutable(first, size - index - 1) && immutable(size - index + 1, last)
    public static void set(int index, Object value) {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        if (index > size() - 1) {
            throw new IllegalArgumentException("So big index for this queue");
        }

        if (index < 0) {
            throw new IllegalArgumentException("Not a right ind");
        }

        queue[(last - index - 1 + queue.length) % queue.length] = value;
    }

    // Pre: true
    // Post: R = "[ element_0, ... , element_(n-1) ]" && nothingChanged()
    public static String toStr() {
        StringBuilder sb = new StringBuilder("[");

        if (isEmpty()) {
            return "[]";
        }
        for (int i = 0; i < size() - 1; i++) {
            sb.append(queue[(queue.length + last - size + i) % queue.length]).append(", ");
        }
        sb.append(queue[(queue.length + last - size + size() - 1) % queue.length]);
        return sb.append("]").toString();
    }

    // Pre: size > 0 && && 0 <= index < size
    // Post: queue[last - index] = newVal && last` = last && size` = size && length` = length && immutable(first, last)
    public static Object get(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        if (index < 0 || index >= size()) {
            throw new IllegalArgumentException("Index is wrong");
        }

        return queue[(last - index - 1 + queue.length) % queue.length];
    }

    // Pre: input != null
    // Post: size` = size + 1 && last` = last + 1 && immutable(first, last)
    public static void enqueue(Object val) {
        if (size() < queue.length) {
            // last = (last + 1) % queue.length;
            queue[last] = val;
            if ((last + 1) < queue.length) {
                last++;
            } else {
                last = 0;
            }
            size++;
        } else {
            resize();
            enqueue(val);
        }
    }

    // Pre: size > 0
    // Post: R = queue[first] && nothingChanged()
    public static Object element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        return queue[(last - size + queue.length) % queue.length];
    }

    // size > 0
    // R = queue[first] && size` = size - 1 && last` = last && immutable(first`, last`)
    public static Object dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        Object val = queue[(queue.length + last - size) % queue.length];
        queue[(queue.length + last - size) % queue.length] = null;
        size--;
        return val;
    }

    // Pre: true
    // Post: last` = size` = 0
    public static void clear() {
        final Object[] es = queue;
        for (int to = size(), i = last = size = 0; i < to; i++)
            es[i] = null;
    }


    // Pre: true
    // Post: R = (size == 0) && nothingChanged()
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pre: true
    // Post: R = size && nothingChanged()
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: length` = 2 * length && size` = size && last` = length && queue`[0..length] = queue[first..last]
    private static void resize() {
        Object[] newQueue = new Object[2 * queue.length];
        int newSize = size();
        for (int i = 0; i < newSize; i++) {
            newQueue[i] = queue[(queue.length + last - size + i) % queue.length];
        }
        queue = newQueue;
        last = newSize;
    }
}
