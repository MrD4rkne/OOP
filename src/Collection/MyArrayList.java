package Collection;

import java.lang.reflect.Array;
import java.util.Iterator;

public class MyArrayList<T> implements IMyList<T> {
    private static final int DEFAULT_CAPACITY = 2;
    private static final int GROWTH_FACTOR = 2;
    
    private T[] array;
    private int size;

    public MyArrayList(){
        this(DEFAULT_CAPACITY);
    }
    
    @SuppressWarnings("unchecked")
    public MyArrayList(int capacity){
        assert(capacity> 0);
        array = (T[])new Object[capacity];
        size = 0;
    }

    @Override
    public void add(T elem) {
        if(size == array.length){
            @SuppressWarnings("unchecked")
            T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), array.length * GROWTH_FACTOR);
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }
        array[size++] = elem;
    }

    @Override
    public void remove(T elem) {
        for(int i = 0; i < size; i++){
            if(array[i].equals(elem)){
                System.arraycopy(array, i+1, array, i, size-i-1);
                size--;
                return;
            }
        }
    }

    @Override
    public void removeAt(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, index+1, array, index, size-index-1);
        size--;
    }

    @Override
    public T get(int index) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        return array[index];
    }

    @Override
    public boolean contains(T elem) {
        for(int i = 0; i<size; i++){
            if(array[i].equals(elem)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void set(int index, T elem) {
        if(index < 0 || index >= size){
            throw new IndexOutOfBoundsException();
        }
        array[index] = elem;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyIterator<T>(array, size);
    }
}
