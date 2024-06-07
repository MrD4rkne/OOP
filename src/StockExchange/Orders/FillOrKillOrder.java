package StockExchange.Orders;

import StockExchange.Investors.Investor;

public class FillOrKillOrder extends ImmediateOrder {
    public FillOrKillOrder(int id,OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
        super(id,type, investor, stockId, amount, limit, firstRoundNo);
    }

    @Override
    public boolean doNeedToBeProcessedFullyAtOnce() {
        return true;
    }

    @Override
    public void complete(int roundNo, int amount) {
        if (amount < this.getAmount()) {
            throw new IllegalArgumentException("Amount cannot be less than order amount");
        }
        super.complete(roundNo,amount);
    }
}
