package Collection;

public class Queue<T> implements IQueue<T> {

    private Node<T> head;
    private Node<T> tail;
    private int size;
    
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
        return size==0;
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
    
    private class Node<T> {
        private final T data;
        private Node<T> next;
        
        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
}
