package stockMarket.investors;

import stockMarket.companies.StockCompany;
import stockMarket.orders.Order;

/**
 * Provides information about transactions in stock market.
 * This interface is used by investors to get information about transactions and to make orders.
 */
public interface ITransactionInfoProvider {
    int getCurrentRoundNo();

    int getLastTransactionPrice(int stockIndex);

    void addOrder(Investor investor, Order order);

    StockCompany getStock(int companyId);
}
