package Simulation.Tram;

import Simulation.Common.Line;
import Simulation.Common.Segment;
import Simulation.Common.Stop;
import Simulation.Common.Vehicle;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;
import Simulation.Event.VehicleStartRouteEvent;
import Simulation.Tram.Tram;

public class TramLine extends Line<Tram> {
    private static final int TRAM_LAST_DEPARTURE_MINUTE = 23*60;
    
    private final int tramsCapacity;

    public TramLine(int id, int vehicleCount, Stop[] stops, int[] segmentDurations, int tramsCapacity) {
        super(id, vehicleCount, stops, segmentDurations);
        this.tramsCapacity = tramsCapacity;
    }

    @Override
    public void prepareVehicles(IEventQueue eventQueue, IEventReporter eventReporter, int currentTime) {
        boolean shouldStartLeft = true;
        int interval = calculateFullRouteTime()/vehicles.size();
        for (int i = 0; i < vehicles.size(); i++) {
            vehicles.set(i,new Tram(this, i, tramsCapacity));
            int departureTime = currentTime + i*interval;
            VehicleStartRouteEvent startRouteEvent = new VehicleStartRouteEvent(departureTime, vehicles.get(i), createRoute(shouldStartLeft));
            shouldStartLeft = !shouldStartLeft;
        }
    }

    @Override
    public void notifyEndOfRoute(Tram vehicle, IEventQueue eventQueue, IEventReporter eventReporter, int currentTime) {
        if(currentTime > TRAM_LAST_DEPARTURE_MINUTE){
            eventReporter.reportTramFinishedDay(vehicle, currentTime);
            return;
        }
        int nextDeparture = currentTime + getLoopStopDuration();
        boolean shouldStartLeft = !isGoingRight(vehicle);
        VehicleStartRouteEvent startRouteEvent = new VehicleStartRouteEvent(nextDeparture, vehicle, createRoute(shouldStartLeft));
        eventQueue.add(startRouteEvent);
    }
    
    private boolean isGoingRight(Vehicle vehicle){
        return vehicle.getFinalStop() == stops[stops.length-1];
    }
    
    private Segment[] createRoute(boolean shouldStartLeft){
        Segment[] route = new Segment[stops.length];
        for (int i = 0; i < stops.length; i++) {
            route[i] = new Segment(i,stops[i], segmentDurations[i]);
        }
        return route;
    }
    
    private int calculateFullRouteTime(){
        int oneWayFull = 0;
        for (int segmentDuration : segmentDurations) {
            oneWayFull += segmentDuration;
        }
        return 2*oneWayFull;
    }
}
