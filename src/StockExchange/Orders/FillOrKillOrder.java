package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class FillOrKillOrder extends ImmediateOrder {
    public FillOrKillOrder(OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
        super(type, investor, stockId, amount, limit, firstRoundNo);
    }

    @Override
    public void complete(int amount) {
        if (amount < this.getAmount()) {
            throw new IllegalArgumentException("Amount cannot be less than order amount");
        }
        super.complete(amount);
    }
}
