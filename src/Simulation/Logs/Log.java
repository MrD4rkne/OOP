package Simulation.Logs;

public abstract class Log {
    protected final int time;
    
    public Log(int time) {
        this.time = time;
    }
    
    public int getTime() {
        return time;
    }
    
    public abstract String toString();
}
