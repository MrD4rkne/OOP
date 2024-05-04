package Simulation.Vehicles;

import Simulation.Common.Stop;
import Simulation.Logs.Log;

public class VehicleStartRouteLog extends Log {
    private final Vehicle vehicle;
    private final Stop firstStop;
    private final Stop lastStop;
    
    public VehicleStartRouteLog(int time, Vehicle vehicle, Stop firstStop, Stop lastStop) {
        super(time);
        this.vehicle = vehicle;
        this.firstStop = firstStop;
        this.lastStop = lastStop;
    }

    @Override
    public String toString() {
        return String.format("%s starts its route from %s to %s",
                vehicle.toString(), firstStop.toString(), lastStop.toString());
    }
}
