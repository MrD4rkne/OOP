package StockExchange.Orders;

import StockExchange.Investors.Investor;

public abstract class Order implements Comparable<Order> {
    private final int id;

    private final OrderType type;
    
    private int amount;
    
    private final int stockId;
    
    private final int firstRoundNo;
    
    private final int limit;
    
    private final Investor investor;
    
    public Order(int id, OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
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

        this.id = id;
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

    public boolean doNeedToBeProcessedFullyAtOnce(){
        return false;
    }

    public int getId() {
        return id;
    }

    public void complete(int roundNo){
        complete(roundNo, amount);
    }
    
    public void complete(int roundNo, int amount){
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(amount > this.amount) {
            throw new IllegalArgumentException("Amount cannot be greater than order amount");
        }
        if(isExpired(roundNo))
            throw new IllegalStateException("Order is expired");

        this.amount -= amount;
    }
    
    /**
     * Compare orders based on the following criteria:
     * <p> 
     * 1. Firstly compare based on the limit if the order type is the same;
     * a) buy: larger limit is better,
     * b) sell: smaller limit is better;
     * <p>
     * 2. Then compare based on the round number; o1.roundNo < o2.roundNo => o1 < o2
     * <p>
     * 3. Finally, compare based on the order id.
     */
    @Override
    public int compareTo(Order o) {
        if(this.getType() == o.getType()) {

            int limitComparison;
            if (this.getType() == OrderType.BUY) {
                // Larger limit is better
                limitComparison = Integer.compare(o.getLimit(), this.getLimit());
            } else {
                // Smaller limit is better
                limitComparison = Integer.compare(this.getLimit(), o.getLimit());
            }

            if (limitComparison != 0) {
                return limitComparison;
            }
        }

        // Older order is better
        int roundComparison = Integer.compare(this.getFirstRoundNo(), o.getFirstRoundNo());
        if(roundComparison != 0){
            return roundComparison;
        }

        return Integer.compare(this.getId(), o.getId());
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", type=" + type +
                ", amount=" + amount +
                ", stockId=" + stockId +
                ", firstRoundNo=" + firstRoundNo +
                ", limit=" + limit +
                ", investor=" + investor +
                '}';
    }
}
