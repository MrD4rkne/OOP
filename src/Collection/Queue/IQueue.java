package Collection.Queue;

import Collection.IMyList;

public interface IQueue<T> {
    void enqueue(T elem);
    
    T dequeue();
    
    T peek();
    
    boolean isEmpty();
    
    int size();
    
    void clear();
}
