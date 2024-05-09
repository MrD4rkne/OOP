package Simulation.Logs;

public class AverageStat extends Stat{
    private final SumStat sum;
    private final SumStat count;
    
    public AverageStat(String name){
        super(name);
        sum = new SumStat("Sum");
        count = new SumStat("Count");
    }

    @Override
    public void resetLocal() {
        sum.resetLocal();
        count.resetLocal();
    }
    
    public void increaseSumBy(int val){
        sum.increaseBy(val);
    }
    
    public void incrementCount(){
        count.increment();
    }
    
    public float getLocalAverage(){
       return calculateAverage(sum.getLocal(), count.getLocal());
    }

    public float getTotalAverage(){
        return calculateAverage(sum.getTotal(), count.getTotal());
    }
    
    public String toStringLocal(){
        return getName()+ ": duration="+ sum.getLocal() + " count=" + count.getLocal() + " average=" + getLocalAverage();
    }
    
    public String toStringTotal(){
        return getName()+ ": duration="+ sum.getTotal() + " count=" + count.getTotal() + " average=" + getTotalAverage();
    }
    
    @Override
    public String toString() {
        return getName() + "\n"
                + "- Local: " + toStringLocal() + "\n"
                + "- Total: " + toStringTotal() + "\n";
    }

    private float calculateAverage(int sum , int count){
        if(count == 0)
            return 0;
        return ((float)sum) / count;
    }
}
