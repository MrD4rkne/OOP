package StockExchange.Core;

import StockExchange.Investors.Investor;
import StockExchange.Orders.Order;

public interface ITransactionInfoProvider {
    int getCurrentRoundNo();

    int getLastTransactionPrice(int stockIndex);

    void addOrder(Investor investor, Order order);
}
