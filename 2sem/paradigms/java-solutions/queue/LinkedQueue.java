package queue;

// :NOTE: тесты не проходят
public class LinkedQueue<R> extends AbstractQueue<R> {
    private Node<R> head;

    protected void insertValue(R val) {
        Node<R> newNode = new Node<>(val, null);

        if (head == null) {
            head = newNode;
            head.next = head;
        } else {
            newNode.next = head.next;
            head.next = newNode;
            head = newNode;
        }
    }

    protected R getFirstValueInQueue() {
        return head.next.value;
    }

    protected void changeFirstElementInQueue() {
        Node<R> node = head.next;

        if (node == head) {
            head = null;
        } else {
            head.next = node.next;
        }
    }

    protected StringBuilder fillElementsInSB() {
        StringBuilder sb = new StringBuilder();
        Node<R> val = head.next;
        for (int i = 0; i < size - 1; i++) {
            sb.append(val.value).append(", ");
            val = val.next;
        }
        sb.append(val.value);
        return sb;
    }

    public void clear() {
        head = null;
        size = 0;
    }

    private static class Node<T> {
        private T value;
        private Node<T> next;

        public Node(T value, Node<T> next) {
            assert value != null;

            this.value = value;
            this.next = next;
        }
    }

}
