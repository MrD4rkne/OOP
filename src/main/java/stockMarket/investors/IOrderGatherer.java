package stockMarket.investors;

import stockMarket.orders.Order;

import java.util.List;

public interface IOrderGatherer extends ITransactionInfoProvider {
   List<Order> getOrders(int roundNo);
}
