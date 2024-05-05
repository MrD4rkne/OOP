package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Logs.IStatistic;
import Simulation.Vehicles.Vehicle;
import Simulation.Core.RandomNumberGenerator;

public class Passenger {
    private final int id;
    private final Stop primaryStop;
    private Stop desiredStop;
    private int drivesTaken;
    private int timeOfLastAction;
    
    public Passenger(int id, Stop primaryStop) {
        this.id = id;
        this.primaryStop = primaryStop;
        this.drivesTaken=0;
        this.desiredStop =null;
        this.timeOfLastAction =-1;
    }
    
    public int getId() {
        return id;
    }
    
    public Stop getDesiredStop() {
        return desiredStop;
    }
    
    public int getDrivesTaken() {
        return drivesTaken;
    }
    
    public Stop getPrimaryStop() {
        return primaryStop;
    }
    
    public boolean tryEnterStop(Stop stop, int time) {
        if(stop.hasSpace()){
            stop.enter(this);
            timeOfLastAction = time;
            desiredStop=null;
            return true;
        }
        return false;
    }
    
    public void board(Vehicle vehicle, int time, ILogReporter reporter) {
        Stop[] stops = vehicle.getStopsLeft();
        desiredStop = stops[RandomNumberGenerator.random(0, stops.length)];
        vehicle.board(this);
        reporter.log(new PassengerBoardVehicleLog(time,this,vehicle, desiredStop, timeFromLastAction(time)));
        drivesTaken++;
        this.timeOfLastAction =time;
    }
    
    public void reset(){
        drivesTaken=0;
        desiredStop=null;
        timeOfLastAction =-1;
    }

    public boolean leaveVehicle(IEventQueue eventQueue, ILogReporter reporter, Vehicle vehicle, Stop stop, int time){
        boolean didLeave=tryEnterStop(stop,time);
        if(!didLeave)
            return false;
        reporter.log(new PassengerLeaveVehicleOnDesiredStopLog(time,this,vehicle,stop, timeFromLastAction(time)));
        return true;
    }

    public void forceGetOutOfVehicle(IEventQueue eventQueue, ILogReporter reporter, Vehicle vehicle,int time) {
        reporter.log(new PassengerLeftVehicleDueToEndOfDay(time, this, vehicle));
    }

    @Override
    public String toString(){
        return "no. "+ id;
    }

    private int timeFromLastAction(int currentTime){
        return currentTime-this.timeOfLastAction;
    }
}
