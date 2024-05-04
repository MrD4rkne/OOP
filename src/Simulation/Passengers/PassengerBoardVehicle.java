package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Logs.Log;
import Simulation.Vehicles.Vehicle;

public class PassengerBoardVehicle extends Log {
    private final Passenger passenger;
    private final Vehicle vehicle;

    private final Stop stop;
    
    public PassengerBoardVehicle(int time, Passenger passenger, Vehicle vehicle, Stop stop) {
        super(time);
        this.passenger = passenger;
        this.vehicle = vehicle;
        this.stop=stop;
    }

    @Override
    public String toString() {
        return String.format("Passenger %s board %s with desire to go to stop %s",
                passenger.toString(), vehicle.toString(), stop.toString());
    }
}
