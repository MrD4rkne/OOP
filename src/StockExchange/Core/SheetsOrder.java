package StockExchange.Core;

import StockExchange.Orders.Order;
import StockExchange.Orders.OrderType;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class SheetsOrder {
    private final int stockId;
    
    private final List<Order> orders;
    
    public SheetsOrder(int stockId, List<Order> orders) {
        this.stockId = stockId;
        this.orders = orders;
    }
    
    public int getStockId() {
        return stockId;
    }
    
    public void insertOrder(Order order) {
        orders.add(order);
    }
    
    public void processOrders(int roundNo){
        List<Order> buyOrders = getSorted(OrderType.BUY);
        List<Order> saleOrders = getSorted(OrderType.SALE);
    }
    
    private List<Order> getSorted(OrderType type){
        return orders.stream()
                .filter(order -> order.getType() == type)
                .collect(toList());
    }
 }
