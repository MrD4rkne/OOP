package Simulation.Logs;

public abstract class Log {
    protected final int time;
    
    public Log(int time) {
        this.time = time;
    }
    
    public int getTime() {
        return time;
    }

    public void updateStatistic(IStatistic statistic){
        statistic.addEvent();
    }
    
    public abstract String toString();
}
