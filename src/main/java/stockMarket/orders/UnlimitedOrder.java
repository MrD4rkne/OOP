package stockMarket.orders;

import stockMarket.companies.StockCompany;

/*
    * Represents unlimited order.
    * It is valid until it is fully processed.
 */
public class UnlimitedOrder extends Order{
    
    public UnlimitedOrder(OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        this(0,type, investorId, stockCompany, amount, limit, firstRoundNo);
    }
    
    public UnlimitedOrder(int id, OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
        super(id,type, investorId, stockCompany, amount, limit, firstRoundNo);
    }

    @Override
    protected String shortName(){
        return "UN";
    }
}
