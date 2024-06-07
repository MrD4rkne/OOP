package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class GoodTillEndOfTurnOrder extends Order{

    private final int dueRoundNo;

    public GoodTillEndOfTurnOrder(OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo, int dueRoundNo) {
        super(type, investor, stockId, amount, limit, firstRoundNo);
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
