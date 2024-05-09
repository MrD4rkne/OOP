package Simulation.Logs;

import Simulation.Common.Stop;
import Simulation.Passengers.Passenger;
import Simulation.Vehicles.Vehicle;

public class Statistic implements IStatistic{
    private final Stats stats;

    public Statistic(){
        stats = new Stats();
    }
    
    @Override
    public void addPassengerWait(Passenger passenger, Stop stop){
        stats.getWaiting().incrementCount();
    }

    @Override
    public void addPassengerBoarded(Passenger passenger, Vehicle vehicle, int waitedFor) {
        if(waitedFor <0){
            throw new IllegalArgumentException("Could not wait negative time");
        }
        stats.getWaiting().increaseSumBy(waitedFor);
        stats.getTrips().incrementCount();
    }

    @Override
    public void addPassengerArriveAtDestination(Passenger passenger, Vehicle vehicle, int tripDuration) {
        stats.getSuccessfulTripsCount().increment();
        stats.getTrips().increaseSumBy(tripDuration);
    }

    @Override
    public void addPassengerDidNotTravelThisDay(Passenger passenger) {
        stats.getDidNotTravelPassengersCount().increment();
    }

    @Override
    public void addPassengerLeaveForcefully(Passenger passenger, Vehicle vehicle, int traveledFor) {
        stats.getForcedEndedTripsCount().increment();
        stats.getTrips().increaseSumBy(traveledFor);
    }

    @Override
    public void addVehicleStartRoute(Vehicle vehicle, int time) {
        stats.getRoutesCount().increment();
    }

    @Override
    public String generateLocalStatistic() {
        return stats.toStringLocal();
    }

    @Override
    public String generateTotalStatistic() {
        return stats.toStringTotal();
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
    public void addPassengerStoppedWaiting(int duration) {
        stats.getWaiting().increaseSumBy(duration);
    }
}
