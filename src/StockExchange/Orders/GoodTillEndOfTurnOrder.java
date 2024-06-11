package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class GoodTillEndOfTurnOrder extends Order{

    private final int dueRoundNo;

    public GoodTillEndOfTurnOrder(int id,OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo, int dueRoundNo) {
        super(id,type, investorId, stockId, amount, limit, firstRoundNo);
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
