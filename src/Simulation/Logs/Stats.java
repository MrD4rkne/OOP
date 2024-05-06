package Simulation.Logs;

public class Stats {
    private final Stat tripsCount;

    private final Stat tripsDuration;

    private final Stat waitingCount;

    private final Stat waitingDuration;

    private final Stat successfulTripsCount;

    private final Stat forcedEndedTripsCount;

    private final Stat didNotTravelPassengersCount;

    private final Stat routesCount;
    
    public Stats(Stats stats) {
        this(stats.getTripsCount(), stats.getTripsDuration(),
                stats.getWaitingCount(), stats.getWaitingDuration(),
                stats.getSuccessfulTripsCount(), stats.getForcedEndedTripsCount(),
                stats.getDidNotTravelPassengersCount(), stats.getRoutesCount());
    }

    public Stats(){
        this(new Stat(), new Stat(), new Stat(), new Stat(), new Stat(), new Stat(), new Stat(), new Stat());
    }
    
    public Stats(Stat tripsCount, Stat tripsDuration, Stat waitingCount, Stat waitingDuration, Stat successfulTripsCount, Stat forcedEndedTripsCount, Stat didNotTravelPassengersCount, Stat routesCount) {
        this.tripsCount = tripsCount;
        this.tripsDuration = tripsDuration;
        this.waitingCount = waitingCount;
        this.waitingDuration = waitingDuration;
        this.successfulTripsCount = successfulTripsCount;
        this.forcedEndedTripsCount = forcedEndedTripsCount;
        this.didNotTravelPassengersCount = didNotTravelPassengersCount;
        this.routesCount = routesCount;
    }
    
    public Stat getTripsCount() {
        return tripsCount;
    }
    
    public Stat getTripsDuration() {
        return tripsDuration;
    }
    
    public Stat getWaitingCount() {
        return waitingCount;
    }
    
    public Stat getWaitingDuration() {
        return waitingDuration;
    }
    
    public Stat getSuccessfulTripsCount() {
        return successfulTripsCount;
    }
    
    public Stat getForcedEndedTripsCount() {
        return forcedEndedTripsCount;
    }
    
    public Stat getDidNotTravelPassengersCount() {
        return didNotTravelPassengersCount;
    }
    
    public Stat getRoutesCount() {
        return routesCount;
    }
    
    public Stats copy(){
        return new Stats(this);
    }
    
    public int getLocalAverageTripsDuration(){
        if(tripsCount.getLocal() == 0)
            return 0;
        return tripsDuration.getLocal() / tripsCount.getLocal();
    }

    public int getTotalAverageTripsDuration(){
        if(tripsCount.getTotal() == 0)
            return 0;
        return tripsDuration.getTotal() / tripsCount.getTotal();
    }
    
    public int getLocalAverageWaitingDuration(){
        if(waitingCount.getLocal() == 0)
            return 0;
        return waitingDuration.getLocal() / waitingCount.getLocal();
    }

    public int getTotalAverageWaitingDuration(){
        if(waitingCount.getTotal() == 0)
            return 0;
        return waitingDuration.getTotal() / waitingCount.getTotal();
    }
    
    @Override
    public String toString() {
        String local =  String.format("Day: Trips: %d, Successful trips: %d, Forced ended trips: %d, Did not travel: %d, Routes: %d, Waiting: %d, Total waiting duration: %d, Average waiting: %d, Total trips duration: %d, Average trips duration: %d",
                tripsCount.getLocal(), successfulTripsCount.getLocal(), forcedEndedTripsCount.getLocal(), didNotTravelPassengersCount.getLocal(), routesCount.getLocal(), waitingCount.getLocal(), waitingDuration.getLocal(), getLocalAverageWaitingDuration(), tripsDuration.getLocal(), getLocalAverageTripsDuration());
        String total =  String.format("Total: Trips: %d, Successful trips: %d, Forced ended trips: %d, Did not travel: %d, Routes: %d, Waiting: %d, Total waiting duration: %d, Average waiting: %d, Total trips duration: %d, Average trips duration: %d",
                tripsCount.getTotal(), successfulTripsCount.getTotal(), forcedEndedTripsCount.getTotal(), didNotTravelPassengersCount.getTotal(), routesCount.getTotal(), waitingCount.getTotal(), waitingDuration.getTotal(), getTotalAverageWaitingDuration(), tripsDuration.getTotal(), getTotalAverageTripsDuration());
        return local + "\n" + total;
    }
}
