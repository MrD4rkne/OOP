package Simulation.Vehicles.Tram;

import Simulation.Common.Line;
import Simulation.Common.Segment;
import Simulation.Common.Stop;
import Simulation.Vehicles.Vehicle;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.VehicleStartRouteEvent;
import Simulation.Vehicles.VehicleEndsRouteLog;

public class TramLine extends Line {
    private static final int TRAM_LAST_DEPARTURE_MINUTE = 23*60;
    
    private final int tramsCapacity;

    public TramLine(int id, int vehicleCount, Stop[] stops, int[] segmentDurations, int tramsCapacity) {
        super(id, vehicleCount, stops, segmentDurations);
        this.tramsCapacity = tramsCapacity;
    }

    @Override
    public int prepareVehicles(IEventQueue eventQueue, ILogReporter eventReporter, int currentTime, int vehicleStartingNo) {
        boolean shouldStartLeft = true;
        int interval = calculateFullRouteTime()/getVehicleCount();
        vehicles.clear();
        for (int i = 0; i < getVehicleCount(); i++) {
            vehicles.add(new Tram(this, vehicleStartingNo + i, tramsCapacity));
            int departureTime = currentTime + (i/2)*interval;
            if(departureTime > TRAM_LAST_DEPARTURE_MINUTE){
                break;
            }
            
            VehicleStartRouteEvent startRouteEvent = new VehicleStartRouteEvent(departureTime, vehicles.get(i), createRoute(shouldStartLeft));
            shouldStartLeft = !shouldStartLeft;
            eventQueue.add(startRouteEvent);
        }
        return getVehicleCount();
    }

    @Override
    public void notifyEndOfRoute(Vehicle vehicle, IEventQueue eventQueue, ILogReporter eventReporter, int currentTime) {
        eventReporter.log(new VehicleEndsRouteLog(currentTime, vehicle, vehicle.getFinalStop()));
        if(vehicle.isOnStartingStop() && currentTime > TRAM_LAST_DEPARTURE_MINUTE){
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
        if(shouldStartLeft) {
            for (int i = 0; i < stops.length; i++) {
                int duration = i == stops.length-1 ? 0 : segmentDurations[i];
                route[i] = new Segment(i, stops[i], duration);
            }
        }
        else{
            for (int i = 0; i < stops.length; i++) {
                int duration = i == stops.length-1 ? 0 : segmentDurations[stops.length-1-i -1];
                route[i] = new Segment(i, stops[stops.length-1-i], duration);
            }
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
    
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString());
        sb.append("Trams capacity: ").append(tramsCapacity).append("\n");
        return sb.toString();
    }
}
