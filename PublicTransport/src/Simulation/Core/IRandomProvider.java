package Simulation.Core;

public interface IRandomProvider {
    /**
     * Returns the next random number.
     * @param lowBound  the lower bound of the random number
     * @param highBound  the upper bound of the random number
     * @return the next random number
     */
    int next(int lowBound, int highBound);
}
