package Simulation.Passengers;

import Simulation.Events.Event;
import Simulation.Events.IEventQueue;
import Simulation.Logs.ILogReporter;

public class PassengerTryEnterPrimaryStopEvent extends Event {
    private final Passenger passenger;
    
    public PassengerTryEnterPrimaryStopEvent(Passenger passenger, int time) {
        super(time);
        this.passenger=passenger;
    }

    @Override
    public void process(IEventQueue queue, ILogReporter reporter) {
        boolean didEnter = passenger.tryEnterStop(passenger.getPrimaryStop(), time);
        if(didEnter){
            reporter.log(new PassengerEnteredPrimaryStopLog(time, passenger));
        }
        else{
            reporter.log(new PassengerCouldNotEnterPrimaryStopLog(time,passenger));
        }
    }
}
