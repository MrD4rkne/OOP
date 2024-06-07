package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class GoodTillCancelledOrder extends Order{
    public GoodTillCancelledOrder(int id,OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
        super(id,type, investor, stockId, amount, limit, firstRoundNo);
    }
}
