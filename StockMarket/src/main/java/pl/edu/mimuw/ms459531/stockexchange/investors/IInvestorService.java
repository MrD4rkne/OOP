package pl.edu.mimuw.ms459531.stockexchange.investors;

/**
 * IInvestorService interface provides the methods for the investor service.
 */
public interface IInvestorService extends IReadonlyInvestorService {
    /**
     * Registers a new investor and creates a new account.
     *
     * @param investor the investor
     * @return the registered investor
     */
    Investor registerInvestor(Investor investor);

    /**
     * Checks if the investor exists.
     *
     * @param investorId the investor id
     * @return if the investor exists
     */
    boolean doesInvestorExist(int investorId);

    /**
     * Adds funds to the investor account.
     *
     * @param investorId the investor id
     * @param amount     the amount to add
     */
    void addFunds(int investorId, int amount);

    /**
     * Removes funds from the investor account.
     *
     * @param investorId the investor id
     * @param amount     the amount to remove
     */
    void removeFunds(int investorId, int amount);

    /**
     * Gets the funds of the investor.
     *
     * @param investorId the investor id
     * @return the funds
     */
    int getFunds(int investorId);

    /**
     * Adds stock to the investor account.
     *
     * @param investorId the investor id
     * @param stockId    the stock id
     * @param amount     the amount to add
     */
    void addStock(int investorId, int stockId, int amount);

    /**
     * Checks if the investor has the stock.
     *
     * @param investorId the investor id
     * @param stockId    the stock id
     * @param amount     the amount
     * @return if the investor has the stock
     */
    boolean hasStock(int investorId, int stockId, int amount);

    /**
     * Removes stock from the investor account.
     *
     * @param investorId the investor id
     * @param stockId    the stock id
     * @param amount     the amount to remove
     */
    void removeStock(int investorId, int stockId, int amount);

    String toString();
}
