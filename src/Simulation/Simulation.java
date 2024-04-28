package Simulation;

import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;
import Simulation.Event.PassengerTryEnterPrimaryStop;

public class Simulation {
    private static final int MINUTES_PER_DAY=24*60;
    private static final int SIMULATION_START_MINUTE=6*60;
    
    private final int daysCount;
    private final int stopsCapacity;
    private final int stopsCount;
    private final int tramCapacity;
    private final int passengersCount;
    private final Stop[] stops;
    private final Line[] lines;
    private final IEventReporter eventReporter;
    private final IEventQueue eventQueue;
    private final Passenger[] passengers;
    
    public Simulation(int daysCount, int stopsCapacity, int stopsCount, int tramCapacity, int passengersCount, Stop[] stops, Line[] lines,IEventReporter eventReporter) {
        this.daysCount = daysCount;
        this.stopsCapacity = stopsCapacity;
        this.stopsCount = stopsCount;
        this.tramCapacity = tramCapacity;
        this.passengersCount = passengersCount;
        this.lines = new Line[lines.length];
        System.arraycopy(lines, 0, this.lines, 0, lines.length);
        this.stops = new Stop[stops.length];
        System.arraycopy(stops, 0, this.stops, 0, stops.length);
        this.eventReporter = eventReporter;
        eventQueue = new EventQueue();
        this.passengers=generatePassengers();
    }
    
    public void simulate(){
        preparePassengersForDay();
        for(int day = 1; day<=daysCount; day++) {
        }
    }
    
    private void preparePassengersForDay()
    {
        for(Passenger passenger : passengers) {
            int time = RandomNumberGenerator.random(SIMULATION_START_MINUTE, MINUTES_PER_DAY);
            PassengerTryEnterPrimaryStop event = new PassengerTryEnterPrimaryStop(passenger, time);
            eventQueue.add(event);
        }
    }
    
    private void prepareVehiclesForDay(int day) {
        for (Line line : lines) {
            for (int i = 0; i < line.getVehicleCount(); i++) {
                line.
            }
        }
    }
    
    private Passenger[] generatePassengers(){
        Passenger[] passengers = new Passenger[passengersCount];
        for(int i=0; i<passengersCount; i++) {
            passengers[i] = new Passenger(i, stops[RandomNumberGenerator.random(0, stops.length)]);
        }
        return passengers;
    }
}
