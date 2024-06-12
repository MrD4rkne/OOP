package main;

public interface ISimulationBuilder {
    SimulationData buildSimulation() throws InvalidDataException;
}
