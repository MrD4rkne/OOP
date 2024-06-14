package main;

public interface ISimulationDataProvider {
    SimulationData buildSimulation() throws InvalidDataException;
}
