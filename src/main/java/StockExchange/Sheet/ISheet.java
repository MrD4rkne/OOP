package StockExchange.Sheet;

import StockExchange.Core.TransactionInfo;
import StockExchange.Orders.Order;

import java.util.List;

public interface ISheet extends IReadonlySheet {
    void insertOrder(Order order);

    List<TransactionInfo> processOrders(int roundNo);
}
