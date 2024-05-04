package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.Vehicle;
import Simulation.Core.RandomNumberGenerator;

public class Passenger {
    private final int id;
    private final Stop primaryStop;
    private Stop desiredStop;
    private int drivesTaken;
    private int timeOfStartedWaiting;
    private int totalTimeWaiting;
    
    public Passenger(int id, Stop primaryStop) {
        this.id = id;
        this.primaryStop = primaryStop;
        this.drivesTaken=0;
        this.desiredStop =null;
        this.totalTimeWaiting = 0;
        this.timeOfStartedWaiting=-1;
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
    
    public int getTotalTimeWaiting() {
        return totalTimeWaiting;
    }
    
    public boolean tryEnterStop(Stop stop, int time) {
        if(stop.hasSpace()){
            stop.enter(this);
            timeOfStartedWaiting = time;
            desiredStop=null;
            return true;
        }
        return false;
    }
    
    public void board(Vehicle vehicle, int time, ILogReporter reporter) {
        Stop[] stops = vehicle.getStopsLeft();
        desiredStop = stops[RandomNumberGenerator.random(0, stops.length)];
        vehicle.board(this);
        reporter.log(new PassengerBoardVehicle(time,this,vehicle, desiredStop));
        drivesTaken++;
        totalTimeWaiting+= time - timeOfStartedWaiting;
        this.timeOfStartedWaiting=-1;
    }
    
    public void abortWaiting(int time) {
        totalTimeWaiting+= time - timeOfStartedWaiting;
        this.timeOfStartedWaiting=-1;
    }
    
    public void reset(){
        drivesTaken=0;
        desiredStop=null;
        totalTimeWaiting=0;
        timeOfStartedWaiting=-1;
    }

    public void forceGetOutOfVehicle(IEventQueue eventQueue, ILogReporter reporter, Vehicle vehicle,int time) {
        reporter.log(new PassengerLeftVehicleDueToEndOfDay(time, this, vehicle));
    }

    @Override
    public String toString(){
        return "no. "+ id;
    }
}
