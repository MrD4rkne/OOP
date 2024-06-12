package stockMarket.orders;

public class ImmediateOrder extends GoodTillEndOfTurnOrder{

    public ImmediateOrder(OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        this(0,type, investorId, stockId, amount, limit, firstRoundNo);
    }
    
    public ImmediateOrder(int id,OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockId, amount, limit, firstRoundNo, firstRoundNo);
    }
}
