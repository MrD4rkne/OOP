package stockMarket.orders;

public class GoodTillEndOfTurnOrder extends Order{

    private final int dueRoundNo;
    
    public GoodTillEndOfTurnOrder(OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo, int dueRoundNo) {
        this(0,type, investorId, stockId,amount,limit, firstRoundNo,  dueRoundNo);
    }

    public GoodTillEndOfTurnOrder(int id,OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo, int dueRoundNo) {
        super(id,type, investorId, stockId, amount, limit, firstRoundNo);
        if(dueRoundNo < 0) {
            throw new IllegalArgumentException("Due round number cannot be negative");
        }
        if(dueRoundNo < firstRoundNo) {
            throw new IllegalArgumentException("Due round number cannot be less than first round number");
        }
        this.dueRoundNo = dueRoundNo;
    }

    @Override
    public boolean isExpired(int roundNo) {
        if(roundNo > dueRoundNo){
            return true;
        }
        return super.isExpired(roundNo);
    }
}
