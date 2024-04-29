package Simulation.Passengers;

import Simulation.Logs.Log;

public class PassengerLeftVehicleDueToEndOfDay extends Log {
    private final Passenger passenger;
    
    public PassengerLeftVehicleDueToEndOfDay(int time, Passenger passenger) {
        super(time);
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return String.format("Passenger %s left vehicle due to end of day",
                passenger.toString());
    }
}
