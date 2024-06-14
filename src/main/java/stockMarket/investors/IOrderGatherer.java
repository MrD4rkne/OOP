package stockMarket.investors;

import stockMarket.orders.Order;

import java.util.List;

/**
 * Represents an order gatherer in the stock market.
 * An order gatherer is responsible for gathering orders from investors.
 */
public interface IOrderGatherer extends ITransactionInfoProvider {
   List<Order> getOrders(int roundNo);
}
