package Simulation.Vehicles;

import Simulation.Core.IRandomProvider;
import Simulation.Events.Event;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;

public class VehicleArrivesAtStopEvent extends Event {
    private final Vehicle vehicle;
    private final int stopIndex;

    public VehicleArrivesAtStopEvent(int time, Vehicle vehicle,int stopIndex){
        super(time);
        this.vehicle=vehicle;
        this.stopIndex=stopIndex;
    }

    @Override
    public void process(IEventQueue queue, ILogReporter reporter, IRandomProvider randomProvider) {
        vehicle.stop(stopIndex, queue,reporter, randomProvider, time);
    }
}
