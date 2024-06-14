package stockMarket.orders;

import stockMarket.companies.StockCompany;

/**
 * Represents fill or kill order.
 * It must be fully processed at once, or it will be canceled.
 */
public class FillOrKillOrder extends Order{
    
    public FillOrKillOrder(OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        this(0,type, investorId, stockCompany, amount, limit, firstRoundNo);
    }
    
    public FillOrKillOrder(int id,OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockCompany, amount, limit, firstRoundNo);
    }

    @Override
    public boolean doNeedToBeProcessedFullyAtOnce() {
        return true;
    }
    
    @Override
    public boolean isExpired(int roundNo) {
        if(super.isExpired(roundNo)) {
            return true;
        }
        
        return roundNo>getFirstRoundNo();
    }

    @Override
    protected String shortName(){
        return "FK";
    }
}
