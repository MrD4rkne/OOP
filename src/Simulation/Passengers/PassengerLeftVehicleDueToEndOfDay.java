package Simulation.Passengers;

import Simulation.Logs.IStatistic;
import Simulation.Logs.Log;
import Simulation.Vehicles.Vehicle;

public class PassengerLeftVehicleDueToEndOfDay extends Log {
    private final Passenger passenger;

    private final Vehicle vehicle;
    
    private final int tripDuration;
    
    public PassengerLeftVehicleDueToEndOfDay(int time, Passenger passenger, Vehicle vehicle, int tripDuration) {
        super(time);
        this.passenger = passenger;
        this.vehicle=vehicle;
        this.tripDuration=tripDuration;
    }
    
    @Override
    public void updateStatistic(IStatistic statistic) {
        super.updateStatistic(statistic);
        statistic.addPassengerLeaveForcefully(passenger,vehicle,tripDuration);
    }

    @Override
    public String toString() {
        return String.format("Passenger %s left %s due to end of day",
                passenger.toString(),
                vehicle.toString());
    }
}
