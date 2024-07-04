package Simulation.Statistic;

import Simulation.Common.Stop;
import Simulation.Passengers.Passenger;
import Simulation.Vehicles.Vehicle;

public class StatisticService implements IStatisticService {
    private final StatisticHolder stats;

    public StatisticService(){
        stats = new StatisticHolder();
    }
    
    @Override
    public void registerPassengerWait(Passenger passenger, Stop stop){
        stats.getWaiting().incrementCount();
    }

    @Override
    public void registerPassengerBoarded(Passenger passenger, Vehicle vehicle, int waitedFor) {
        if(waitedFor <0){
            throw new IllegalArgumentException("Could not wait negative time");
        }
        stats.getWaiting().increaseSumBy(waitedFor);
        stats.getTrips().incrementCount();
    }

    @Override
    public void registerPassengerArriveAtDestination(Passenger passenger, Vehicle vehicle, int tripDuration) {
        stats.getSuccessfulTripsCount().increment();
        stats.getTrips().increaseSumBy(tripDuration);
    }

    @Override
    public void registerPassengerDidNotTravelThisDay(Passenger passenger) {
        stats.getDidNotTravelPassengersCount().increment();
    }

    @Override
    public void registerPassengerLeaveForcefully(Passenger passenger, Vehicle vehicle, int traveledFor) {
        stats.getForcedEndedTripsCount().increment();
        stats.getTrips().increaseSumBy(traveledFor);
    }

    @Override
    public void registerVehicleStartRoute(Vehicle vehicle, int time) {
        stats.getRoutesCount().increment();
    }

    @Override
    public StatisticHolder getStats() {
        return new StatisticHolder(stats);
    }

    @Override
    public String toString() {
        return stats.toString();
    }

    @Override
    public void resetLocal() {
        stats.resetLocal();
    }

    @Override
    public void registerPassengerStoppedWaiting(int duration) {
        stats.getWaiting().increaseSumBy(duration);
    }
}
