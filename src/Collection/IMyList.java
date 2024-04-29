package Collection;

public interface IMyList<T> extends Iterable<T> {
    void add(T elem);

    void addRange(T[] elems);
    
    void remove(T elem);
    
    void removeAt(int index);
    
    T get(int index);
    
    boolean contains(T elem);
    
    boolean isEmpty();
    
    int size();

    void set(int index, T elem);

    IMyList<T> sub(int index);

    IMyList<T> sub(int index, int length);

    T[] subArray(int index);

    T[] subArray(int index, int length);
}
