package StockExchange.Core;

public class Stock {
    private final int stockId;

    private int amount;

    public Stock(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(stockId < 0) {
            throw new IllegalArgumentException("Stock ID cannot be negative");
        }

        this.stockId = stockId;
        this.amount = amount;
    }

    public int getStockId() {
        return stockId;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }

        this.amount += amount;
    }

    public void removeAmount(int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(this.amount < amount) {
            throw new IllegalArgumentException("Not enough Stocks");
        }

        this.amount -= amount;
    }
}
