package stockMarket.investors;

import stockMarket.core.StockCompany;
import stockMarket.stock.Share;
import stockMarket.core.Wallet;

import java.util.ArrayList;
import java.util.List;

public class InvestorWallet extends Wallet {
    private final List<Share> stocks;

    public InvestorWallet(int investorId, StockCompany[] stockCompanies) {
        this(investorId,0,stockCompanies);
    }

    public InvestorWallet(int investorId, int funds, StockCompany[] stockCompanies) {
        super(investorId, funds);
        this.stocks = new ArrayList<>(stockCompanies.length);
        seedStocks(stockCompanies);
    }

    public boolean hasStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        validateStockId(stockId);

        return stocks.get(stockId).getAmount() >= amount;
    }

    public void addStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        validateStockId(stockId);
        
        stocks.get(stockId).addAmount(amount);
    }

    public void removeStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(!hasStocks(stockId, amount)) {
            throw new IllegalArgumentException("Not enough Stocks");
        }

        stocks.get(stockId).removeAmount(amount);
    }

    public int getStocksAmount(int stockId) {
        validateStockId(stockId);
        return stocks.get(stockId).getAmount();
    }

    public List<Share> getStocks() {
        return new ArrayList<>(stocks);
    }
    
    private void validateStockId(int stockId) {
        if(stockId < 0 || stockId >= stocks.size()) {
            throw new IllegalArgumentException("StockId is invalid");
        }
    }
    
    private void seedStocks(StockCompany[] stockCompanies) {
        for (int i = 0; i < stockCompanies.length; i++) {
            stocks.set(i, new Share(stockCompanies[i], 0));
        }
    }
}
