package Simulation.Statistic;

public abstract class Stat {
    private final String name;

    public Stat(String name){
        this.name= name;
    }
    
    public String getName(){
        return name;
    }

    /**
     * Resets the local values of the statistic
     */
    public abstract void resetLocal();
    
    /**
     * Returns the local values of the statistic
     * @return text representation of the local values
     */
    public abstract String toStringLocal();

    /**
     * Returns the total values of the statistic
     * @return text representation of the total values
     */
    public abstract String toStringTotal();

    public abstract String toString();
}
