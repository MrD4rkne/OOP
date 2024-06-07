package StockExchange.Core;

import StockExchange.Orders.Order;
import StockExchange.Orders.OrderType;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class SheetsOrder {
    private final int stockId;
    
    private final List<Order> orders;
    
    public SheetsOrder(int stockId) {
        this.stockId = stockId;
        this.orders = new ArrayList<>();
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

        while(!buyOrders.isEmpty() || !saleOrders.isEmpty()){
            Order orderToProcess = getNextOrderToProcess(buyOrders, saleOrders);

            if(!tryProcess(orderToProcess, buyOrders.iterator(), saleOrders.iterator(), roundNo)){
                if(orderToProcess.getType() == OrderType.BUY) {
                    buyOrders.remove(orderToProcess);
                }else {
                    saleOrders.remove(orderToProcess);
                }
            }
        }
    }

    private boolean tryProcess(Order orderToProcess, Iterator<Order> buyOrders, Iterator<Order> sellOrders, int roundNo){
        if(orderToProcess.isExpired(roundNo)){
            return false;
        }

        List<Order> ordersToRemove = new ArrayList<>();
        ordersToRemove.add(orderToProcess);
        int currentDemand = orderToProcess.getAmount();
        Order currentOrder = orderToProcess;

        while(!wasProperlyProcessed(currentOrder, currentDemand)){
            try {
                Order possibleOrder = currentOrder.getType() == OrderType.SALE ? buyOrders.next() : sellOrders.next();
                if(possibleOrder == orderToProcess)
                    continue;

                if(possibleOrder.isExpired(roundNo)){
                    continue;
                }
                if (!doOrdersMatch(currentOrder, possibleOrder)) {
                    break;
                }

                if(currentDemand >= possibleOrder.getAmount()){
                    currentDemand -= possibleOrder.getAmount();
                }else {
                    currentDemand = possibleOrder.getAmount() - currentDemand;
                    currentOrder = possibleOrder;
                }
                ordersToRemove.add(possibleOrder);

            }catch (NoSuchElementException e){
                break;
            }
        }

        Order order = ordersToRemove.get(ordersToRemove.size()-1);
        if(!wasProperlyProcessed(order, currentDemand))
            return false;
        order.complete(roundNo, order.getAmount() - currentDemand);

        for(int i = ordersToRemove.size()-1-1; i >=0; i--){
            Order orderToRemove = ordersToRemove.get(i);
            orderToRemove.complete(roundNo);
        }

        // DEBUG
        for(Order orderToRemove : ordersToRemove){
            System.out.println(orderToRemove + " was removed");
        }

        return true;
    }

    private boolean wasProperlyProcessed(Order order, int currentDemand){
        if(order.doNeedToBeProcessedFullyAtOnce())
            return currentDemand == 0;
        return order.getAmount() > currentDemand;
    }

    private boolean doOrdersMatch(Order orderA, Order orderB){
        if(orderA.getType() == orderB.getType())
            throw new IllegalArgumentException("Order types must differ");

        Order buyOrder = orderA.getType() == OrderType.BUY ? orderA : orderB;
        Order sellOrder = orderA.getType() == OrderType.SALE ? orderA : orderB;
        return buyOrder.getLimit() >= sellOrder.getLimit();
    }

    private Order getNextOrderToProcess(List<Order> buyOrders, List<Order> sellOrders){
        if(buyOrders.isEmpty() && sellOrders.isEmpty()){
            throw new IllegalArgumentException("There are no orders to process");
        }

        if(buyOrders.isEmpty())
            return sellOrders.get(0);
        if(sellOrders.isEmpty())
            return buyOrders.get(0);

        Order buyOrder = buyOrders.get(0);
        Order sellOrder = sellOrders.get(0);

        return buyOrder.compareTo(sellOrder) < 0 ? buyOrder : sellOrder;
    }
    
    private List<Order> getSorted(OrderType type){
        return orders.stream()
                .filter(order -> order.getType() == type)
                .collect(toList());
    }
 }
