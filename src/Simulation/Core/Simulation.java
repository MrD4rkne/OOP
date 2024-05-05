package Simulation.Core;

import Simulation.Logs.IStatistic;
import Simulation.Logs.Statistic;
import Simulation.Passengers.Passenger;
import Simulation.Events.*;
import Simulation.Common.Line;
import Simulation.Common.Stop;
import Simulation.Logs.ILogReporter;
import Simulation.Passengers.PassengerTryEnterPrimaryStopEvent;

public class Simulation {
    private static final int MINUTES_PER_DAY = 24 * 60;
    private static final int PASSENGER_GET_OUT_LAST_MINUTE = 12 * 60;
    private static final int SIMULATION_START_MINUTE = 6 * 60;

    private final int daysCount;
    private final int passengersCount;
    private final Stop[] stops;
    private final Line[] lines;
    private final ILogReporter eventReporter;
    private final IEventQueue eventQueue;
    private final Passenger[] passengers;

    public Simulation(int daysCount, int passengersCount, Stop[] stops, Line[] lines, ILogReporter eventReporter) {
        this.daysCount = daysCount;
        this.passengersCount = passengersCount;
        this.lines = new Line[lines.length];
        System.arraycopy(lines, 0, this.lines, 0, lines.length);
        this.stops = new Stop[stops.length];
        System.arraycopy(stops, 0, this.stops, 0, stops.length);
        this.eventReporter = eventReporter;
        eventQueue = new EventQueue();
        this.passengers = generatePassengers();
    }

    public void simulate() {
        eventQueue.clear();
        IStatistic statistic = new Statistic();
        for (int day = 1; day <= daysCount; day++) {
            eventReporter.prepareLogging(day,statistic);
            prepareVehiclesForDay(day);
            preparePassengersForDay();
            simulateDay();
        }
    }

    private void simulateDay() {
        while(!eventQueue.isEmpty()) {
            Event event = eventQueue.pop();
            event.process(eventQueue, eventReporter);
        }
    }

    private void preparePassengersForDay() {
        for (Passenger passenger : passengers) {
            passenger.reset();
            int time = RandomNumberGenerator.random(SIMULATION_START_MINUTE, PASSENGER_GET_OUT_LAST_MINUTE);
            PassengerTryEnterPrimaryStopEvent event = new PassengerTryEnterPrimaryStopEvent(passenger, time);
            eventQueue.add(event);
        }
    }

    private void prepareVehiclesForDay(int day) {
        int currentVehicleNo = 0;
        for (Line line : lines) {
            int vehiclesCount = line.prepareVehicles(eventQueue, eventReporter, SIMULATION_START_MINUTE, currentVehicleNo);
            currentVehicleNo+=vehiclesCount;
        }
    }

    private Passenger[] generatePassengers() {
        Passenger[] passengers = new Passenger[passengersCount];
        for (int i = 0; i < passengersCount; i++) {
            passengers[i] = new Passenger(i, stops[RandomNumberGenerator.random(0, stops.length)]);
        }
        return passengers;
    }
}
