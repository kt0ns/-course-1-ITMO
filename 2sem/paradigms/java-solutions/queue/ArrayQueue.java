package queue;


import java.util.Arrays;

public class ArrayQueue<R> extends AbstractQueue<R> {
    @SuppressWarnings("unchecked")
    private R[] queue = (R[]) new Object[8];
    private int last = 0;

    protected void insertValue(R val) {
        if (size() < queue.length) {
            queue[last] = val;
            if ((last + 1) < queue.length) {
                last++;
            } else {
                last = 0;
            }
        } else {
            resize();
            size--;
            enqueue(val);
        }
    }

    protected R getFirstValueInQueue() {
        return queue[(queue.length + last - size) % queue.length];
    }

    protected void changeFirstElementInQueue() {
        queue[(queue.length + last - size) % queue.length] = null;
    }

    protected StringBuilder fillElementsInSB() {
        StringBuilder sb = new StringBuilder();
        if (size > 0) {
            for (int i = 0; i < size() - 1; i++) {
                sb.append(queue[(queue.length + last - size + i) % queue.length]).append(", ");
            }
            sb.append(queue[(queue.length + last - size + size() - 1) % queue.length]);
        }
        return sb;
    }

    public void clear() {
        final Object[] es = queue;
        for (int to = size(), i = last = size = 0; i < to; i++)
            es[i] = null;
    }

    // Pre: true
    // Post: length` = 2 * length && size` = size && last` = length && queue`[0..length] = queue[(first..last) % length]
    private void resize() {
        R[] newQueue = Arrays.copyOf(queue, 2 * queue.length);
        int newSize = size();
        for (int i = 0; i < newSize; i++) {
            newQueue[i] = queue[(queue.length + last - size + i) % queue.length];
        }
        queue = newQueue;
        last = newSize;
    }
}
