package Collection;

public interface IQueue<T> {
    void enqueue(T elem);
    
    T dequeue();
    
    T peek();
    
    boolean isEmpty();
    
    int size();
    
    void clear();
}
