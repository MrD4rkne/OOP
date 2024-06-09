package StockExchange.Investors;

import StockExchange.Core.Stock;
import StockExchange.Core.Wallet;

import java.util.Map;
import java.util.TreeMap;

public class InvestorWallet extends Wallet {
    private final Map<Integer, Stock> Stocks;

    public InvestorWallet(int investorId) {
        this(investorId, 0);
    }

    public InvestorWallet(int investorId, int funds) {
        super(investorId, funds);
        this.Stocks = new TreeMap<>();
    }

    public boolean hasStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }

        return Stocks.getOrDefault(stockId, new Stock(stockId, 0)).getAmount() >= amount;
    }

    public void addStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }

        Stocks.put(stockId, Stocks.getOrDefault(stockId, new Stock(stockId, 0)));
        Stocks.get(stockId).addAmount(amount);
    }

    public void removeStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(!hasStocks(stockId, amount)) {
            throw new IllegalArgumentException("Not enough Stocks");
        }

        Stocks.get(stockId).removeAmount(amount);
    }

    public int getStocksAmount(int stockId) {
        return Stocks.getOrDefault(stockId, new Stock(stockId, 0)).getAmount();
    }
}
