package Simulation.Event;

import Simulation.Common.Vehicle;

public class VehicleArrivesAtStopEvent extends Event {
    private final Vehicle vehicle;
    private final int stopIndex;

    public VehicleArrivesAtStopEvent(int time, Vehicle vehicle,int stopIndex){
        super(time);
        this.vehicle=vehicle;
        this.stopIndex=stopIndex;
    }

    @Override
    public void process(IEventQueue queue, IEventReporter reporter) {
        vehicle.stop(stopIndex, queue,reporter,time);
    }
}
