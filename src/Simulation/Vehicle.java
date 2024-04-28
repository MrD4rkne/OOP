package Simulation;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;

import java.security.PrivateKey;

public abstract class Vehicle {
    protected Line line;
    protected final int sideNo;
    protected final int capacity;
    protected int currentStopIndex;
    protected Direction direction;
    protected IMyList<Passenger> passengers;
    
    public Vehicle(Line line, int sideNo, int capacity) {
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

    public void stop(int currentStopIndex,IEventQueue eventQueue, IEventReporter eventReporter, int time) {
        this.currentStopIndex = currentStopIndex;
        Stop currentStop = line.getStop(currentStopIndex);
        getOffPassengers(currentStop, time);
        currentStop.tryBoardPassengers(this, eventQueue,time);
        
        int nextStopIndex = -1;
        int timeToNextStop = -1;
        if(line.isLastStop(currentStopIndex, direction)) {
            // TODO: report loop
            direction = line.switchDirection(direction);
            nextStopIndex = line.getStartIndex(direction);
            timeToNextStop = line.getLoopStopDuration();
        }
        else{
            nextStopIndex = line.getNextStopIndex(currentStopIndex, direction);
            timeToNextStop = line.getTimeToNextStopDuration(currentStopIndex, direction);
        }
    }
    
    private void getOffPassengers(Stop stop, int time) {
        for (int i = 0; i < passengers.size(); i++) {
            if(!stop.hasSpace())
                return;
            Passenger passenger = passengers.get(i);
            if (passenger.getDesiredStop() == line.getStop(currentStopIndex)) {
                passenger.enterStop(stop,time);
                passengers.removeAt(i);
                i--;
            }
        }
    }
}