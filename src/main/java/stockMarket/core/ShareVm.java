package stockMarket.core;

import stockMarket.companies.StockCompany;

public record ShareVm(StockCompany stockCompany, int amount) {
    public ShareVm {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

    }

    @Override
    public String toString() {
        return "Stock{" +
                "stockId=" + stockCompany.toString() +
                ", amount=" + amount +
                '}';
    }
}
