package Simulation.Event;

import Simulation.Common.Passenger;

public class PassengerTryEnterPrimaryStopEvent extends Event{
    private final Passenger passenger;
    
    public PassengerTryEnterPrimaryStopEvent(Passenger passenger, int time) {
        super(time);
        this.passenger=passenger;
    }

    @Override
    public void process(IEventQueue queue, IEventReporter reporter) {
        boolean didEnter = passenger.tryEnterStop(passenger.getPrimaryStop(), time);
        // TODO: Report the event
    }

    @Override
    public String toString() {
        return "";
    }
}
