package stockMarket.core;

import stockMarket.orders.Order;
import stockMarket.orders.OrderType;

public record TransactionInfo(Order buyOrder, Order sellOrder, int amount, int rate, int roundNo) {
    public TransactionInfo {
        if(buyOrder.getType() != OrderType.BUY) {
            throw new IllegalArgumentException("Buy order must be of type BUY");
        }
        if(sellOrder.getType() != OrderType.SALE) {
            throw new IllegalArgumentException("Sell order must be of type SALE");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if (rate <= 0) {
            throw new IllegalArgumentException("Rate cannot be non-positive");
        }
        if (roundNo < 0) {
            throw new IllegalArgumentException("Round number cannot be negative");
        }
    }

    public int getTotalValue() {
        return amount * rate;
    }
    
    @Override
    public int hashCode() {
        return buyOrder.hashCode() + sellOrder.hashCode() + amount + rate + roundNo;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if(hashCode() != obj.hashCode()) {
            return false;
        }
        
        if (!(obj instanceof TransactionInfo other)) {
            return false;
        }
        return buyOrder.equals(other.buyOrder) && sellOrder.equals(other.sellOrder) && amount == other.amount && rate == other.rate && roundNo == other.roundNo;
    }
    
    @Override
    public String toString() {
        return "stock: " + buyOrder.getStockCompany().name() + ", amount: " + amount + ", rate: " + rate + ", round: " + roundNo + " | sell order: " + sellOrder.getId() + " | buy order: " + buyOrder.getId();
    }
}
