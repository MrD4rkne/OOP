package Simulation.Client;

import Collection.MyList.IMyList;
import Collection.MyList.MyArrayList;
import Simulation.Core.Simulation;
import Simulation.Logs.ConsoleLogReporter;
import Simulation.Statistic.StatisticHolder;

import java.util.Scanner;

public class Main {
    public static void main(String [] args)
    {
        ISimulationProvider simulationProvider = new SystemInSimulationProvider(new Scanner(System.in));
        Simulation simulation = simulationProvider.create(new ConsoleLogReporter(), new RandomProvider());
        IMyList<StatisticHolder> stats = new MyArrayList<>(simulation.getDaysCount());
        
        displaySimulationParameters(simulation);
        System.out.println("Simulation started");
        System.out.println();
        
        while(simulation.hasDaysLeft()){
            System.out.println("#".repeat(40));
            System.out.println("Day " + (simulation.getNextDayNo()+1));
            StatisticHolder dailyStatistics = simulation.simulateDay();
            
            stats.add(dailyStatistics);
            displayDayStats(dailyStatistics);
        }

        System.out.println("Simulation is over");
        
        displayFinalStats(stats,simulation.getTotalStats());
    }
    
    private static void displaySimulationParameters(Simulation simulation){
        System.out.println("Simulation parameters:");
        System.out.println(simulation.toString());
    }
    
    private static void displayDayStats(StatisticHolder stats){
        System.out.println();
        System.out.println("#".repeat(40));
        
        System.out.println("Day is over");
        System.out.println(stats);
        
        System.out.println("#".repeat(40));
        System.out.println();
    }
    
    private static void displayFinalStats(IMyList<StatisticHolder> stats, StatisticHolder totalStats){
        System.out.println();
        System.out.println("#".repeat(40));
        System.out.println("Statistics:");
        System.out.println();
        
        for(int i = 0;i< stats.size(); i++){
            System.out.println("Day " + i + ": ");
            System.out.println(stats.get(i).toStringLocal());
            System.out.println();
        }
        
        System.out.println("Total:");
        System.out.println(totalStats.toStringTotal());
        
        System.out.println("#".repeat(40));
        System.out.println();
    }
}
