package Simulation.Client;

import Simulation.Common.Line;
import Simulation.Common.Stop;
import Simulation.Core.IRandomProvider;
import Simulation.Core.Simulation;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.Tram.TramLine;

import java.util.Scanner;

public class SystemInSimulationProvider implements ISimulationProvider {
    private final Scanner scanner;
    
    public SystemInSimulationProvider(Scanner scanner){
        this.scanner = scanner;
    }
    
    @Override
    public Simulation create(ILogReporter logReporter, IRandomProvider randomProvider) {
        int numberOfDays = scanner.nextInt();
        int stopCapacity = scanner.nextInt();
        Stop[] stops = getStops(stopCapacity);
        int passengersCount = scanner.nextInt();
        int tramCapacity = scanner.nextInt();
        Line[] lines = getTramLines(stops, tramCapacity);
        return new Simulation(numberOfDays, passengersCount,stopCapacity, tramCapacity,stops,lines, logReporter, randomProvider);
    }

    private Stop[] getStops(int stopCapacity){
        int numberOfStops = scanner.nextInt();
        Stop[] stops = new Stop[numberOfStops];
        for(int i = 0; i<numberOfStops; i++){
            String stopName = scanner.next();
            stops[i] = new Stop(stopName, stopCapacity);
        }
        return stops;
    }

    private Line[] getTramLines(Stop[] stops, int tramCapacity){
        int tramLinesCount = scanner.nextInt();
        Line[] lines = new Line[tramLinesCount];
        for(int i = 0; i<tramLinesCount; i++){
            lines[i] = getTramLine(i,stops,tramCapacity);
        }
        return lines;
    }

    private Line getTramLine(int no,Stop[] stops, int tramsCapacity){
        int tramsCount = scanner.nextInt();
        int stopsCount = scanner.nextInt();
        Stop[] lineStops = new Stop[stopsCount];
        int[] stopsIntervals = new int[stopsCount];
        for(int i = 0; i<stopsCount; i++){
            lineStops[i]= getStopWithName(stops, scanner.next());
            stopsIntervals[i] = scanner.nextInt();
        }
        return new TramLine(no,tramsCount,lineStops,stopsIntervals,tramsCapacity);
    }

    private Stop getStopWithName(Stop[] stops, String name){
        for (Stop stop : stops) {
            if (name.equals(stop.getName()))
                return stop;
        }
        throw new IllegalArgumentException("Stop with name " + name + " not found");
    }
}
