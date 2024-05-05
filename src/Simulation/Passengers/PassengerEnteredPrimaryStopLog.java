package Simulation.Passengers;

import Simulation.Logs.IStatistic;
import Simulation.Logs.Log;

public class PassengerEnteredPrimaryStopLog extends Log {
    private final Passenger passenger;
    
    public PassengerEnteredPrimaryStopLog(int time, Passenger passenger) {
        super(time);
        this.passenger = passenger;
    }

    @Override
    public String toString() {
        return String.format("Passenger %s entered his primary stop %s",
                passenger.toString(), passenger.getPrimaryStop().toString());
    }
}
