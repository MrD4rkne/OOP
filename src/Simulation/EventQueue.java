package Simulation;

import Simulation.Event.Event;
import Simulation.Event.IEventQueue;

class EventQueue implements IEventQueue {
    private final Heap<Event> heap;
    
    public EventQueue() {
        heap = new Heap<Event>();
    }
    
    @Override
    public void add(Event event) {
        heap.insert(event);
    }

    @Override
    public Event pop() {
       assert(!heap.isEmpty());
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
