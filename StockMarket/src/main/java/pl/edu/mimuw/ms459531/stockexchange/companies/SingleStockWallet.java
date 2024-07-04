package pl.edu.mimuw.ms459531.stockexchange.companies;

import pl.edu.mimuw.ms459531.stockexchange.core.Wallet;

/**
 * SingleStockWallet class provides the methods for reading and modifying the wallet data of investor.
 * It is used for storing the wallet data of investor for a single stock during processing the orders.
 */
public class SingleStockWallet extends Wallet {
    private final int NEVER_ACCESSED = -1;

    private int stockAmount;
    private int processCounter;
    private int roundNo;

    public SingleStockWallet(int investorId, int funds, int stockAmount) {
        super(investorId, funds);
        if (stockAmount < 0) {
            throw new IllegalArgumentException("Stock amount cannot be negative");
        }
        this.stockAmount = stockAmount;
        this.processCounter = NEVER_ACCESSED;
    }

    public void setProcessInfo(int roundNo, int processCounter, int funds, int stockAmount) {
        if (roundNo < 0) {
            throw new IllegalArgumentException("Round number cannot be negative");
        }
        if (processCounter < 0) {
            throw new IllegalArgumentException("Process counter cannot be negative");
        }
        if (funds < 0) {
            throw new IllegalArgumentException("Funds cannot be negative");
        }
        if (stockAmount < 0) {
            throw new IllegalArgumentException("Stock amount cannot be negative");
        }
        this.roundNo = roundNo;
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
        if (possibleAmount < 0) {
            throw new IllegalArgumentException("Possible amount cannot be negative");
        }

        stockAmount += possibleAmount;
    }

    public void removeStock(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (!hasStock(amount)) {
            throw new IllegalArgumentException("Not enough stocks");
        }

        stockAmount -= amount;
    }

    public int getRoundNo() {
        return roundNo;
    }
}
