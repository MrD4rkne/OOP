package Simulation.Core;

import Collection.IMyList;
import Collection.MyArrayList;

import java.math.BigInteger;

class Heap<T extends Comparable<T>> {
    private final IMyList<Node<T>> heap;

    private BigInteger index;

    public Heap() {
        heap = new MyArrayList<Node<T>>();
        index = BigInteger.ZERO;
    }

    public void insert(T element) {
        heap.add(new Node<T>(element,index));
        index = index.add(BigInteger.valueOf(1));
        int index = heap.size() - 1;
        heapifyUp(index);
    }

    public T peekMin() {
        if (heap.isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0).getVal();
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
        Node<T> temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    private class Node<T extends Comparable<T>> implements Comparable<Node<T>>{
        private final T val;
        private final BigInteger no;

        public Node(T val, BigInteger no){
            this.val=val;
            this.no=no;
        }

        public T getVal(){
            return val;
        }

        public BigInteger getNo(){
            return no;
        }

        @Override
        public int compareTo(Node<T> o) {
            int valCompare = getVal().compareTo(o.getVal());
            if(valCompare != 0){
                return valCompare;
            }
            return getNo().compareTo(o.getNo());
        }
    }
}
