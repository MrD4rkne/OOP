package Simulation.Core;

import Simulation.Common.Line;
import Simulation.Common.Stop;
import Simulation.Logs.ConsoleLogReporter;
import Simulation.Logs.ILogReporter;
import Simulation.Vehicles.Tram.TramLine;

import java.util.Objects;
import java.util.Scanner;

public class Main {
    public static void main(String [] args)
    {
        Simulation simulation = processInput();
        simulation.simulate();
    }

    private static Simulation processInput(){
        Scanner scanner = new Scanner(System.in);
        int numberOfDays = scanner.nextInt();
        Stop[] stops = getStops(scanner);
        int passengersCount = scanner.nextInt();
        Line[] lines = getTramLines(scanner,stops);
        return new Simulation(numberOfDays, passengersCount,stops,lines, new ConsoleLogReporter());
    }

    private static Stop[] getStops(Scanner scanner){
        int stopCapacity = scanner.nextInt();
        int numberOfStops = scanner.nextInt();
        Stop[] stops = new Stop[numberOfStops];
        for(int i = 0; i<numberOfStops; i++){
            String stopName = scanner.next();
            stops[i] = new Stop(stopName, stopCapacity);
        }
        return stops;
    }

    private static Line[] getTramLines(Scanner scanner,Stop[] stops){
        int tramCapacity = scanner.nextInt();
        int tramLinesCount = scanner.nextInt();
        Line[] lines = new Line[tramLinesCount];
        for(int i = 0; i<tramLinesCount; i++){
            lines[i] = getTramLine(i,scanner,stops,tramCapacity);
        }
        return lines;
    }

    private static Line getTramLine(int no,Scanner scanner, Stop[] stops, int tramsCapacity){
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

    private static Stop getStopWithName(Stop[] stops, String name){
        for (Stop stop : stops) {
            if (name.equals(stop.getName()))
                return stop;
        }
        return null;
    }
}
