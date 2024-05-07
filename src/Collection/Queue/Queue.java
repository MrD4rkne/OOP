package Collection.Queue;

public class Queue<T> implements IQueue<T> {

    Node<T> head;
    Node<T> tail;
    int size;
    
    public Queue() {
        head = null;
        tail = null;
        size = 0;
    }
    
    @Override
    public void enqueue(T elem) {
        Node<T> newNode = new Node<T>(elem);
        if (head == null) {
            head = newNode;
        } else {
            newNode.previous=tail;
            tail.next = newNode;
        }
        tail = newNode;
        size++;
    }

    @Override
    public T dequeue() {
        if (head == null) {
            throw new IllegalStateException("Queue is empty");
        }
        T data = head.data;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        else{
            head.previous= null;
        }
        size--;
        return data;
    }

    @Override
    public T peek() {
        if (head == null) {
            throw new IllegalStateException("Queue is empty");
        }
        return head.data;
    }

    @Override
    public boolean isEmpty() {
        return size()==0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }

    private static class Node<T> {
        private final T data;
        private Node<T> next;
        private Node<T> previous;

        public Node(T data) {
            this.data = data;
            this.next = null;
            this.previous = null;
        }
    }
}
