package Simulation.Common;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.Vehicle;

public abstract class Line  {
    protected final int id;
    protected final Stop[] stops;
    protected final int[] segmentDurations;
    protected final IMyList<Vehicle> vehicles;
    protected final Segment[] vehiclesSegment;
    
    public Line(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        this.id = id;
        this.vehicles = new MyArrayList<Vehicle>(vehicleCount);
        this.vehiclesSegment = new Segment[vehicleCount];
        this.stops = new Stop[stops.length];
        System.arraycopy(stops, 0, this.stops, 0, stops.length);
        this.segmentDurations = new int[segmentDurations.length];
        System.arraycopy(segmentDurations, 0, this.segmentDurations, 0, segmentDurations.length);
    }
    
    public int getId() {
        return id;
    }
    
    public int getVehicleCount() {
        return vehicles.size();
    }
    
    public abstract void prepareVehicles(IEventQueue eventQueue, ILogReporter eventReporter, int currentTime);

    public abstract void notifyEndOfRoute(Vehicle vehicle, IEventQueue eventQueue, ILogReporter eventReporter, int currentTime);

    protected int getLoopStopDuration(){
        return segmentDurations[segmentDurations.length-1];
    }
}
