package Simulation.Vehicles;

import Collection.MyList.IMyList;
import Collection.Queue.IQueue;
import Collection.MyList.MyArrayList;
import Collection.Queue.Queue;
import Simulation.Common.Line;
import Simulation.Common.Segment;
import Simulation.Common.Stop;
import Simulation.Core.IRandomProvider;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Passengers.Passenger;

public abstract class Vehicle {
    private final int sideNo;
    private final int capacity;
    private Stop firstStop;
    protected final Line line;
    protected IQueue<Passenger> passengers;
    protected int currentStopIndex;
    protected IMyList<Segment> route;
    
    public Vehicle(Line line, int sideNo, int capacity) {
        this.line=line;
        this.sideNo = sideNo;
        this.capacity=capacity;
        this.passengers= new Queue<>();
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

    public boolean isOnStartingStop(){
        return isOnStartingStop(route.get(currentStopIndex).getStop());
    }
    
    public boolean isOnStartingStop(Stop stop){
        if(firstStop==null)
            throw new IllegalStateException("Vehicle has not started route yet");
        return firstStop==stop;
    }

    public Stop getFirstStop(){
        return route.get(0).getStop();
    }
    
    public Stop getFinalStop(){
        return route.get(route.size()-1).getStop();
    }
    
    public void startRoute(Segment[] route, IEventQueue eventQueue, ILogReporter eventReporter, int time){
        if(firstStop== null)
            firstStop=route[0].getStop();
        this.route= new MyArrayList<>(route);
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
    
    public void stop(int stopIndex, IEventQueue eventQueue, ILogReporter eventReporter, IRandomProvider randomProvider, int time) {
        this.currentStopIndex=stopIndex;
        eventReporter.log(new VehicleArriveAtStopLog(time, this, route.get(stopIndex).getStop()));
        Stop currentStop = route.get(stopIndex).getStop();
        int passengersGetOffCount = getOffPassengers(eventQueue, eventReporter, currentStop, time);
        int passengersBoardCount = 0;
        if(!isOnFinalStop()){
            passengersBoardCount = boardPassengers(eventQueue, eventReporter, randomProvider,currentStop, time);
        }
        
        eventReporter.log(new VehicleLeavesStopLog(time, this, currentStop, passengersBoardCount, passengersGetOffCount));
        scheduleNextAction(eventQueue, eventReporter, time);
    }
    
    public void enterLoop(IEventQueue eventQueue, ILogReporter eventReporter, int time, Stop stop){
        eventReporter.log(new VehicleEntersLoopLog(time, this,stop));
    }
    
    public void endDay(IEventQueue eventQueue, ILogReporter eventReporter, int time){
        eventReporter.log(new VehicleEndsDayLog(time, this));
        kickOutPassengers(eventQueue, eventReporter,route.get(currentStopIndex).getStop(),time);
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
        IQueue<Passenger> passengersAfterStop = new Queue<>();
        while(!passengers.isEmpty()){
            if(!currentStop.hasSpace())
            {
                while(!passengers.isEmpty())
                    passengersAfterStop.enqueue(passengers.dequeue());
                break;
            }
            Passenger passenger = passengers.dequeue();
            if (passenger.getDesiredStop() != currentStop) {
                passengersAfterStop.enqueue(passenger);
                continue;
            }
            passenger.leaveVehicle(eventQueue,logReporter, currentStop, time);
            passengersGetOffCount++;
        }
        passengers = passengersAfterStop;
        return passengersGetOffCount;
    }
    
    private void kickOutPassengers(IEventQueue eventQueue, ILogReporter eventReporter, Stop stop, int time) {
        getOffPassengers(eventQueue, eventReporter, stop, time);
        while(!passengers.isEmpty()){
            Passenger passenger = passengers.dequeue();
            passenger.forceGetOutOfVehicle(eventQueue, eventReporter, this,time);
        }
    }
    
    private int boardPassengers(IEventQueue eventQueue, ILogReporter eventReporter, IRandomProvider randomProvider, Stop stop, int time){
        int passengersBoardCount = 0;
        while(stop.hasPassengers() && hasSpaceLeft()){
            Passenger passenger = stop.passengerLeave();
            passenger.enter(this,time, eventReporter,randomProvider);
            passengersBoardCount++;
        }
        return passengersBoardCount;
    }
}