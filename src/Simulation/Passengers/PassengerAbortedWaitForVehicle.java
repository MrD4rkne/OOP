package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Logs.IStatistic;
import Simulation.Logs.Log;

public class PassengerAbortedWaitForVehicle extends Log {
    private final Passenger passenger;
    
    private final int waitedFor;
    
    public PassengerAbortedWaitForVehicle(int time, Passenger passenger, int waitedFor) {
        super(time);
        this.passenger = passenger;
        this.waitedFor = waitedFor;
    }
    
    @Override
    public void updateStatistic(IStatistic statistic) {
        super.updateStatistic(statistic);
        statistic.addPassengerStoppedWaiting(waitedFor);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s stopped waiting",
                passenger.toString());
    }
}
