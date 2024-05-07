package Collection;

import java.util.Iterator;

public class MyListIterator<T> implements Iterator<T> {
    private final T[] array;
    private int index;
    private final int size;

    public MyListIterator(T[] array, int size) {
        this.array = array;
        this.size = size;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < size;
    }

    @Override
    public T next() {
        return array[index++];
    }
}
