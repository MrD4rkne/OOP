package StockExchange.Investors;

public interface IInvestorService {
    Investor registerInvestor(Investor investor);

    Investor getInvestor(int investorId);

    int count();

    boolean doesInvestorExist(int investorId);

    void addFunds(int investorId, int amount);

    boolean hasFunds(int investorId, int amount);

    void removeFunds(int investorId, int amount);

    int getFunds(int investorId);

    void addStock(int investorId, int stockId, int amount);

    boolean hasStock(int investorId, int stockId, int amount);

    void removeStock(int investorId, int stockId, int amount);

    int getStockAmount(int investorId, int stockId);

    InvestorWalletVm getWallet(int investorId);
}
