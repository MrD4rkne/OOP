package Simulation.Vehicles;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Common.Line;
import Simulation.Common.Segment;
import Simulation.Common.Stop;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Passengers.Passenger;

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
    
    public Line getLine(){
        return line;
    }

    public final int getSideNo(){
        return sideNo;
    }

    public boolean hasSpaceLeft() {
        return passengers.size()<capacity;
    }

    public void board(Passenger passenger){
        if(!hasSpaceLeft())
            throw new IllegalStateException("No space left");
        passengers.add(passenger);
    }

    public Stop getFirstStop(){
        return route.get(0).getStop();
    }
    
    public Stop getFinalStop(){
        return route.get(route.size()-1).getStop();
    }
    
    public void startRoute(Segment[] route, IEventQueue eventQueue, ILogReporter eventReporter, int time){
        this.route=new MyArrayList<Segment>(route);
        currentStopIndex=0;
        eventReporter.log(new VehicleStartRouteLog(time, this, getFirstStop(),getFinalStop()));
        eventQueue.add(new VehicleArrivesAtStopEvent(time, this, 0));
    }

    public Stop[] getStopsLeft() {
        Stop[] stopsLeft = new Stop[getStopsLeftCount()];
        for (int i = 0; i < stopsLeft.length; i++) {
            stopsLeft[i] = route.get(i+stopsLeft.length).getStop();
        }
        return stopsLeft;
    }

    public void stop(int stopIndex, IEventQueue eventQueue, ILogReporter eventReporter, int time) {
        this.currentStopIndex=stopIndex;
        eventReporter.log(new VehicleArriveAtStopLog(time, this, route.get(stopIndex).getStop()));
        
        Stop currentStop = route.get(stopIndex).getStop();
        int passengersGetOffCount = getOffPassengers(currentStop, time);
        int passengersBoardCount = currentStop.tryBoardPassengers(this, eventQueue,time);
        
        eventReporter.log(new VehicleLeavesStopLog(time, this, currentStop, passengersBoardCount, passengersGetOffCount));
        scheduleNextAction(eventQueue, eventReporter, time);
    }
    
    public String toString(){
        return String.format("%s no. %d of line %d",
                getName(), getSideNo(), getLine().getId());
    }
    
    public void enterLoop(IEventQueue eventQueue, ILogReporter eventReporter, int time, Stop stop){
        eventReporter.log(new VehicleEntersLoopLog(time, this,stop));
    }
    
    public void endDay(IEventQueue eventQueue, ILogReporter eventReporter, int time){
        eventReporter.log(new VehicleEndsDayLog(time, this));
        kickOutPassengers(time, eventQueue, eventReporter);
    }

    protected boolean isOnFinalStop(){
        return currentStopIndex==route.size()-1;
    }
    
    protected abstract String getName();
    
    private int getStopsLeftCount() {
        return route.size()-currentStopIndex;
    }
    
    private void scheduleNextAction(IEventQueue eventQueue, ILogReporter eventReporter, int currentTime){
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
    
    private int getOffPassengers(Stop currentStop, int time) {
        int passengersGetOffCount = 0;
        for (int i = 0; i < passengers.size(); i++) {
            if(!currentStop.hasSpace())
                break;
            Passenger passenger = passengers.get(i);
            if (passenger.getDesiredStop() == currentStop) {
                if(passenger.tryEnterStop(currentStop,time)) {
                    passengers.removeAt(i);
                    i--;
                    passengersGetOffCount++;
                }
            }
        }
        return passengersGetOffCount;
    }
    
    private void kickOutPassengers(int time, IEventQueue eventQueue, ILogReporter eventReporter) {
        for (int i = 0; i < passengers.size(); i++) {
            Passenger passenger = passengers.get(i);
            passenger.forceGetOutOfVehicle(eventQueue, eventReporter, time);
            passengers.removeAt(i);
            i--;
        }
    }
}