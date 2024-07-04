package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Statistic.IStatisticService;
import Simulation.Logs.Log;

public class PassengerEnterStopLog extends Log {
    private final Passenger passenger;
    
    private final Stop stop;
    
    public PassengerEnterStopLog(int time, Passenger passenger, Stop stop) {
        super(time);
        this.passenger = passenger;
        this.stop = stop;
    }

    @Override
    public void updateStatistic(IStatisticService statistic) {
        super.updateStatistic(statistic);
        statistic.registerPassengerWait(passenger,stop);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s entered stop %s",
                passenger.toString(), stop.toString());
    }
}
