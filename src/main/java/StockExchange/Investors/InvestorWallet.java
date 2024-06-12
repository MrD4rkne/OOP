package StockExchange.Investors;

import StockExchange.Core.Stock;
import StockExchange.Core.Wallet;

import java.util.ArrayList;
import java.util.List;

public class InvestorWallet extends Wallet {
    private final List<Stock> stocks;

    public InvestorWallet(int investorId, int stocksCount) {
        this(investorId, stocksCount,0);
    }

    public InvestorWallet(int investorId, int stocksCount, int funds) {
        super(investorId, funds);
        this.stocks = new ArrayList<>(stocksCount);
        seedStocks(stocksCount);
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

    public List<Stock> getStocks() {
        return new ArrayList<>(stocks);
    }
    
    private void validateStockId(int stockId) {
        if(stockId < 0 || stockId >= stocks.size()) {
            throw new IllegalArgumentException("StockId is invalid");
        }
    }
    
    private void seedStocks(int stocksCount) {
        for (int i = 0; i < stocksCount; i++) {
            stocks.add(new Stock(i, 0));
        }
    }
}
