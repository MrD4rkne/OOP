package Simulation;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;

public abstract class Vehicle {
    protected Line line;
    protected final int sideNo;
    protected final int lineVehicleNo;
    protected final int capacity;
    protected IMyList<Passenger> passengers;
    
    public Vehicle(Line line, int sideNo, int lineVehicleNo,int capacity) {
        this.line=line;
        this.lineVehicleNo=lineVehicleNo;
        this.sideNo = sideNo;
        this.capacity=capacity;
        passengers= new MyArrayList<Passenger>(capacity);
    }

    public int getSideNo(){
        return sideNo;
    }

    public int getLineVehicleNo(){
        return lineVehicleNo;
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
        return line.getStopsLeft(this);
    }

    public void stop(Segment segment,IEventQueue eventQueue, IEventReporter eventReporter, int time) {
        Stop currentStop = segment.getStop();
        getOffPassengers(currentStop, time);
        line.reportStop(segment, eventReporter, time);
        line.trySchedule(this, eventQueue, time);
        currentStop.tryBoardPassengers(this, eventQueue,time);
    }
    
    private void getOffPassengers(Stop currentStop, int time) {
        for (int i = 0; i < passengers.size(); i++) {
            if(!currentStop.hasSpace())
                return;
            Passenger passenger = passengers.get(i);
            if (passenger.getDesiredStop() == currentStop) {
                if(passenger.tryEnterStop(currentStop,time)) {
                    passengers.removeAt(i);
                    i--;
                }
            }
        }
    }
}