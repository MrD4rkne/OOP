package StockExchange.Core;

import StockExchange.Orders.Order;
import StockExchange.Orders.OrderType;

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
}
