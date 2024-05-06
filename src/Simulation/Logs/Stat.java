package Simulation.Logs;

public class Stat {
    private int defaultValue = 0;
    private int local;
    private int total;

    public Stat(){
        this(0);
    }

    public Stat(int defaultValue){
        this.defaultValue =defaultValue;
        local = defaultValue;
        total = defaultValue;
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
    
    public void resetLocal(){
        local= defaultValue;
    }
}
