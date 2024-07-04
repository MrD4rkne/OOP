package Simulation.Statistic;

import Simulation.Common.Stop;
import Simulation.Passengers.Passenger;
import Simulation.Vehicles.Vehicle;

public interface IStatisticService {

    /**
     * Register a passenger that has started waiting
     */
    void registerPassengerWait(Passenger passenger, Stop stop);

    /**
     * Register a passenger that has boarded a vehicle
     */
    void registerPassengerBoarded(Passenger passenger, Vehicle vehicle, int waitedFor);
    
    /**
     * Register a passenger that has arrived at their destination
     */
    void registerPassengerArriveAtDestination(Passenger passenger, Vehicle vehicle, int tripDuration);

    /**
     * Register a passenger that did not travel this day
     */
    void registerPassengerDidNotTravelThisDay(Passenger passenger);

    /**
     * Register a passenger that has been forced to leave a vehicle
     */
    void registerPassengerLeaveForcefully(Passenger passenger, Vehicle vehicle, int traveledFor);

    /**
     * Register a vehicle that has started a route
     */
    void registerVehicleStartRoute(Vehicle vehicle, int time);

    /**
     * Register a passenger that has stopped waiting
     */
    void registerPassengerStoppedWaiting(int duration);
    
    /**
     * Generate a string with the local statistics
     */
    StatisticHolder getStats();
    
    /**
     * Generate a string with the local statistics
     */
    void resetLocal();
}
