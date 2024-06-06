package StockExchange.Investors;

public class Investor {
    private int balance;
    
    public Investor(int balance) {
        if(balance < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }
        this.balance = balance;
    }
    
    public int getBalance() {
        return balance;
    }
    
    public void addBalance(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        balance += amount;
    }
    
    public void subtractBalance(int amount) {
        if(amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if(balance < amount) {
            throw new IllegalArgumentException("Amount cannot be greater than balance");
        }
        balance -= amount;
    }
}
