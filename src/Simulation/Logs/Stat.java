package Simulation.Logs;

public class Stat {
    private static final int DEFAULT_VALUE=0;
    private int local;
    private int total;

    public Stat(){
        this(DEFAULT_VALUE);
    }

    public Stat(int defaultValue){
        local=defaultValue;
        total=defaultValue;
    }

    public void increment(){
        increaseBy(1);
    }

    public void increaseBy(int val){
        local+=val;
        total+=val;
    }

    public int getLocal(){
        return local;
    }

    public int getTotal(){
        return total;
    }
}
