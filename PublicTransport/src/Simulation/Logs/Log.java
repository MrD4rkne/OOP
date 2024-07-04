package Simulation.Logs;

import Simulation.Statistic.IStatisticService;

public abstract class Log {
    protected final int time;
    
    public Log(int time) {
        this.time = time;
    }
    
    public int getTime() {
        return time;
    }

    public void updateStatistic(IStatisticService statistic){
        
    }
    
    public abstract String toString();
}
