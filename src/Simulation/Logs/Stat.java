package Simulation.Logs;

public abstract class Stat {
    private final String name;

    public Stat(String name){
        this.name= name;
    }
    
    public String getName(){
        return name;
    }
    
    public abstract void resetLocal();

    public abstract String toStringLocal();

    public abstract String toStringTotal();

    public abstract String toString();
}
