package Simulation.Passengers;

import Simulation.Logs.Log;
import Simulation.Vehicles.Vehicle;

public class PassengerBoardVehicle extends Log {
    private final Passenger passenger;
    private final Vehicle vehicle;
    
    public PassengerBoardVehicle(int time, Passenger passenger, Vehicle vehicle) {
        super(time);
        this.passenger = passenger;
        this.vehicle = vehicle;
    }

    @Override
    public String toString() {
        return String.format("Passenger %s board vehicle %s with desire to go to stop %s",
                passenger.toString(), vehicle.toString(), passenger.getDesiredStop().getName());
    }
}
