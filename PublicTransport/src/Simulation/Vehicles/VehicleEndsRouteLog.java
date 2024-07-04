package Simulation.Vehicles;

import Simulation.Common.Stop;
import Simulation.Logs.Log;

public class VehicleEndsRouteLog extends Log {
    private final Vehicle vehicle;
    private final Stop lastStop;
    
    public VehicleEndsRouteLog(int time, Vehicle vehicle, Stop lastStop) {
        super(time);
        this.vehicle = vehicle;
        this.lastStop = lastStop;
    }
    
    @Override
    public String toString() {
        return String.format("%s ends its route at %s",
                vehicle.toString(), lastStop.toString());
    }
}
