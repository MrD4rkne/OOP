package Simulation;

import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;
import Simulation.Event.VehicleArrivesAtStopEvent;

public class TramLine extends Line{
    private static final int TRAM_LAST_DEPARTURE_MINUTE = 23*60;

    public TramLine(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        super(id, vehicleCount, stops, segmentDurations);
    }

    @Override
    public void trySchedule(Vehicle vehicle, IEventQueue eventQueue, int time) {
        if(time >TRAM_LAST_DEPARTURE_MINUTE)
            return;
        Direction direction = getCurrentVehicleDirection(vehicle);
        Segment currentSegment = getCurrentVehicleSegment(vehicle);
        int timeOfNextStop = time + calculateTimeUntilNextStop(currentSegment, direction);
        VehicleArrivesAtStopEvent vehicleArrivesAtStopEvent = new VehicleArrivesAtStopEvent(timeOfNextStop, vehicle, next)
    }

    @Override
    public void reportStop(Segment segment, IEventReporter eventReporter, int time) {

    }
}
