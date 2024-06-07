package StockExchange.Orders;

import StockExchange.Investors.Investor;

public abstract class Order implements Comparable<Order> {
    private final StockExchange.Orders.OrderType type;
    
    private int amount;
    
    private final int stockId;
    
    private final int firstRoundNo;
    
    private final int limit;
    
    private final Investor investor;
    
    public Order(StockExchange.Orders.OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(limit <= 0) {
            throw new IllegalArgumentException("Limit cannot be non-positive");
        }
        if(stockId < 0) {
            throw new IllegalArgumentException("Stock ID cannot be negative");
        }
        if(firstRoundNo < 0) {
            throw new IllegalArgumentException("First round number cannot be negative");
        }

        this.type = type;
        this.amount = amount;
        this.limit=limit;
        this.stockId = stockId;
        this.firstRoundNo = firstRoundNo;
        this.investor = investor;
    }
    
    public OrderType getType() {
        return type;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public int getLimit() { return limit; }
    
    public int getStockId() {
        return stockId;
    }
    
    public int getFirstRoundNo() {
        return firstRoundNo;
    }
    
    public Investor getInvestor() {
        return investor;
    }
    
    public boolean isExpired(int roundNo){
        return amount==0;
    }
    
    public void complete(int amount){
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if(amount > this.amount) {
            throw new IllegalArgumentException("Amount cannot be greater than order amount");
        }
        this.amount -= amount;
    }
    
    /**
     * Compare orders based on the following criteria:
     * <p>
     * 1. Buy orders are smaller than sell orders;
     * <p> 
     * 2. Firstly compare based on the limit;
     * a) buy: larger limit is better,
     * b) sell: smaller limit is better;
     * <p>
     * 4. Older orders are better;
     */
    @Override
    public int compareTo(Order o) {
        if(this.getType() != o.getType()){
                        
            return this.getType() == OrderType.BUY ? -1 : 1;
        }
        
        int limitComparison;
        if(this.getType() == OrderType.BUY){
            // Larger limit is better
            limitComparison = Integer.compare(o.getLimit(), this.getLimit());
        }
        else{
            // Smaller limit is better
            limitComparison = Integer.compare(this.getLimit(), o.getLimit());
        }
        
        if(limitComparison != 0){
            return limitComparison;
        }

        // Older order is better
        return Integer.compare(this.getFirstRoundNo(), o.getFirstRoundNo());
    }
}
