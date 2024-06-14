package stockMarket.core;

public abstract class Wallet {
    private final int investorId;

    protected int funds;

    public Wallet(int investorId, int funds) {
        if(funds < 0) {
            throw new IllegalArgumentException("Funds cannot be negative");
        }

        this.investorId = investorId;
        this.funds = funds;
    }

    public int getInvestorId() {
        return investorId;
    }

    public int getFunds() {
        return funds;
    }

    public boolean hasFunds(int amount) {
        return funds >= amount;
    }

    public void addFunds(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.funds += amount;
    }

    public void removeFunds(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if(!hasFunds(amount)) {
            throw new IllegalArgumentException("Not enough funds");
        }

        funds -= amount;
    }
}
