package Simulation;

import java.util.Arrays;

public class Line {
    private final int id;
    private final Stop[] stops;
    private final int[] segmentDurations;
    private final int vehicleCount;
    
    public Line(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        this.id = id;
        this.vehicleCount = vehicleCount;
        this.stops = new Stop[stops.length];
        System.arraycopy(stops, 0, this.stops, 0, stops.length);
        this.segmentDurations = new int[segmentDurations.length];
        System.arraycopy(segmentDurations, 0, this.segmentDurations, 0, segmentDurations.length);
    }
    
    public int getId() {
        return id;
    }
    
    public int getVehicleCount() {
        return vehicleCount;
    }
    
    public Stop[] getStopsLeft(int index, Direction direction) {
        int stopsLeftCount = getStopsLeftCount(index, direction);
        if(direction == Direction.UP) {
            int stopsToSkip = stops.length - stopsLeftCount;
            return Arrays.stream(stops).skip(stopsToSkip).toArray(Stop[]::new);
        }
        else if(direction == Direction.DOWN)
            return Arrays.stream(stops).limit(stopsLeftCount).toArray(Stop[]::new);
        throw new UnsupportedOperationException("Invalid direction");
    }
    
    public int getStartIndex(Direction direction){
        if(direction == Direction.UP)
            return 0;
        else if(direction == Direction.DOWN)
            return stops.length-1;
        throw new UnsupportedOperationException("Invalid direction");
    }
    
    public boolean isLastStop(int index, Direction direction) {
        return getStopsLeftCount(index, direction) == 0;
    }
    
    public int getNextStopIndex(int index, Direction direction){
        if(direction == Direction.UP)
            return index+1;
        else if(direction == Direction.DOWN)
            return index-1;
        throw new UnsupportedOperationException("Invalid direction");
    }
    
    public int getTimeToNextStopDuration(int index, Direction direction) {
        if(direction == Direction.UP)
            return segmentDurations[index];
        else if(direction == Direction.DOWN)
            return segmentDurations[index-1];
        throw new UnsupportedOperationException("Invalid direction");
    }
    
    public Direction switchDirection(Direction direction) {
        if(direction == Direction.UP)
            return Direction.DOWN;
        else if(direction == Direction.DOWN)
            return Direction.UP;
        throw new UnsupportedOperationException("Invalid direction");
    }

    public int getTimeToStop(int currentStopIndex, Direction direction, Stop stop) {
        int time = 0;
        while(!isLastStop(currentStopIndex, direction)) {
            time += getTimeToNextStopDuration(currentStopIndex, direction);
            if(stops[getNextStopIndex(currentStopIndex, direction)] == stop)
                return time;
            currentStopIndex += direction == Direction.UP ? 1 : -1;
        }
        Direction switchedDirection = switchDirection(direction);
        return time + getLoopStopDuration() + getTimeToStop(getStartIndex(switchedDirection), switchedDirection, stop);
    }
    
    public Stop getStop(int index){
        return stops[index];
    }
    
    
    private int getStopsLeftCount(int index, Direction direction) {
        if(direction == Direction.UP)
            return stops.length-index -1;
        else if(direction == Direction.DOWN)
            return index;
        throw new UnsupportedOperationException("Invalid direction");
    }
    
    public int getLoopStopDuration(){
        return segmentDurations[segmentDurations.length-1];
    }
}
