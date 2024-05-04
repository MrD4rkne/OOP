package Simulation.Passengers;

import Simulation.Logs.Log;

public class PassengerCouldNotEnterPrimaryStopLog extends Log {
    private final Passenger passenger;

    public PassengerCouldNotEnterPrimaryStopLog(int time, Passenger passenger) {
        super(time);
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return String.format("Passenger %s could not enter his primary stop %s",
                passenger.toString(), passenger.getPrimaryStop().toString());
    }
}
