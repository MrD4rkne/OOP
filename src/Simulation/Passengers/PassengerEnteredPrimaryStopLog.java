package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Logs.IStatistic;
import Simulation.Logs.Log;

public class PassengerEnteredPrimaryStopLog extends Log {
    private final Passenger passenger;
    
    private final Stop stop;
    
    public PassengerEnteredPrimaryStopLog(int time, Passenger passenger, Stop primaryStop) {
        super(time);
        this.passenger = passenger;
        this.stop = primaryStop;
    }

    @Override
    public void updateStatistic(IStatistic statistic) {
        super.updateStatistic(statistic);
        statistic.addPassengerWait(passenger,stop);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s entered his primary stop %s",
                passenger.toString(), stop.toString());
    }
}
