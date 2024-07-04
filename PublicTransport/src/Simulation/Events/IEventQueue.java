package Simulation.Events;

public interface IEventQueue {

    /**
     * Adds an event to the queue.
     * @param  event  the event to add
     */
    void add(Event event);
    
    /**
     * Removes and returns the event at the front of the queue.
     * @return the event at the front of the queue
     * @throws IllegalStateException if the queue is empty
     */
    
    Event pop();
    
    /**
     * Checks if the queue is empty.
     * @return true if the queue is empty, false otherwise
     */
    
    boolean isEmpty();
    
    /**
     * Returns the number of events in the queue.
     * @return the number of events in the queue
     */
    
    int size();
    
    /**
     * Returns the event at the front of the queue without removing it.
     * @return the event at the front of the queue
     * @throws IllegalStateException if the queue is empty
     */
    
    Event peek();
    
    /**
     * Removes all events from the queue.
     */
    
    void clear();
}
