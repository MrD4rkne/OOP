package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class ImmediateOrder extends Order{
    
    private boolean wasModified;
    
    public ImmediateOrder(OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
        super(type, investor, stockId, amount, limit, firstRoundNo);
        wasModified = false;
    }
    
    @Override
    public boolean isExpired(int roundNo) {
        if(super.isExpired(roundNo)) {
            return true;
        }

        if(roundNo > getFirstRoundNo()) {
            return !wasModified;
        }
        return false;
    }
    
    @Override
    public void complete(int amount) {
        super.complete(amount);
        wasModified = true;
    }
}
