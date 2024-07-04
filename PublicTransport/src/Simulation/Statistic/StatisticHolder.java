package Simulation.Statistic;

public class StatisticHolder {
    private final AverageStat trips;
    
    private final AverageStat waiting;

    private final SumStat successfulTripsCount;

    private final SumStat forcedEndedTripsCount;

    private final SumStat didNotTravelPassengersCount;

    private final SumStat routesCount;
    
    private final Stat[] stats;
    
    public StatisticHolder(StatisticHolder stats){
        this(stats.trips, stats.waiting, stats.successfulTripsCount, stats.forcedEndedTripsCount, stats.didNotTravelPassengersCount, stats.routesCount);
    }

    public StatisticHolder(){
        this(new AverageStat("Trips"), new AverageStat("Waiting"),
                new SumStat("Successful trips"), new SumStat("Forced ended trips"),
                new SumStat("Did not travel"), new SumStat("Routes"));
    }
    
    public StatisticHolder(AverageStat trips, AverageStat waiting, SumStat successfulTripsCount, SumStat forcedEndedTripsCount, SumStat didNotTravelPassengersCount, SumStat routesCount) {
        this.trips = new AverageStat(trips);
        this.waiting = new AverageStat(waiting);
        this.successfulTripsCount = new SumStat(successfulTripsCount);
        this.forcedEndedTripsCount = new SumStat(forcedEndedTripsCount);
        this.didNotTravelPassengersCount = new SumStat(didNotTravelPassengersCount);
        this.routesCount = new SumStat(routesCount);
        stats = new Stat[]{this.trips, this.waiting, this.successfulTripsCount, this.forcedEndedTripsCount, this.didNotTravelPassengersCount, this.routesCount};
    }
    
    public AverageStat getTrips() {
        return trips;
    }
    
    public AverageStat getWaiting() {
        return waiting;
    }
    
    public SumStat getSuccessfulTripsCount() {
        return successfulTripsCount;
    }
    
    public SumStat getForcedEndedTripsCount() {
        return forcedEndedTripsCount;
    }
    
    public SumStat getDidNotTravelPassengersCount() {
        return didNotTravelPassengersCount;
    }
    
    public SumStat getRoutesCount() {
        return routesCount;
    }

    /**
     * Resets all local values of the stats
     */
    public void resetLocal(){
        for(Stat stat : stats){
            stat.resetLocal();
        }
    }

    /**
     * Generates a string representation of the local values of the stats
     */
    
    public String toStringLocal(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < stats.length; i++){
            sb.append(stats[i].toStringLocal());
            if(i != stats.length - 1){
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    /**
     * Generates a string representation of the total values of the stats
     */
    
    public String toStringTotal(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < stats.length; i++){
            sb.append(stats[i].toStringTotal());
            if(i != stats.length - 1){
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < stats.length; i++){
            sb.append(stats[i].toString());
            if(i != stats.length - 1){
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    
}
