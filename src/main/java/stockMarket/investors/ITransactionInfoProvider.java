package stockMarket.investors;

import stockMarket.core.StockCompany;
import stockMarket.orders.Order;

public interface ITransactionInfoProvider {
    int getCurrentRoundNo();

    int getLastTransactionPrice(int stockIndex);

    void addOrder(Investor investor, Order order);

    StockCompany getStock(int companyId);
}
