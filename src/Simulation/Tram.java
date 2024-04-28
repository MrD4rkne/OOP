package Simulation;

import Collection.IMyList;
import Collection.MyArrayList;

public class Tram extends Vehicle {
    protected Line line;
    protected final int sideNo;
    protected final int capacity;
    protected int currentStopIndex;
    protected Direction direction;
    protected IMyList<Passenger> passengers;
    
    
    public Tram(Line line, int sideNo, int capacity) {
        this.line=line;
        this.sideNo = sideNo;
        this.capacity=capacity;
        passengers= new MyArrayList<Passenger>(capacity);
    }
    
    public boolean hasSpace() {
        return passengers.size()<capacity;
    }
    
    public int getTimeToStop(Stop stop, Direction direction) {
        return line.getTimeToStop(currentStopIndex, direction,stop);
    }
    
    public void board(Passenger passenger){
        if(!hasSpace())
            throw new IllegalStateException("No space left");
        passengers.add(passenger);
    }
    
    public Stop[] getStopsLeft() {
        return line.getStopsLeft(currentStopIndex, direction);
    }
    
     public void stop(){
        Stop currentStop = line.getStop(currentStopIndex);
        
     }
}
