package pl.edu.mimuw.ms459531.stockexchange.investors;

import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.orders.Order;

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
