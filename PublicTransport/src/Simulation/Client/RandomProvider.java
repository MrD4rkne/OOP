package Simulation.Client;

import Simulation.Core.IRandomProvider;

public class RandomProvider implements IRandomProvider {

    @Override
    public int next(int lowBound, int highBound) {
        return Losowanie.losuj(lowBound, highBound);
    }
}
