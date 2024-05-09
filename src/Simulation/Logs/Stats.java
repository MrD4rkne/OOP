package Simulation.Logs;

import java.security.PrivateKey;

public class Stats {
    private final AverageStat trips;
    
    private final AverageStat waiting;

    private final SumStat successfulTripsCount;

    private final SumStat forcedEndedTripsCount;

    private final SumStat didNotTravelPassengersCount;

    private final SumStat routesCount;
    
    private final Stat[] stats;

    public Stats(){
        this(new AverageStat("Trips"), new AverageStat("Waiting"),
                new SumStat("Successful trips"), new SumStat("Forced ended trips"),
                new SumStat("Did not travel"), new SumStat("Routes"));
    }
    
    public Stats(AverageStat trips, AverageStat waiting, SumStat successfulTripsCount, SumStat forcedEndedTripsCount, SumStat didNotTravelPassengersCount, SumStat routesCount) {
        this.trips = trips;
        this.waiting = waiting;
        this.successfulTripsCount = successfulTripsCount;
        this.forcedEndedTripsCount = forcedEndedTripsCount;
        this.didNotTravelPassengersCount = didNotTravelPassengersCount;
        this.routesCount = routesCount;
        stats = new Stat[]{trips, waiting, successfulTripsCount, forcedEndedTripsCount, didNotTravelPassengersCount, routesCount};
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
    
    public void resetLocal(){
        for(Stat stat : stats){
            stat.resetLocal();
        }
    }
    
    public String toStringLocal(){
        StringBuilder sb = new StringBuilder();
        for(Stat stat : stats){
            sb.append(stat.toStringLocal());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public String toStringTotal(){
        StringBuilder sb = new StringBuilder();
        for(Stat stat : stats){
            sb.append(stat.toStringTotal());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Stat stat : stats){
            sb.append(stat.toString());
            sb.append("\n");
        }
        return sb.toString();
    }
    
    
}
