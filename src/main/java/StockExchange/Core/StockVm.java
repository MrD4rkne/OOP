package StockExchange.Core;

public class StockVm {
    private final int stockId;

    private final int amount;

    public StockVm(int stockId, int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
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

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockId +
                ", amount=" + amount +
                '}';
    }
}
