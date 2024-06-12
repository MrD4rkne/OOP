package stockMarket.orders;

public class GoodTillCancelledOrder extends Order{
    
    public GoodTillCancelledOrder(OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        this(0,type, investorId, stockId, amount, limit, firstRoundNo);
    }
    
    public GoodTillCancelledOrder(int id,OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockId, amount, limit, firstRoundNo);
    }
}
