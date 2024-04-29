package Simulation;

public class Segment {
    private final int index;
    private final Stop stop;
    public Segment(int index, Stop stop){
        this.index=index;
        this.stop=stop;
    }

    public int getIndex(){return index;}

    public Stop getStop(){
        return stop;
    }
}
