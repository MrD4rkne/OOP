package Simulation.Core;

import Simulation.Events.Event;
import Simulation.Events.IEventQueue;

class EventQueue implements IEventQueue {
    private final Heap<Event> heap;

    public EventQueue() {
        heap = new Heap<>();
    }

    @Override
    public void add(Event event) {
        heap.insert(event);
    }

    @Override
    public Event pop() {
        if(heap.isEmpty()){
            throw new IllegalStateException("Event queue is empty");
        }
        return heap.popMin();
    }

    @Override
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    @Override
    public int size() {
        return heap.size();
    }

    @Override
    public Event peek() {
        return heap.peekMin();
    }

    @Override
    public void clear() {
        while (!heap.isEmpty()) {
            heap.popMin();
        }
    }
}