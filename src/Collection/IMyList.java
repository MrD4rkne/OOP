package Collection;

public interface IMyList<T> extends Iterable<T> {
    void add(T elem);

    void addRange(T[] elems);
    
    void removeAt(int index);
    
    T get(int index);
    
    boolean contains(T elem);
    
    boolean isEmpty();
    
    int size();

    void set(int index, T elem);

    void clear();
}
