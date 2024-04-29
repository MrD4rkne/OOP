package Simulation.Vehicles;

import Simulation.Logs.Log;

public class VehicleEndsDayLog extends Log {
    private final Vehicle vehicle;
    
    public VehicleEndsDayLog(int time, Vehicle vehicle) {
        super(time);
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return String.format("%s ended its day",
                vehicle.toString());
    }
}
