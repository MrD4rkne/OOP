package Simulation.Event;

import Simulation.Segment;
import Simulation.Vehicle;

public class VehicleArrivesAtStopEvent extends Event {
    private final Vehicle vehicle;
    private final Segment segment;

    public VehicleArrivesAtStopEvent(int time, Vehicle vehicle,Segment segment){
        super(time);
        this.vehicle=vehicle;
        this.segment=segment;
    }

    @Override
    public void process(IEventQueue queue, IEventReporter reporter) {
        vehicle.stop(segment, queue,reporter,time);
    }
}
