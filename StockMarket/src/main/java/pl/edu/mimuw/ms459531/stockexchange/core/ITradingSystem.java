package pl.edu.mimuw.ms459531.stockexchange.core;

/**
 * ITradingSystem interface provides the methods for the trading system.
 */
public interface ITradingSystem {

    /**
     * Gets the number of rounds.
     *
     * @return if there is a next round
     */
    boolean hasNextRound();

    /**
     * Simulates the next round.
     */
    void nextRound();
}
