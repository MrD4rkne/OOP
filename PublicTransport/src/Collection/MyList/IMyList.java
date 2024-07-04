package Collection.MyList;

public interface IMyList<T> extends Iterable<T> {
    /**
     * Add an element to the list.
     * @param  elem the element to add
     */
    void add(T elem);
    
    /**
     * Add a range of elements to the list.
     * @param  elems the elements to add
     */

    void addRange(T[] elems);
    
    /**
     * Remove an element from the list.
     * @param  index the index of element to remove
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    
    void removeAt(int index);
    
    /**
     * Get an element from the list.
     * @param  index the index of element to get
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    
    T get(int index);
    
    /**
     * Check if the list contains an element.
     * @param  elem the element to check
     * @return true if the list contains the element, false otherwise
     */
    
    boolean contains(T elem);
    
    /**
     * Check if the list is empty.
     * @return true if the list is empty, false otherwise
     */
    
    boolean isEmpty();
    
    /**
     * Get the number of elements in the list.
     * @return the number of elements in the list
     */
    
    int size();
    
    /**
     * Set an element in the list.
     * @param  index the index of element to set
     * @param  elem the element to set
     * @throws IndexOutOfBoundsException if the index is out of range
     */

    void set(int index, T elem);
    
    /**
     * Clear the list.
     */

    void clear();
}
