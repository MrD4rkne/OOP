package stockMarket.orders;

import stockMarket.core.StockCompany;

public class ImmediateOrder extends GoodTillEndOfTurnOrder{

    public ImmediateOrder(OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        this(0,type, investorId, stockCompany, amount, limit, firstRoundNo);
    }
    
    public ImmediateOrder(int id,OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockCompany, amount, limit, firstRoundNo, firstRoundNo);
    }

    @Override
    protected String acronim(){
        return "IM";
    }
}
