package Simulation;

import Collection.IMyList;
import Collection.MyArrayList;
import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;

import java.util.Arrays;

public abstract class Line {
    protected final int id;
    protected final IMyList<Segment> route;
    protected final int[] segmentDurations;
    protected final int vehicleCount;
    
    public Line(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        this.id = id;
        this.vehicleCount = vehicleCount;
        this.route = new MyArrayList<Segment>(generateRoute(stops));
        this.segmentDurations = new int[segmentDurations.length];
        System.arraycopy(segmentDurations, 0, this.segmentDurations, 0, segmentDurations.length);
    }
    
    public int getId() {
        return id;
    }
    
    public int getVehicleCount() {
        return vehicleCount;
    }
    
    public Stop[] getStopsLeft(Segment currentSegment, Direction direction) {
        if(!doesSegmentIsInRoute(currentSegment)){
            throw new IllegalArgumentException("Segment does not belong to this line's route.");
        }

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

    public abstract void trySchedule(Vehicle vehicle, Segment segment, Direction direction, IEventQueue eventQueue, int time);

    public abstract void reportStop(Segment segment, Direction direction, IEventReporter eventReporter, int time);
    
    private boolean isFinalStop(Segment segment, Direction direction) {
        return getStopsLeftCount(segment.getIndex(), direction) == 0;
    }
    
    private Segment getNextSegment(Segment currentSegment, Direction direction){
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
    
    private int getTimeToNextStopDuration(Segment segment, Direction direction) {
        if(direction == Direction.UP)
            return segmentDurations[segment.getIndex()];
        else if(direction == Direction.DOWN)
            return segmentDurations[segment.getIndex()-1];
        throw new UnsupportedOperationException("Invalid direction");
    }
    
    private Direction switchDirection(Direction direction) {
        switch(direction){
            case UP:
                return Direction.DOWN;
            case DOWN:
                return Direction.UP;
        }
        throw new UnsupportedOperationException("Invalid direction");
    }

    private int getLoopStopDuration(){
        return segmentDurations[segmentDurations.length-1];
    }

    private boolean doesSegmentIsInRoute(Segment segment){
        return segment.getIndex() >=0 && segment.getIndex() < route.size() && segment == route.get(segment.getIndex());
    }

    private int getStopsLeftCount(int index, Direction direction) {
        if(direction == Direction.UP)
            return route.size()-index -1;
        else if(direction == Direction.DOWN)
            return index;
        throw new UnsupportedOperationException("Invalid direction");
    }

    private Segment[] generateRoute(Stop[] stops){
        Segment[] route = new Segment[stops.length];
        for(int i = 0; i<route.length; i++){
            route[i] = new Segment(i, stops[i]);
        }
        return route;
    }
}
