package Simulation.Vehicles;

import Collection.IMyList;
import Collection.IQueue;
import Collection.MyArrayList;
import Collection.Queue;
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
    protected IQueue<Passenger> passengers;
    protected int currentStopIndex;
    protected IMyList<Segment> route;
    
    public Vehicle(Line line, int sideNo, int capacity) {
        this.line=line;
        this.sideNo = sideNo;
        this.capacity=capacity;
        this.passengers= new Queue<Passenger>();
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
        passengers.enqueue(passenger);
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
            stopsLeft[i] = route.get(currentStopIndex + i + 1).getStop();
        }
        return stopsLeft;
    }
    
    public void stop(int stopIndex, IEventQueue eventQueue, ILogReporter eventReporter, int time) {
        this.currentStopIndex=stopIndex;
        eventReporter.log(new VehicleArriveAtStopLog(time, this, route.get(stopIndex).getStop()));
        
        Stop currentStop = route.get(stopIndex).getStop();
        int passengersGetOffCount = getOffPassengers(eventQueue, eventReporter, currentStop, time);
        int passengersBoardCount = 0;
        if(!isOnFinalStop()){
            passengersBoardCount = boardPassengers(eventQueue, eventReporter, currentStop, time);
        }
        
        eventReporter.log(new VehicleLeavesStopLog(time, this, currentStop, passengersBoardCount, passengersGetOffCount));
        scheduleNextAction(eventQueue, eventReporter, time);
    }
    
    public void enterLoop(IEventQueue eventQueue, ILogReporter eventReporter, int time, Stop stop){
        eventReporter.log(new VehicleEntersLoopLog(time, this,stop));
    }
    
    public void endDay(IEventQueue eventQueue, ILogReporter eventReporter, int time){
        eventReporter.log(new VehicleEndsDayLog(time, this));
        kickOutPassengers(eventQueue, eventReporter,time);
    }

    protected boolean isOnFinalStop(){
        return currentStopIndex==route.size()-1;
    }
    
    protected abstract String getName();

    public String toString(){
        return String.format("%s no. %d of line %d",
                getName(), getSideNo(), getLine().getId());
    }
    
    private int getStopsLeftCount() {
        return route.size()-currentStopIndex -1;
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
    
    private int getOffPassengers(IEventQueue eventQueue, ILogReporter logReporter,Stop currentStop, int time) {
        int passengersGetOffCount = 0;
        for (int i = 0; i < passengers.size(); i++) {
            if(!currentStop.hasSpace())
                break;
            Passenger passenger = passengers.dequeue();
            if (passenger.getDesiredStop() != currentStop) {
                continue;
            }
            passenger.leaveVehicle(eventQueue,logReporter, currentStop, time);
            passengersGetOffCount++;
        }
        return passengersGetOffCount;
    }
    
    private void kickOutPassengers(IEventQueue eventQueue, ILogReporter eventReporter, int time) {
        for (int i = 0; i < passengers.size(); i++) {
            Passenger passenger = passengers.dequeue();
            passenger.forceGetOutOfVehicle(eventQueue, eventReporter, this,time);
            i--;
        }
    }
    
    private int boardPassengers(IEventQueue eventQueue, ILogReporter eventReporter,Stop stop, int time){
        int passengersBoardCount = 0;
        while(stop.hasPassengers() && hasSpaceLeft()){
            Passenger passenger = stop.passengerLeave();
            passenger.enter(this,time, eventReporter);
            passengersBoardCount++;
        }
        return passengersBoardCount;
    }
}