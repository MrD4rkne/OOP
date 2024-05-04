package Simulation.Passengers;

import Simulation.Logs.Log;
import Simulation.Vehicles.Vehicle;

public class PassengerLeftVehicleDueToEndOfDay extends Log {
    private final Passenger passenger;

    private final Vehicle vehicle;
    
    public PassengerLeftVehicleDueToEndOfDay(int time, Passenger passenger, Vehicle vehicle) {
        super(time);
        this.passenger = passenger;
        this.vehicle=vehicle;
    }

    @Override
    public String toString() {
        return String.format("Passenger %s left %s due to end of day",
                passenger.toString(),
                vehicle.toString());
    }
}
