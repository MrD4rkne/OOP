package StockExchange.Orders;

public class ImmediateOrder extends GoodTillEndOfTurnOrder{
    
    public ImmediateOrder(int id,OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockId, amount, limit, firstRoundNo, firstRoundNo);
    }
}
