package stockMarket.stock;

import stockMarket.core.Wallet;

public class SingleStockWallet extends Wallet {
    private final int NEVER_ACCESSED = -1;

    private int stockAmount;
    private int processCounter;

    public SingleStockWallet(int investorId, int funds, int stockAmount) {
        super(investorId, funds);
        if(stockAmount < 0) {
            throw new IllegalArgumentException("Stock amount cannot be negative");
        }
        this.stockAmount = stockAmount;
        this.processCounter = NEVER_ACCESSED;
    }

    public void setProcessInfo(int processCounter, int funds, int stockAmount) {
        if(processCounter < 0) {
            throw new IllegalArgumentException("Process counter cannot be negative");
        }
        if(funds < 0) {
            throw new IllegalArgumentException("Funds cannot be negative");
        }
        if(stockAmount < 0) {
            throw new IllegalArgumentException("Stock amount cannot be negative");
        }

        this.processCounter = processCounter;
        this.stockAmount = stockAmount;
        this.funds = funds;
    }

    public int getProcessCounter() {
        return processCounter;
    }

    public boolean hasStock(int amount) {
        return stockAmount >= amount;
    }

    public int getStockAmount() {
        return stockAmount;
    }

    public void addStock(int possibleAmount) {
        if(possibleAmount < 0) {
            throw new IllegalArgumentException("Possible amount cannot be negative");
        }

        stockAmount += possibleAmount;
    }

    public void removeStock(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if(!hasStock(amount)) {
            throw new IllegalArgumentException("Not enough stocks");
        }

        stockAmount -= amount;
    }
}
