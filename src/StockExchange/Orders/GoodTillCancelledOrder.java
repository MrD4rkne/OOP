package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class GoodTillCancelledOrder extends Order{
    public GoodTillCancelledOrder(OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
        super(type, investor, stockId, amount, limit, firstRoundNo);
    }
}
