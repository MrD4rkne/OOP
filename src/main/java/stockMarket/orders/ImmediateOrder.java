package stockMarket.orders;

import stockMarket.companies.StockCompany;

/**
 * Represents immediate order.
 * It is valid only if processed (even partially) in the same round it was added.
 */
public class ImmediateOrder extends GoodTillEndOfTurnOrder {

    public ImmediateOrder(OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        this(0, type, investorId, stockCompany, amount, limit, firstRoundNo);
    }

    public ImmediateOrder(int id, OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        super(id, type, investorId, stockCompany, amount, limit, firstRoundNo, firstRoundNo);
    }

    @Override
    protected String shortName() {
        return "IM";
    }
}
