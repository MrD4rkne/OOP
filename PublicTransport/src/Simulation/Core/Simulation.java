package Simulation.Core;

import Simulation.Statistic.IStatisticService;
import Simulation.Statistic.StatisticService;
import Simulation.Statistic.StatisticHolder;
import Simulation.Passengers.Passenger;
import Simulation.Events.*;
import Simulation.Common.Line;
import Simulation.Common.Stop;
import Simulation.Logs.ILogReporter;
import Simulation.Passengers.PassengerTryEnterPrimaryStopEvent;

import javax.net.ssl.SNIHostName;

public class Simulation {
    private static final int MINUTES_PER_DAY = 24 * 60;
    private static final int PASSENGER_GET_OUT_LAST_MINUTE = 12 * 60;
    private static final int SIMULATION_START_MINUTE = 6 * 60;

    private final int daysCount;
    private final int passengersCount;
    private final int stopsCapacity;
    private final int tramCapacity;
    private final Stop[] stops;
    private final Line[] lines;
    private final ILogReporter eventReporter;
    private final IEventQueue eventQueue;
    private final Passenger[] passengers;
    private final IStatisticService statistic;
    private final IRandomProvider randomProvider;
    private int dayNo;

    public Simulation(int daysCount, int passengersCount,int stopsCapacity, int tramCapacity, Stop[] stops, Line[] lines, ILogReporter eventReporter, IRandomProvider randomProvider) {
        this.daysCount = daysCount;
        this.passengersCount = passengersCount;
        this.stopsCapacity = stopsCapacity;
        this.tramCapacity = tramCapacity;
        this.lines = new Line[lines.length];
        System.arraycopy(lines, 0, this.lines, 0, lines.length);
        this.stops = new Stop[stops.length];
        System.arraycopy(stops, 0, this.stops, 0, stops.length);
        this.eventReporter = eventReporter;
        eventQueue = new EventQueue();
        this.statistic = new StatisticService();
        this.randomProvider = randomProvider;
        this.dayNo = -1;

        this.passengers = generatePassengers();
    }
    
    public boolean hasDaysLeft(){
        // dayNo is -1 before first day
        return dayNo+1<daysCount;
    }

    public StatisticHolder simulateDay() {
        if(!hasDaysLeft()){
            throw new IllegalStateException("No more days to simulate");
        }
        dayNo=getNextDayNo();
        
        eventQueue.clear();
        statistic.resetLocal();
        eventReporter.prepareLogging(dayNo,statistic);
        prepareVehiclesForDay();
        preparePassengersForDay();
        
        runDay();
        finishDay();
        return statistic.getStats();
    }

    private void runDay() {
        while(!eventQueue.isEmpty()) {
            Event event = eventQueue.pop();
            event.process(eventQueue, eventReporter, randomProvider);
        }
    }

    private void preparePassengersForDay() {
        for (Passenger passenger : passengers) {
            passenger.reset();
            int time = randomProvider.next(SIMULATION_START_MINUTE, PASSENGER_GET_OUT_LAST_MINUTE);
            PassengerTryEnterPrimaryStopEvent event = new PassengerTryEnterPrimaryStopEvent(passenger, time);
            eventQueue.add(event);
        }
    }
    
    private void finishDay() {
        for(Stop stop : stops) {
            stop.kickOutAllPassengers(eventReporter, MINUTES_PER_DAY);
        }
    }

    private void prepareVehiclesForDay() {
        int currentVehicleNo = 0;
        for (Line line : lines) {
            int vehiclesCount = line.prepareVehicles(eventQueue, eventReporter, SIMULATION_START_MINUTE, currentVehicleNo);
            currentVehicleNo+=vehiclesCount;
        }
    }

    private Passenger[] generatePassengers() {
        Passenger[] passengers = new Passenger[passengersCount];
        for (int i = 0; i < passengersCount; i++) {
            passengers[i] = new Passenger(i, stops[randomProvider.next(0, stops.length)]);
        }
        return passengers;
    }

    public int getDaysCount() {
        return daysCount;
    }
    
    public int getNextDayNo(){
        if(!hasDaysLeft()){
            throw new IllegalStateException("No more days to simulate");
        }
        return dayNo+1;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Days count: ").append(daysCount).append("\n");
        sb.append("Passengers count: ").append(passengersCount).append("\n");
        sb.append("Stops capacity: ").append(stopsCapacity).append("\n");
        sb.append("Stops count: ").append(stops.length).append("\n");
        for(Stop stop : stops){
            sb.append("- ").append(stop).append("\n");
        }
        sb.append("Lines count: ").append(lines.length).append("\n");
        for(Line line : lines){
            sb.append(line).append("\n");
        }
        
        return sb.toString();
    }

    public StatisticHolder getTotalStats() {
        return statistic.getStats();
    }
}
