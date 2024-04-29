package Simulation.Common;

public class Segment {
    private final int index;
    private final Stop stop;
    private final int timeToNextStop;
    
    public Segment(int index, Stop stop, int timeToNextStop){
        this.index=index;
        this.stop=stop;
        this.timeToNextStop=timeToNextStop;
    }

    public int getIndex(){return index;}

    public Stop getStop(){
        return stop;
    }
    
    public int getTimeToNextStop(){
        return timeToNextStop;
    }
}
