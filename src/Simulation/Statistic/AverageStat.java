package Simulation.Statistic;

public class AverageStat extends Stat {
    private final SumStat sum;
    private final SumStat count;

    public AverageStat(AverageStat stat){
        this(stat.getName(), stat.sum.getLocal(), stat.count.getLocal(), stat.sum.getTotal(), stat.count.getTotal());
    }

    public AverageStat(String name, int localSum, int localCount, int totalSum, int totalCount){
        super(name);
        sum = new SumStat("Sum", localSum, totalSum);
        count = new SumStat("Count", localCount, totalCount);
    }
    
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
        return getName()+ ": "+ toStringLocalWithoutName();
    }

    public String toStringLocalWithoutName(){
        return "duration="+ sum.getLocal() + " count=" + count.getLocal() + " average=" + getLocalAverage();
    }
    
    public String toStringTotal(){
        return getName()+ ": "+ toStringTotalWithoutName();
    }

    private String toStringTotalWithoutName(){
        return "duration="+ sum.getTotal() + " count=" + count.getTotal() + " average=" + getTotalAverage();
    }
    
    @Override
    public String toString() {
        return getName() + "\n"
                + "- Local: " + toStringLocalWithoutName() + "\n"
                + "- Total: " + toStringTotalWithoutName() + "\n";
    }

    private float calculateAverage(int sum , int count){
        if(count == 0)
            return 0;
        return ((float)sum) / count;
    }
}
