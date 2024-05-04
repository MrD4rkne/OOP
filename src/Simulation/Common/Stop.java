package Simulation.Common;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;
import Simulation.Passengers.Passenger;
import Simulation.Vehicles.Vehicle;

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

    public int tryBoardPassengers(Vehicle vehicle, IEventQueue eventQueue, ILogReporter reporter, int time) {
        int boardCount = 0;
        for (int i = 0; i < waitingPassengers.size(); i++) {
            if (!vehicle.hasSpaceLeft()) {
                break;
            }
            Passenger passenger = waitingPassengers.get(i);
            passenger.board(vehicle, time, reporter);
            waitingPassengers.removeAt(i);
            i--;
            boardCount++;
        }
        return boardCount;
    }

    @Override
    public String toString(){
        return "Stop " + name;
    }
}
