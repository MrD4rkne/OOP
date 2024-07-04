package Simulation.Client;

import Simulation.Core.IRandomProvider;
import Simulation.Core.Simulation;
import Simulation.Logs.ILogReporter;

public interface ISimulationProvider {
    Simulation create(ILogReporter logReporter, IRandomProvider randomProvider);
}
