package Collection.MyList;

import java.lang.reflect.Array;
import java.util.Iterator;

public class MyArrayList<T> implements IMyList<T> {
    private static final int DEFAULT_CAPACITY = 2;
    private static final int GROWTH_FACTOR = 2;
    
    private T[] array;
    private int size;

    public MyArrayList(T[] elems){
        this(Math.max(elems.length, DEFAULT_CAPACITY));
        addRange(elems);
    }

    public MyArrayList(){
        this(DEFAULT_CAPACITY);
    }
    
    @SuppressWarnings("unchecked")
    public MyArrayList(int capacity){
        if(capacity < 0){
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        array = (T[])new Object[capacity];
        size = 0;
    }

    @Override
    public void add(T elem) {
        if(size == array.length){
            resize(size+1);
        }
        array[size++] = elem;
    }

    @Override
    public void addRange(T[] elems) {
        resize(size + elems.length);
        System.arraycopy(elems,0,array,size,elems.length);
        size+=elems.length;
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
    public void clear() {
        array =(T[]) Array.newInstance(array.getClass().getComponentType(), DEFAULT_CAPACITY);
        size=0;
    }

    @Override
    public Iterator<T> iterator() {
        return new MyListIterator<>(array, size);
    }

    private void resize(int minDesiredSize){
        int newCapacity = array.length;
        while(newCapacity < minDesiredSize){
            newCapacity*=GROWTH_FACTOR;
        }
        T[] newArray = (T[]) Array.newInstance(array.getClass().getComponentType(), newCapacity);
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }
}
