package Simulation.Common;

import Collection.IMyList;
import Collection.IQueue;
import Collection.MyArrayList;
import Collection.Queue;
import Simulation.Passengers.Passenger;

public class Stop {
    private final String name;
    private final int capacity;
    private final IQueue<Passenger> waitingPassengers;
    
    public Stop(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.waitingPassengers = new Queue<Passenger>();
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

    @Override
    public String toString(){
        return "Stop " + name;
    }
}
