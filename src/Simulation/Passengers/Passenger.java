package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Core.IRandomProvider;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.Vehicle;

public class Passenger {
    private final int id;
    private final Stop primaryStop;
    private Stop desiredStop;
    private int timeOfLastAction;
    private Stop currentStop;
    private Vehicle vehicle;
    
    public Passenger(int id, Stop primaryStop) {
        this.id = id;
        this.primaryStop = primaryStop;
        this.desiredStop =null;
        this.timeOfLastAction =-1;
    }
    
    public int getId() {
        return id;
    }
    
    public Stop getDesiredStop() {
        return desiredStop;
    }
    
    public Stop getPrimaryStop() {
        return primaryStop;
    }
    
    public void enter(Stop stop, int time, ILogReporter reporter) {
        stop.putPassenger(this);
        reporter.log(new PassengerEnterStopLog(time,this,stop));
        currentStop = stop;
        timeOfLastAction = time;
        desiredStop=null;
        vehicle=null;
    }
    
    public void enter(Vehicle vehicle, int time, ILogReporter reporter, IRandomProvider randomProvider) {
        Stop[] stops = vehicle.getStopsLeft();
        desiredStop = stops[randomProvider.next(0, stops.length)];
        this.vehicle = vehicle;
        vehicle.board(this);
        reporter.log(new PassengerBoardVehicleLog(time,this,vehicle, desiredStop, timeFromLastAction(time)));
        this.currentStop=null;
        this.timeOfLastAction =time;
    }
    
    public void reset(){
        desiredStop=null;
        timeOfLastAction =-1;
        currentStop=null;
        vehicle=null;
    }

    public void leaveVehicle(IEventQueue eventQueue, ILogReporter reporter,Stop stop, int time){
        if(vehicle==null)
            throw new IllegalStateException("Passenger is not in vehicle");
        reporter.log(new PassengerLeaveVehicleOnDesiredStopLog(time,this,vehicle,stop, timeFromLastAction(time)));
        enter(stop, time, reporter);
    }
    
    public void abortWaitForVehicle(ILogReporter reporter, int time){
        if(currentStop == null)
            return;
        reporter.log(new PassengerAbortedWaitForVehicle(time,this, timeFromLastAction(time)));
        this.currentStop=null;
        this.timeOfLastAction = time;
    }

    public void forceGetOutOfVehicle(IEventQueue eventQueue, ILogReporter reporter, Vehicle vehicle,int time) {
        if(this.vehicle!=vehicle)
            throw new IllegalStateException("Passenger is not in vehicle");
        reporter.log(new PassengerLeftVehicleDueToEndOfDay(time, this, vehicle, timeFromLastAction(time)));
        this.vehicle = null;
        this.timeOfLastAction=time;
    }

    @Override
    public String toString(){
        return "no. "+ id;
    }

    private int timeFromLastAction(int currentTime){
        return currentTime-this.timeOfLastAction;
    }
}
