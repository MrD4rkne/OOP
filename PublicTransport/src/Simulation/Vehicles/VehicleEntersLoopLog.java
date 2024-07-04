package Simulation.Vehicles;

import Simulation.Common.Stop;
import Simulation.Logs.Log;

public class VehicleEntersLoopLog extends Log {
    private final Vehicle vehicle;
    private final Stop stop;
    
    public VehicleEntersLoopLog(int time, Vehicle vehicle, Stop stop) {
        super(time);
        this.vehicle = vehicle;
        this.stop = stop;
    }
    
    @Override
    public String toString() {
        return String.format("%s enters loop at %s",
                vehicle.toString(), stop.toString());
    }
}
