package Simulation.Statistic;

public class SumStat extends Stat {
    static final int DEFAULT_VALUE = 0;

    private int local;
    private int total;
    
    public SumStat(SumStat stat){
        this(stat.getName(), stat.local, stat.total);
    }
    
    public SumStat(String name){
        this(name, DEFAULT_VALUE, DEFAULT_VALUE);
    }

    public SumStat(String name, int local, int total){
        super(name);
        this.local=local;
        this.total=total;
    }
    

    @Override
    public void resetLocal() {
        local=DEFAULT_VALUE;
    }

    @Override
    public String toStringLocal() {
        return getName() + ": " + local;
    }

    @Override
    public String toStringTotal() {
        return getName() + ": " + total;
    }

    @Override
    public String toString() {
        return getName() + " local: " + local + " total: " + total;
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