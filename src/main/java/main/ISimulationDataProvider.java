package main;

/**
 * ISimulationDataProvider interface provides a method to get the simulation data.
 */
public interface ISimulationDataProvider {
    SimulationData getData() throws InvalidDataException;
}
