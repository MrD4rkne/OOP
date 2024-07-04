package Simulation.Passengers;

import Simulation.Core.IRandomProvider;
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
    public void process(IEventQueue queue, ILogReporter reporter, IRandomProvider randomProvider) {
        boolean canEnter = passenger.getPrimaryStop().hasSpace();
        if(canEnter){
            passenger.enter(passenger.getPrimaryStop(), time, reporter);
        }
        else{
            reporter.log(new PassengerCouldNotEnterPrimaryStopLog(time,passenger, passenger.getPrimaryStop()));
        }
    }
}
