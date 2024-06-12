package stockMarket.investors;

public interface IInvestorService extends IReadonlyInvestorService {
    Investor registerInvestor(Investor investor);

    boolean doesInvestorExist(int investorId);

    void addFunds(int investorId, int amount);

    void removeFunds(int investorId, int amount);

    int getFunds(int investorId);

    void addStock(int investorId, int stockId, int amount);

    boolean hasStock(int investorId, int stockId, int amount);

    void removeStock(int investorId, int stockId, int amount);
    
    String toString();
}
