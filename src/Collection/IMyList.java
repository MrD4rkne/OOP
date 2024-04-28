package Collection;

public interface IMyList<T> extends Iterable<T> {
    void add(T elem);
    
    void remove(T elem);
    
    void removeAt(int index);
    
    T get(int index);
    
    boolean contains(T elem);
    
    boolean isEmpty();
    
    int size();

    void set(int index, T elem);
}
