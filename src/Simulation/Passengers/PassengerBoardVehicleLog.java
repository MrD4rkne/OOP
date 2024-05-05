package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Logs.IStatistic;
import Simulation.Logs.Log;
import Simulation.Vehicles.Vehicle;

public class PassengerBoardVehicleLog extends Log {
    private final Passenger passenger;
    private final Vehicle vehicle;

    private final int waitedFor;

    private final Stop stop;
    
    public PassengerBoardVehicleLog(int time, Passenger passenger, Vehicle vehicle, Stop stop, int waitedFor) {
        super(time);
        this.passenger = passenger;
        this.vehicle = vehicle;
        this.stop=stop;
        this.waitedFor =waitedFor;
    }

    @Override
    public void updateStatistic(IStatistic statistic) {
        super.updateStatistic(statistic);
        statistic.addPassengerBoarded(passenger,vehicle, waitedFor);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s board %s with desire to go to stop %s",
                passenger.toString(), vehicle.toString(), stop.toString());
    }
}
