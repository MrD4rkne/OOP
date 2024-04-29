package Simulation.Event;

import Simulation.Common.Vehicle;
import Simulation.Tram.Tram;

public interface IEventReporter {

    void reportTramFinishedRoute(Vehicle vehicle, int currentTime);

    void reportTramFinishedDay(Tram vehicle, int currentTime);
}
