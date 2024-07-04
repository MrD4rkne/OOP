package pl.edu.mimuw.ms459531.stockexchange.core;

import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;

public class Share {
    private final StockCompany stockCompany;

    private int amount;

    public Share(StockCompany stockCompany, int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.stockCompany = stockCompany;
        this.amount = amount;
    }

    public StockCompany getStockCompany() {
        return stockCompany;
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }

        this.amount += amount;
    }

    public void removeAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if (this.amount < amount) {
            throw new IllegalArgumentException("Not enough Stocks");
        }

        this.amount -= amount;
    }
}
