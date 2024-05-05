package Simulation.Logs;

import Simulation.Events.Event;
import Simulation.Passengers.Passenger;
import Simulation.Vehicles.Vehicle;

public class Statistic implements IStatistic{
    private final Stat tripsCount;

    private final Stat tripsDuration;

    private final Stat waitingCount;

    private final Stat waitingDuration;

    private final Stat successfulTripsCount;

    private final Stat forcedEndedTripsCount;

    private final Stat didNotTravelPassengersCount;

    private final Stat routesCount;

    public Statistic(){
        tripsCount = new Stat();
        tripsDuration = new Stat();
        waitingCount = new Stat();
        waitingDuration = new Stat();
        successfulTripsCount = new Stat();
        didNotTravelPassengersCount=new Stat();
        forcedEndedTripsCount = new Stat();
        routesCount = new Stat();
    }

    @Override
    public void addPassengerBoarded(Passenger passenger, Vehicle vehicle, int waitedFor) {
        if(waitedFor <0){
            throw new IllegalArgumentException("Could not wait negative time");
        }
        waitingDuration.increaseBy(waitedFor);
        tripsDuration.increment();
    }

    @Override
    public void addPassengerArriveAtDestination(Passenger passenger, Vehicle vehicle, int tripDuration) {
        successfulTripsCount.increment();
    }

    @Override
    public void addPassengerDidNotTravelThisDay(Passenger passenger) {
        didNotTravelPassengersCount.increment();
    }

    @Override
    public void addPassengerLeaveForcefully(Passenger passenger, Vehicle vehicle) {
        forcedEndedTripsCount.increment();
    }

    @Override
    public void addVehicleStartRoute(Vehicle vehicle, int time) {
        routesCount.increment();
    }

    @Override
    public Stats generateStatistic() {
        return new Stats();
    }

    @Override
    public void addPassengerStoppedWaiting(int duration) {
        waitingDuration.increaseBy(duration);
    }
}
