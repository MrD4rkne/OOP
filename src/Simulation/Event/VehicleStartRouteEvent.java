package Simulation.Event;

import Simulation.Common.Segment;
import Simulation.Common.Vehicle;

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
    public void process(IEventQueue queue, IEventReporter reporter) {
        vehicle.startRoute(route, queue, reporter, time);
    }
}
