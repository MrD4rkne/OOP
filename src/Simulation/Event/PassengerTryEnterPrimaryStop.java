package Simulation.Event;

import Simulation.Passenger;

public class PassengerTryEnterPrimaryStop extends Event{
    private final Passenger passenger;
    
    public PassengerTryEnterPrimaryStop(Passenger passenger, int time) {
        super(time);
        this.passenger=passenger;
    }

    @Override
    public void process(IEventQueue queue, IEventReporter reporter) {
        boolean didEnter = passenger.enterStop(passenger.getPrimaryStop(), time);
        // TODO: Report the event
    }

    @Override
    public String toString() {
        return "";
    }
}
