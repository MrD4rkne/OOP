package Simulation.Core;

import Collection.IMyList;
import Collection.MyArrayList;

class Heap<T extends Comparable<T>> {
    private final IMyList<T> heap;

    public Heap() {
        heap = new MyArrayList<T>();
    }

    public void insert(T element) {
        heap.add(element);
        int index = heap.size() - 1;
        heapifyUp(index);
    }

    public T peekMin() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }

    public T popMin() {
        T min = peekMin();
        heap.set(0, heap.get(heap.size() - 1));
        heap.removeAt(heap.size() - 1);
        heapifyDown(0);
        return min;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    private void heapifyDown(int index) {
        int leftChildIdx = 2 * index + 1;
        int rightChildIdx = 2 * index + 2;
        int smallest = index;

        if (leftChildIdx < heap.size() && heap.get(leftChildIdx).compareTo(heap.get(smallest)) < 0) {
            smallest = leftChildIdx;
        }

        if (rightChildIdx < heap.size() && heap.get(rightChildIdx).compareTo(heap.get(smallest)) < 0) {
            smallest = rightChildIdx;
        }

        if (smallest != index) {
            swap(index, smallest);
            heapifyDown(smallest);
        }
    }

    private void heapifyUp(int index) {
        int parentIndex = (index - 1) / 2;
        while (index > 0 && heap.get(index).compareTo(heap.get(parentIndex)) < 0) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
}
