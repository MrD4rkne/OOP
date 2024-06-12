package stockMarket.stock;

import stockMarket.core.TransactionInfo;
import stockMarket.orders.Order;

import java.util.List;

public interface ISheet extends IReadonlySheet {
    void insertOrder(Order order);

    List<TransactionInfo> processOrders(int roundNo);
}
