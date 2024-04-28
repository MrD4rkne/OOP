package Simulation.Event;

public interface IEventQueue {
    void add(Event event);
    
    Event pop();
    
    boolean isEmpty();
    
    int size();
    
    Event peek();
    
    void clear();
}
