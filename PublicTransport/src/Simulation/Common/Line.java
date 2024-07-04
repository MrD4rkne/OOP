package Simulation.Common;

import Collection.MyList.IMyList;
import Collection.MyList.MyArrayList;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.Vehicle;

public abstract class Line  {
    protected final int id;
    protected final Stop[] stops;
    protected final int[] segmentDurations;
    protected final int vehiclesCount;
    protected final IMyList<Vehicle> vehicles;
    
    public Line(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        this.id = id;
        this.vehicles = new MyArrayList<>(vehicleCount);
        this.vehiclesCount=vehicleCount;
        this.stops = new Stop[stops.length];
        System.arraycopy(stops, 0, this.stops, 0, stops.length);
        this.segmentDurations = new int[segmentDurations.length];
        System.arraycopy(segmentDurations, 0, this.segmentDurations, 0, segmentDurations.length);
    }
    
    public int getId() {
        return id;
    }
    
    public int getVehicleCount() {
        return vehiclesCount;
    }
    
    public abstract int prepareVehicles(IEventQueue eventQueue, ILogReporter eventReporter, int currentTime, int vehicleStartingNo);

    public abstract void notifyEndOfRoute(Vehicle vehicle, IEventQueue eventQueue, ILogReporter eventReporter, int currentTime);

    protected int getLoopStopDuration(){
        return segmentDurations[segmentDurations.length-1];
    }
    
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Line ").append(id).append(":\n");
        sb.append("Stops: ");
        for(Stop stop: stops){
            sb.append(stop.getName()).append(" ");
        }
        sb.append("\n");
        sb.append("Segment durations: ");
        for(int duration: segmentDurations){
            sb.append(duration).append(" ");
        }
        sb.append("\n");
        sb.append("Vehicles count: ").append(vehiclesCount).append("\n");
        return sb.toString();
    }
}
