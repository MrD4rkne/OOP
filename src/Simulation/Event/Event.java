package Simulation.Event;

public abstract class Event implements Comparable<Event>{
    protected final int time;
    
    public Event(int time) {
        this.time = time;
    }
    
    public int getTime() {
        return time;
    }
    
    public abstract void process(IEventQueue queue, IEventReporter reporter);
    
    @Override
    public int compareTo(Event other) {
        return Integer.compare(time, other.time);
    }
    
    public abstract String toString();
}
