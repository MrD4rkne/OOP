package stockMarket.orders;

import stockMarket.core.StockCompany;

import javax.swing.*;

public abstract class Order implements Comparable<Order> {
    private int id;

    private final OrderType type;
    
    private int amount;
    
    private final StockCompany stockCompany;
    
    private final int firstRoundNo;
    
    private final int limit;
    
    private final int investorId;

    private boolean isCancelled;

    public Order(OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo){
        this(-1, type, investorId, stockCompany, amount, limit, firstRoundNo);
    }
    
    public Order(int id, OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(limit <= 0) {
            throw new IllegalArgumentException("Limit cannot be non-positive");
        }
        if(firstRoundNo < 0) {
            throw new IllegalArgumentException("First round number cannot be negative");
        }
        if(investorId < 0) {
            throw new IllegalArgumentException("Investor ID cannot be negative");
        }

        this.id = id;
        this.type = type;
        this.amount = amount;
        this.limit=limit;
        this.stockCompany = stockCompany;
        this.firstRoundNo = firstRoundNo;
        this.investorId = investorId;
        this.isCancelled = false;
    }
    
    public int getInvestorId() {
        return investorId;
    }
    
    public OrderType getType() {
        return type;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public int getLimit() { return limit; }
    
    public StockCompany getStockCompany() {
        return stockCompany;
    }
    
    public int getStockId() {
        return stockCompany.getId();
    }
    
    public int getFirstRoundNo() {
        return firstRoundNo;
    }
    
    public boolean isExpired(int roundNo){
        if(isCancelled)
            return true;
        return amount==0;
    }

    public void cancel(){
        isCancelled = true;
    }

    public boolean doNeedToBeProcessedFullyAtOnce(){
        return false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public void complete(int roundNo, int amount){
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(amount > this.amount) {
            throw new IllegalArgumentException("Amount cannot be greater than order amount");
        }
        if(isExpired(roundNo))
            throw new IllegalStateException("Order is expired: " + this);

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
    public int hashCode() {
        return id;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Order other)) {
            return false;
        }
        return id== other.getId() && type == other.getType() && amount == other.getAmount() 
                && getStockCompany().equals(other.getStockCompany()) && firstRoundNo == other.getFirstRoundNo()
                && limit == other.getLimit()
                && investorId == other.getInvestorId();
    }
    
    protected abstract String acronim();

    @Override
    public String toString() {
        return acronim() + " id=" + id + ", type=" + type + ", amount=" + amount + ", company=" + stockCompany.getName() + ", firstRoundNo="
                + firstRoundNo + ", limit=" + limit + ", investorId=" + investorId;
    }
}
