package StockExchange.Investors;

import StockExchange.Orders.Order;

import java.util.List;

public interface IOrderGatherer extends ITransactionInfoProvider {
   List<Order> getOrders(int roundNo);
}
