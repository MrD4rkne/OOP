package Simulation.Common;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;
import Simulation.Event.VehicleArrivesAtStopEvent;

public abstract class Vehicle {
    private final int sideNo;
    private final int capacity;
    protected Line line;
    protected IMyList<Passenger> passengers;
    protected int currentStopIndex;
    protected IMyList<Segment> route;
    
    public Vehicle(Line line, int sideNo, int capacity) {
        this.line=line;
        this.sideNo = sideNo;
        this.capacity=capacity;
        this.passengers= new MyArrayList<Passenger>(capacity);
        this.route= null;
    }

    public int getSideNo(){
        return sideNo;
    }

    public boolean hasSpace() {
        return passengers.size()<capacity;
    }

    public void board(Passenger passenger){
        if(!hasSpace())
            throw new IllegalStateException("No space left");
        passengers.add(passenger);
    }
    
    public Stop getFinalStop(){
        return route.get(route.size()-1).getStop();
    }
    
    public void startRoute(Segment[] route, IEventQueue eventQueue, IEventReporter eventReporter, int time){
        this.route=new MyArrayList<Segment>(route);
        currentStopIndex=0;
        eventQueue.add(new VehicleArrivesAtStopEvent(time, this, 0));
    }

    public Stop[] getStopsLeft() {
        Stop[] stopsLeft = new Stop[getStopsLeftCount()];
        for (int i = 0; i < stopsLeft.length; i++) {
            stopsLeft[i] = route.get(i+stopsLeft.length).getStop();
        }
        return stopsLeft;
    }

    public void stop(int stopIndex,IEventQueue eventQueue, IEventReporter eventReporter, int time) {
        this.currentStopIndex=stopIndex;
        Stop currentStop = route.get(stopIndex).getStop();
        getOffPassengers(currentStop, time);
        currentStop.tryBoardPassengers(this, eventQueue,time);
        scheduleNextAction(eventQueue, eventReporter, time);
    }

    protected boolean isOnFinalStop(){
        return currentStopIndex==route.size()-1;
    }
    
    private int getStopsLeftCount() {
        return route.size()-currentStopIndex;
    }
    
    private void scheduleNextAction(IEventQueue eventQueue, IEventReporter eventReporter, int currentTime){
        if(isOnFinalStop()) {
            line.notifyEndOfRoute(this,eventQueue, eventReporter, currentTime);
        }
        else {
            Segment currentSegment = route.get(currentStopIndex);
            int nextStopIndex = currentSegment.getIndex()+1;
            int timeOfNextStop = currentTime + currentSegment.getTimeToNextStop();
            eventQueue.add(new VehicleArrivesAtStopEvent(timeOfNextStop, this, nextStopIndex));
        }
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