package Simulation;

import Simulation.Event.IEventQueue;
import Simulation.Event.IEventReporter;

public class TramLine extends Line{
    public TramLine(int id, int vehicleCount, Stop[] stops, int[] segmentDurations) {
        super(id, vehicleCount, stops, segmentDurations);
    }

    @Override
    public void trySchedule(Vehicle vehicle, Segment segment, Direction direction, IEventQueue eventQueue, int time) {

    }

    @Override
    public void reportStop(Segment segment, Direction direction, IEventReporter eventReporter, int time) {

    }
}
