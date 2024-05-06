package Simulation.Logs;

import Simulation.Common.Stop;
import Simulation.Passengers.Passenger;
import Simulation.Vehicles.Vehicle;

public interface IStatistic {
    void addPassengerWait(Passenger passenger, Stop stop);
    
    void addPassengerBoarded(Passenger passenger, Vehicle vehicle, int waitedFor);

    void addPassengerArriveAtDestination(Passenger passenger, Vehicle vehicle, int tripDuration);

    void addPassengerDidNotTravelThisDay(Passenger passenger);

    void addPassengerLeaveForcefully(Passenger passenger, Vehicle vehicle, int traveledFor);

    void addVehicleStartRoute(Vehicle vehicle, int time);

    void addPassengerStoppedWaiting(int duration);

    Stats generateStatistic();
    
    void resetLocal();
}
