package stockMarket.orders;

public class FillOrKillOrder extends Order{
    
    public FillOrKillOrder(OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        this(0,type, investorId, stockId, amount, limit, firstRoundNo);
    }
    
    public FillOrKillOrder(int id,OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockId, amount, limit, firstRoundNo);
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
}
