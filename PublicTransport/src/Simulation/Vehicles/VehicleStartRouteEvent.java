package Simulation.Vehicles;

import Simulation.Common.Segment;
import Simulation.Core.IRandomProvider;
import Simulation.Events.Event;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;

public class VehicleStartRouteEvent extends Event {
    private final Vehicle vehicle;
    private final Segment[] route;
    
    public VehicleStartRouteEvent(int time, Vehicle vehicle, Segment[] route) {
        super(time);
        this.vehicle = vehicle;
        this.route = new Segment[route.length];
        System.arraycopy(route, 0, this.route, 0, route.length);
    }

    @Override
    public void process(IEventQueue queue, ILogReporter reporter, IRandomProvider randomProvider) {
        vehicle.startRoute(route, queue, reporter, time);
    }
}
