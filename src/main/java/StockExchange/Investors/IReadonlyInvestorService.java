package StockExchange.Investors;

public interface IReadonlyInvestorService {
    Investor getInvestor(int investorId);

    int count();

    boolean hasFunds(int investorId, int amount);

    int getFunds(int investorId);

    boolean hasStock(int investorId, int stockId, int amount);

    int getStockAmount(int investorId, int stockId);

    InvestorWalletVm getWallet(int investorId);
}
