package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class GoodTillCancelledOrder extends Order{
    public GoodTillCancelledOrder(int id,OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockId, amount, limit, firstRoundNo);
    }
}
