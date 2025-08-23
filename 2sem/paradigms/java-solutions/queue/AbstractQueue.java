package queue;

import java.util.Arrays;
import java.util.NoSuchElementException;

public abstract class AbstractQueue<T> implements Queue<T> {
    protected int size = 0;
    private T[] temp;

    public String toStr() {
        StringBuilder sb = new StringBuilder();
        if (size != 0) sb.append(fillElementsInSB());
        return "[" + sb + "]";
    }


    public T get(int index) {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Index is wrong");
        }

        copyQueue();
        for (int i = 0; i < temp.length; i++) {
            temp[i] = dequeue();
        }

        for (T t : temp) {
            enqueue(t);
        }

        return temp[temp.length - index - 1];
    }

    public void set(int index, T value) {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        if (index < 0 || index >= size) {
            throw new IllegalArgumentException("Index is wrong");
        }

        copyQueue();
        for (int i = 0; i < temp.length; i++) {
            temp[i] = dequeue();
        }

        temp[temp.length - index - 1] = value;
        for (T t : temp) {
            enqueue(t);
        }
    }

    public void enqueue(T val) {
        insertValue(val);
        size++;
    }

    public T element() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }

        return getFirstValueInQueue();
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        T val = getFirstValueInQueue();
        changeFirstElementInQueue();
        size--;
        return val;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public abstract void clear();

    // Pre: true
    // Post: last` = last + 1 && immutable(first, last)
    protected abstract void insertValue(T val);

    // Pre: size > 0
    // Post: R = queue[first] && nothingChanged()
    protected abstract T getFirstValueInQueue();

    // Pre: size > 0
    // Post: queue[first] = null && last` = last && size` = size && length` = length && immutable(first + 1, last)
    protected abstract void changeFirstElementInQueue();

    // Pre: true
    // Post: "element_0, ..., element_n"
    protected abstract StringBuilder fillElementsInSB();

    @SuppressWarnings("unchecked")
    private void copyQueue() {
        temp = (T[]) new Object[size];
    }
}
