package Simulation.Logs;

import Simulation.Passengers.Passenger;
import Simulation.Vehicles.Vehicle;

public interface IStatistic {
    void addPassengerBoarded(Passenger passenger, Vehicle vehicle, int waitedFor);

    void addPassengerArriveAtDestination(Passenger passenger, Vehicle vehicle, int tripDuration);

    void addPassengerDidNotTravelThisDay(Passenger passenger);

    void addPassengerLeaveForcefully(Passenger passenger, Vehicle vehicle);

    void addVehicleStartRoute(Vehicle vehicle, int time);

    void addEvent();

    Stats generateStatistic();

    void addPassengerStoppedWaiting(int duration);
}
