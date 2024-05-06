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
        stats.getWaitingCount().increment();
    }

    @Override
    public void addPassengerBoarded(Passenger passenger, Vehicle vehicle, int waitedFor) {
        if(waitedFor <0){
            throw new IllegalArgumentException("Could not wait negative time");
        }
        stats.getWaitingDuration().increaseBy(waitedFor);
        stats.getTripsCount().increment();
    }

    @Override
    public void addPassengerArriveAtDestination(Passenger passenger, Vehicle vehicle, int tripDuration) {
        stats.getSuccessfulTripsCount().increment();
        stats.getTripsDuration().increaseBy(tripDuration);
    }

    @Override
    public void addPassengerDidNotTravelThisDay(Passenger passenger) {
        stats.getDidNotTravelPassengersCount().increment();
    }

    @Override
    public void addPassengerLeaveForcefully(Passenger passenger, Vehicle vehicle, int traveledFor) {
        stats.getForcedEndedTripsCount().increment();
        stats.getTripsDuration().increaseBy(traveledFor);
    }

    @Override
    public void addVehicleStartRoute(Vehicle vehicle, int time) {
        stats.getRoutesCount().increment();
    }

    @Override
    public Stats generateStatistic() {
        return new Stats(stats);
    }

    @Override
    public void resetLocal() {
        stats.getTripsCount().resetLocal();
        stats.getTripsDuration().resetLocal();
        stats.getWaitingCount().resetLocal();
        stats.getWaitingDuration().resetLocal();
        stats.getSuccessfulTripsCount().resetLocal();
        stats.getForcedEndedTripsCount().resetLocal();
        stats.getDidNotTravelPassengersCount().resetLocal();
        stats.getRoutesCount().resetLocal();
    }

    @Override
    public void addPassengerStoppedWaiting(int duration) {
        stats.getWaitingDuration().increaseBy(duration);
    }
}
