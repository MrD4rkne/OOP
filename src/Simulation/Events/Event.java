package Simulation.Events;

import Simulation.Logs.ILogReporter;
import Simulation.Logs.IStatistic;

public abstract class Event implements Comparable<Event>{
    protected final int time;
    
    public Event(int time) {
        this.time = time;
    }
    
    public int getTime() {
        return time;
    }
    
    public abstract void process(IEventQueue queue, ILogReporter reporter);
    
    @Override
    public int compareTo(Event other) {
        return Integer.compare(time, other.time);
    }
}
