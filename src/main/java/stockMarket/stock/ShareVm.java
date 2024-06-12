package stockMarket.stock;

import stockMarket.core.StockCompany;

public class ShareVm {
    private final StockCompany stockCompany;

    private final int amount;

    public ShareVm(StockCompany stockCompany, int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        
        this.amount = amount;
        this.stockCompany = stockCompany;
    }

    public StockCompany getStockCompany() {
        return stockCompany;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockCompany.toString() +
                ", amount=" + amount +
                '}';
    }
}
