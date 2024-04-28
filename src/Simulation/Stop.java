package Simulation;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;

public class Stop {
    private final String name;
    private final int capacity;
    private final IMyList<Passenger> waitingPassengers;
    
    public Stop(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
        this.waitingPassengers = new MyArrayList<Passenger>(capacity);
    }
    
    public String getName() {
        return name;
    }

    public Boolean hasSpace() {
        return waitingPassengers.size() < capacity;
    }
    
    public void enter(Passenger passenger) {
        if (!hasSpace()) {
            throw new IllegalStateException("No space left");
        }
        waitingPassengers.add(passenger);
    }

    public void tryBoardPassengers(Vehicle vehicle, IEventQueue eventQueue, int time) {
        for (int i = 0; i < waitingPassengers.size(); i++) {
            if (!vehicle.hasSpace()) {
                return;
            }
            Passenger passenger = waitingPassengers.get(i);
            passenger.board(vehicle, time);
            waitingPassengers.removeAt(i);
        }
    }
}
