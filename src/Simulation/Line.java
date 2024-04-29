package Simulation;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;

public abstract class Line {
    protected final int id;
    protected final IMyList<Segment> route;
    protected final int[] segmentDurations;
    protected final Vehicle[] vehicles;
    protected final Segment[] vehiclesSegment;
    protected final Direction[] directions;
    
    public Line(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        this.id = id;
        this.vehicles = new Vehicle[vehicleCount];
        this.vehiclesSegment = new Segment[vehicleCount];
        this.directions = new Direction[vehicleCount];
        this.route = new MyArrayList<Segment>(generateRoute(stops));
        this.segmentDurations = new int[segmentDurations.length];
        System.arraycopy(segmentDurations, 0, this.segmentDurations, 0, segmentDurations.length);
    }
    
    public int getId() {
        return id;
    }
    
    public int getVehicleCount() {
        return vehicles.length;
    }

    public Stop[] getStopsLeft(Vehicle vehicle) {
        Direction direction = getCurrentVehicleDirection(vehicle);
        Segment currentSegment = getCurrentVehicleSegment(vehicle);
        int stopsLeftCount = getStopsLeftCount(currentSegment.getIndex(), direction);
        Segment[] segmentsLeft = null;
        switch(direction){
            case UP:
                if(isFinalStop(currentSegment, direction)){
                    return new Stop[0];
                }
                int nextStopIndex = currentSegment.getIndex()+1;
                segmentsLeft = route.subArray(nextStopIndex);
                break;
            case DOWN:
                segmentsLeft = route.subArray(0, currentSegment.getIndex());
                break;
        }

        Stop[] stopsLeft = new Stop[segmentsLeft.length];
        for(int i = 0; i<stopsLeftCount; i++){
            stopsLeft[i] = segmentsLeft[i].getStop();
        }
        return stopsLeft;
    }

    public abstract void trySchedule(Vehicle vehicle,IEventQueue eventQueue, int time);

    public abstract void reportStop(Segment segment, IEventReporter eventReporter, int time);
    
    protected boolean isFinalStop(Segment segment, Direction direction) {
        return getStopsLeftCount(segment.getIndex(), direction) == 0;
    }
    
    protected Segment getNextSegment(Segment currentSegment, Direction direction){
        if(isFinalStop(currentSegment,direction))
            throw new IllegalStateException();
        switch(direction) {
            case UP:
                return route.get(currentSegment.getIndex() + 1);
            case DOWN:
                return route.get(currentSegment.getIndex() - 1);
        }
        throw new IllegalStateException();
    }

    protected int getLoopStopDuration(){
        return segmentDurations[segmentDurations.length-1];
    }

    protected int getStopsLeftCount(int index, Direction direction) {
        if(direction == Direction.UP)
            return route.size()-index -1;
        else if(direction == Direction.DOWN)
            return index;
        throw new UnsupportedOperationException("Invalid direction");
    }

    protected int calculateTimeUntilArrival(Segment from, Segment to, Direction direction){
        int time = 0;
        Segment current = from;
        while(!isFinalStop(current,direction)){
            if(current == to)
                break;

            time+= calculateTimeUntilNextStop(current,direction);
            current = getNextSegment(current,direction);
        }
        return time;
    }

    protected int calculateTimeUntilNextStop(Segment current, Direction direction){
        switch (direction){
            case UP:
                return segmentDurations[current.getIndex()];
            case DOWN:
                return segmentDurations[current.getIndex()-1];
        }
        throw new IllegalArgumentException();
    }

    protected Segment getCurrentVehicleSegment(Vehicle vehicle){
        return vehiclesSegment[vehicle.getLineVehicleNo()];
    }

    protected Direction getCurrentVehicleDirection(Vehicle vehicle){
        return directions[vehicle.getLineVehicleNo()];
    }

    protected Direction switchDirection(Direction direction){
        switch (direction){
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
        }
        throw new IllegalArgumentException();
    }

    private Segment[] generateRoute(Stop[] stops){
        Segment[] route = new Segment[stops.length];
        for(int i = 0; i<route.length; i++){
            route[i] = new Segment(i, stops[i]);
        }
        return route;
    }
}
