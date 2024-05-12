package Collection.Queue;

public interface IQueue<T> {

    /**
     * Adds the specified element to the end of the queue.
     * @param  elem  the element to add
     */
    void enqueue(T elem);
    
    /**
     * Removes and returns the element at the front of the queue.
     * @return the element at the front of the queue
     * @throws IllegalStateException if the queue is empty
     */
    
    T dequeue();
    
    /**
     * Returns the element at the front of the queue without removing it.
     * @return the element at the front of the queue
     * @throws IllegalStateException if the queue is empty
     */
    
    T peek();
    
    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise
     */
    
    boolean isEmpty();
    
    /**
     * Returns the number of elements in the queue.
     * @return the number of elements in the queue
     */
    
    int size();
    
    /**
     * Removes all elements from the queue.
     */
    
    void clear();
}
