package queue;

import java.util.Arrays;
import java.util.NoSuchElementException;


// Model: circular sequence queue[0]..queue[n]
// Inv: n >= 0 && forall i=first..last: a[i % length] != null
// Let: length = queue.length
// Let: first = queue.length + last - size + 1
// Let: immutable(last, size): forall i=first..last: a'[i % length] = a[i % length]
// Let: nothingChanged(): last` = last && size` = size && length` = length && immutable(last, size)

public class ArrayQueueADT<T> {
    @SuppressWarnings("unchecked")
    private T[] queue = (T[]) new Object[32];
    private int last = 0;
    private int size = 0;

    // Pre: size > 0 && 0 <= index < size && value != null
    // Post: queue[last - index] = value && last` = last && first` = first && size` = size && length` = length && immutable(first, size - index - 1) && immutable(size - index + 1, last)
    public static <R> void set(ArrayQueueADT<R> queueADT, int ind, R value) {
        if (ArrayQueueADT.isEmpty(queueADT)) {
            throw new NoSuchElementException("Queue is empty");
        }

        if (ind > ArrayQueueADT.size(queueADT)) {
            throw new IllegalArgumentException("So big index for this queue");
        }

        int index = (queueADT.last - ind - 1 + queueADT.queue.length) % queueADT.queue.length;
        queueADT.queue[index] = value;
    }

    // Pre: true
    // Post: R = "[ element_0, ... , element_(n-1) ]" && nothingChanged()
    public static <R> String toStr(ArrayQueueADT<R> queueADT) {
        StringBuilder sb = new StringBuilder("[");

        if (isEmpty(queueADT)) {
            return "[]";
        }
        for (int i = 0; i < size(queueADT) - 1; i++) {
            sb.append(String.valueOf(queueADT.queue[(queueADT.queue.length + queueADT.last - queueADT.size + i) % queueADT.queue.length])).append(", ");
        }
        sb.append(String.valueOf(queueADT.queue[(queueADT.queue.length + queueADT.last - queueADT.size + size(queueADT) - 1) % queueADT.queue.length]));
        return sb.append("]").toString();
    }

    // Pre: size > 0 && && 0 <= index < size
    // Post: queue[last - index] = newVal && last` = last && size` = size && length` = length && immutable(first, last)
    public static <R> R get(ArrayQueueADT<R> queueADT, int index) {
        if (isEmpty(queueADT)) {
            throw new NoSuchElementException("Queue is empty");
        }

        return queueADT.queue[(queueADT.last - index - 1 + queueADT.queue.length) % queueADT.queue.length];
    }

    // Pre: input != null
    // Post: size` = size + 1 && last` = last + 1 && immutable(first, last)
    public static <R> void enqueue(ArrayQueueADT<R> queueADT, R val) {
        if (size(queueADT) < queueADT.queue.length) {
            // last = (last + 1) % queue.length;
            queueADT.queue[queueADT.last] = val;
            if ((queueADT.last + 1) < queueADT.queue.length) {
                queueADT.last++;
            } else {
                queueADT.last = 0;
            }
            queueADT.size++;
        } else {
            resize(queueADT);
            enqueue(queueADT, val);
        }
    }

    // Pre: size > 0
    // Post: R = queue[first] && nothingChanged()
    public static <R> R element(ArrayQueueADT<R> queueADT) {
        if (isEmpty(queueADT)) {
            throw new NoSuchElementException("Queue is empty");
        }

        return queueADT.queue[(queueADT.queue.length + queueADT.last - queueADT.size) % queueADT.queue.length];
    }

    // size > 0
    // R = queue[first] && size` = size - 1 && last` = last && immutable(first`, last`)
    public static <R> R dequeue(ArrayQueueADT<R> queueADT) {
        if (isEmpty(queueADT)) {
            throw new NoSuchElementException("Queue is empty");
        }
        R val = queueADT.queue[(queueADT.queue.length + queueADT.last - queueADT.size) % queueADT.queue.length];
        queueADT.queue[(queueADT.queue.length + queueADT.last - queueADT.size) % queueADT.queue.length] = null;
        queueADT.size--;
        return val;
    }

    // Pre: true
    // Post: last` = size` = 0
    public static <R> void clear(ArrayQueueADT<R> queueADT) {
        final Object[] es = queueADT.queue;
        for (int to = size(queueADT), i = queueADT.last = queueADT.size = 0; i < to; i++)
            es[i] = null;
    }

    // Pre: true
    // Post: R = (size == 0) && nothingChanged()
    public static <R> boolean isEmpty(ArrayQueueADT<R> queueADT) {
        return queueADT.size == 0;
    }

    // Pre: true
    // Post: R = size && nothingChanged()
    public static <R> int size(ArrayQueueADT<R> queueADT) {
        return queueADT.size;
    }

    // Pre: true
    // Post: length` = 2 * length && size` = size && last` = length && queue`[0..length] = queue[first..last]
    private static <R> void resize(ArrayQueueADT<R> queueADT) {
        R[] newQueue = Arrays.copyOf(queueADT.queue, 2 * queueADT.queue.length);
        int newSize = size(queueADT);
        for (int i = 0; i < newSize; i++) {
            newQueue[i] = queueADT.queue[(queueADT.queue.length + queueADT.last - queueADT.size + i) % queueADT.queue.length];
        }
        queueADT.queue = newQueue;
        queueADT.last = newSize;
    }
}
