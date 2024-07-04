package Simulation.Common;

import Collection.Queue.IQueue;
import Collection.Queue.Queue;
import Simulation.Logs.ILogReporter;
import Simulation.Passengers.Passenger;

public class Stop {
    private final String name;
    private final int capacity;
    private final IQueue<Passenger> waitingPassengers;
    
    public Stop(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.waitingPassengers = new Queue<>();
    }
    
    public String getName() {
        return name;
    }

    public Boolean hasSpace() {
        return waitingPassengers.size() < capacity;
    }
    
    public void putPassenger(Passenger passenger) {
        if (!hasSpace()) {
            throw new IllegalStateException("No space left");
        }
        waitingPassengers.enqueue(passenger);
    }

    public boolean hasPassengers(){
        return !waitingPassengers.isEmpty();
    }
    
    public Passenger passengerLeave() {
        if (waitingPassengers.isEmpty()) {
            throw new IllegalStateException("No passengers left");
        }
        return waitingPassengers.dequeue();
    }
    
    public void kickOutAllPassengers(ILogReporter reporter, int time) {
        while(!waitingPassengers.isEmpty()) {
            Passenger passenger = waitingPassengers.dequeue();
            passenger.abortWaitForVehicle(reporter,time);
        }
    }

    @Override
    public String toString(){
        return "Stop " + name;
    }
}
