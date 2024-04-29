package Simulation.Common;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;

public abstract class Line<T extends Vehicle>  {
    protected final int id;
    protected final Stop[] stops;
    protected final int[] segmentDurations;
    protected final IMyList<T> vehicles;
    protected final Segment[] vehiclesSegment;
    
    public Line(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        this.id = id;
        this.vehicles = new MyArrayList<T>();
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
    
    public abstract void prepareVehicles(IEventQueue eventQueue, IEventReporter eventReporter, int currentTime);

    public abstract void notifyEndOfRoute(T vehicle, IEventQueue eventQueue, IEventReporter eventReporter, int currentTime);

    protected int getLoopStopDuration(){
        return segmentDurations[segmentDurations.length-1];
    }
}
