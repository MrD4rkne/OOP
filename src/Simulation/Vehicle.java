package Simulation;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;

public abstract class Vehicle {
    protected Line line;
    protected final int sideNo;
    protected final int capacity;
    protected Segment currentSegment;
    protected Direction direction;
    protected IMyList<Passenger> passengers;
    
    public Vehicle(Line line, int sideNo, int capacity) {
        this.line=line;
        this.sideNo = sideNo;
        this.capacity=capacity;
        passengers= new MyArrayList<Passenger>(capacity);
        currentSegment=null;
    }

    public boolean hasSpace() {
        return passengers.size()<capacity;
    }

    public void board(Passenger passenger){
        if(!hasSpace())
            throw new IllegalStateException("No space left");
        passengers.add(passenger);
    }

    public Stop[] getStopsLeft() {
        return line.getStopsLeft(currentSegment, direction);
    }

    public void stop(Segment segment,IEventQueue eventQueue, IEventReporter eventReporter, int time) {
        this.currentSegment = segment;
        Stop currentStop = currentSegment.getStop();

        getOffPassengers(currentStop, time);
        currentStop.tryBoardPassengers(this, eventQueue,time);

        line.reportStop(segment, direction, eventReporter, time);
        line.trySchedule(this,segment, direction, eventQueue, time);
        this.currentSegment=null;
    }
    
    private void getOffPassengers(Stop stop, int time) {
        for (int i = 0; i < passengers.size(); i++) {
            if(!stop.hasSpace())
                return;
            Passenger passenger = passengers.get(i);
            if (passenger.getDesiredStop() == currentSegment.getStop()) {
                if(passenger.tryEnterStop(stop,time)) {
                    passengers.removeAt(i);
                    i--;
                }
            }
        }
    }
}