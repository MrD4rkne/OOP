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
    
    public float getLocalAverageTripsDuration(){
        return calcAverage(tripsDuration.getLocal(), tripsCount.getLocal());
    }

    public float getTotalAverageTripsDuration(){
        return calcAverage(tripsDuration.getTotal(), tripsCount.getTotal());
    }
    
    public float getLocalAverageWaitingDuration(){
        return calcAverage(waitingDuration.getLocal(), waitingCount.getLocal());
    }

    public float getTotalAverageWaitingDuration(){
        return calcAverage(waitingDuration.getTotal(), waitingCount.getTotal());
    }

    private float calcAverage(float nominator, float denominator){
        if(denominator == 0)
            return 0;
        return (float)nominator/denominator;
    }
    
    @Override
    public String toString() {
        String local =  String.format("Day: Trips: %d, Successful trips: %d, Forced ended trips: %d, Did not travel: %d, Routes: %d, Waiting: %d, Total waiting duration: %d, Average waiting: %f, Total trips duration: %d, Average trips duration: %f",
                tripsCount.getLocal(), successfulTripsCount.getLocal(), forcedEndedTripsCount.getLocal(), didNotTravelPassengersCount.getLocal(), routesCount.getLocal(), waitingCount.getLocal(), waitingDuration.getLocal(), getLocalAverageWaitingDuration(), tripsDuration.getLocal(), getLocalAverageTripsDuration());
        String total =  String.format("Total: Trips: %d, Successful trips: %d, Forced ended trips: %d, Did not travel: %d, Routes: %d, Waiting: %d, Total waiting duration: %d, Average waiting: %f, Total trips duration: %d, Average trips duration: %f",
                tripsCount.getTotal(), successfulTripsCount.getTotal(), forcedEndedTripsCount.getTotal(), didNotTravelPassengersCount.getTotal(), routesCount.getTotal(), waitingCount.getTotal(), waitingDuration.getTotal(), getTotalAverageWaitingDuration(), tripsDuration.getTotal(), getTotalAverageTripsDuration());
        return local + "\n" + total;
    }
}
