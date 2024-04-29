package Simulation.Vehicles.Tram;

import Simulation.Common.Line;
import Simulation.Common.Segment;
import Simulation.Common.Stop;
import Simulation.Vehicles.Vehicle;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.VehicleStartRouteEvent;
import Simulation.Vehicles.VehicleEndsDayLog;
import Simulation.Vehicles.VehicleEndsRouteLog;

public class TramLine extends Line {
    private static final int TRAM_LAST_DEPARTURE_MINUTE = 23*60;
    
    private final int tramsCapacity;

    public TramLine(int id, int vehicleCount, Stop[] stops, int[] segmentDurations, int tramsCapacity) {
        super(id, vehicleCount, stops, segmentDurations);
        this.tramsCapacity = tramsCapacity;
    }

    @Override
    public void prepareVehicles(IEventQueue eventQueue, ILogReporter eventReporter, int currentTime) {
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
    public void notifyEndOfRoute(Vehicle vehicle, IEventQueue eventQueue, ILogReporter eventReporter, int currentTime) {
        eventReporter.log(new VehicleEndsRouteLog(currentTime, vehicle, vehicle.getFinalStop()));
        if(currentTime > TRAM_LAST_DEPARTURE_MINUTE){
            vehicle.endDay(eventQueue,eventReporter,currentTime);
            return;
        }
        
        vehicle.enterLoop(eventQueue,eventReporter,currentTime, vehicle.getFinalStop());
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
