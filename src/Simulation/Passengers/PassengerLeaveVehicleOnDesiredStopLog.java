package Simulation.Passengers;

import Simulation.Common.Stop;
import Simulation.Logs.IStatistic;
import Simulation.Logs.Log;
import Simulation.Vehicles.Vehicle;

public class PassengerLeaveVehicleOnDesiredStopLog extends Log {
    private final Passenger passenger;

    private final Vehicle vehicle;

    private final Stop stop;

    private final int tripDuration;

    public PassengerLeaveVehicleOnDesiredStopLog(int time, Passenger passenger, Vehicle vehicle, Stop stop, int tripDuration) {
        super(time);
        this.passenger = passenger;
        this.vehicle=vehicle;
        this.stop=stop;
        this.tripDuration=tripDuration;
    }

    @Override
    public void updateStatistic(IStatistic statistic) {
        super.updateStatistic(statistic);
        statistic.addPassengerArriveAtDestination(passenger,vehicle,tripDuration);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s left %s at desired destination: %s",
                passenger.toString(),
                vehicle.toString(),
                stop.toString());
    }
}