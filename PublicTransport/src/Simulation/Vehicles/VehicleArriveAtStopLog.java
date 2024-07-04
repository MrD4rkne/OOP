package Simulation.Vehicles;

import Simulation.Common.Stop;
import Simulation.Logs.Log;

public class VehicleArriveAtStopLog extends Log {
    private final Vehicle vehicle;
    private final Stop stop;
    
    public VehicleArriveAtStopLog(int time, Vehicle vehicle, Stop stop) {
        super(time);
        this.vehicle = vehicle;
        this.stop = stop;
    }

    @Override
    public String toString() {
        return String.format("%s arrive at %s",
                vehicle.toString(), stop.toString());
    }
}