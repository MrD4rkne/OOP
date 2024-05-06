package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Logs.IStatistic;
import Simulation.Logs.Log;

public class PassengerCouldNotEnterPrimaryStopLog extends Log {
    private final Passenger passenger;
    
    private final Stop stop;

    public PassengerCouldNotEnterPrimaryStopLog(int time, Passenger passenger, Stop stop) {
        super(time);
        this.passenger = passenger;
        this.stop=stop;
    }

    @Override
    public void updateStatistic(IStatistic statistic) {
        super.updateStatistic(statistic);
        statistic.addPassengerDidNotTravelThisDay(passenger);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s could not enter his primary stop %s",
                passenger.toString(), stop.toString());
    }
}
