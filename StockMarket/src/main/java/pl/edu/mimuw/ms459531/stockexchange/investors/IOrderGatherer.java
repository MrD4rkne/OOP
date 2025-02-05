package pl.edu.mimuw.ms459531.stockexchange.investors;

import pl.edu.mimuw.ms459531.stockexchange.orders.Order;

import java.util.List;

/**
 * Represents an order gatherer in the stock market.
 * An order gatherer is responsible for gathering orders from investors.
 */
public interface IOrderGatherer extends ITransactionInfoProvider {
    List<Order> getOrders(int roundNo);
}
