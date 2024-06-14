package stockMarket.companies;

import stockMarket.core.TransactionInfo;
import stockMarket.orders.Order;

import java.util.List;

/**
 * ISheet interface provides the methods for reading and modifying the stock sheet data of company.
 */
public interface ISheet extends IReadonlySheet {
    /**
     * Inserts the order into the order sheet.
     * 
     * @param order the order
     */
    void insertOrder(Order order);

    /**
     * Processes the orders in the order sheet.
     * 
     * @param roundNo the number of the round
     * @return the list of transaction information finalized during processing the orders
     */
    List<TransactionInfo> processOrders(int roundNo);
}
