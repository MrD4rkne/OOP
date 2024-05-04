package Simulation.Vehicles;

import Simulation.Common.Stop;
import Simulation.Logs.Log;

public class VehicleLeavesStopLog extends Log {
    private final Vehicle vehicle;
    private final Stop stop;
    private final int passengersBoarded;
    private final int passengersLeft;
    
    public VehicleLeavesStopLog(int time, Vehicle vehicle, Stop stop, int passengersBoarded, int passengersLeft) {
        super(time);
        this.vehicle = vehicle;
        this.stop = stop;
        this.passengersBoarded = passengersBoarded;
        this.passengersLeft = passengersLeft;
    }

    @Override
    public String toString() {
        return String.format("%s leaves %s. %d passengers boarded, %d passengers left",
                vehicle.toString(), stop.toString(), passengersBoarded, passengersLeft);
    }
}
