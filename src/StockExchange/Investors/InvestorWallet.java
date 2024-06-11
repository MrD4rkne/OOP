package StockExchange.Investors;

import StockExchange.Core.Stock;
import StockExchange.Core.Wallet;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class InvestorWallet extends Wallet {
    private final List<Stock> stocks;

    public InvestorWallet(int investorId, int stocksCount) {
        this(investorId, stocksCount,0);
    }

    public InvestorWallet(int investorId, int stocksCount, int funds) {
        super(investorId, funds);
        this.stocks = new TreeMap<>();
    }

    public boolean hasStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }

        return stocks.getOrDefault(stockId, new Stock(stockId, 0)).getAmount() >= amount;
    }

    public void addStocks(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }

        stocks.put(stockId, stocks.getOrDefault(stockId, new Stock(stockId, 0)));
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
        return stocks.getOrDefault(stockId, new Stock(stockId, 0)).getAmount();
    }
}
