package queue;

// Model: queue[0]..queue[n-1]
// Inv: n >= 0 && forall i=1..n-1: queue[i] != null

/* Let:
        Let first = 0
        let last = n-1
        Let size = n
        immutable(first, last): forall i=first..last: queue'[i] = queue[i]
                               (elements from head to tail did not change);
        nothingChanged(): first` = first && last` = last && size` = size && immutable(first, last)
                                                                  (the queue status has not changed);
*/

public interface Queue<T> {

    // Pre: true
    // Post: R = "[ element_0, ... , element_(n-1) ]" && nothingChanged()
    String toStr();

    // Pre: size > 0 && && 0 <= index < size
    // Post: queue[last - index] = newVal && last` = last && size` = size && length` = length && immutable(first, last)
    T get(int index);

    // Pre: size > 0 && 0 <= index < size && value != null
    // Post: queue[last - index] = value && last` = last && first` = first && size` = size && length` = length && immutable(first, size - index - 1) && immutable(size - index + 1, last)
    void set(int ind, T value);

    // Pre: input != null
    // Post: size` = size + 1 && last` = last + 1 && queue[last`] = input && immutable(first, last)
    void enqueue(T val);

    // Pre: size > 0
    // Post: R = queue[first] && nothingChanged()
    T element();

    // size > 0
    // R = queue[first] && size` = size - 1 && last` = last && immutable(first`, last`)
    T dequeue();

    // Pre: true
    // Post: R = size && nothingChanged()
    int size();

    // Pre: true
    // Post: R = (size == 0) && nothingChanged()
    boolean isEmpty();

    // Pre: true
    // Post: last` = size` = 0
    void clear();
}
