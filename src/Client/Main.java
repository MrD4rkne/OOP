package Client;

import StockExchange.Core.SheetsOrder;
import StockExchange.Investors.Investor;
import StockExchange.Orders.FillOrKillOrder;
import StockExchange.Orders.OrderType;

public class Main {
    public static void main(String[] args) {
        //ex2();
    }

//    private static void ex1(){
//        SheetsOrder sheetsOrder = new SheetsOrder(1);
//        Investor investor = new Investor(1);
//        sheetsOrder.insertOrder(new FillOrKillOrder(1, OrderType.SALE, investor, 1, 100, 100, 0));
//        sheetsOrder.insertOrder(new FillOrKillOrder(2, OrderType.SALE, investor, 1, 100, 100, 1));
//        sheetsOrder.insertOrder(new FillOrKillOrder(3, OrderType.BUY, investor, 1, 150, 100, 2));
//        sheetsOrder.insertOrder(new FillOrKillOrder(4, OrderType.BUY, investor, 1, 200, 100, 3));
//        sheetsOrder.insertOrder(new FillOrKillOrder(5, OrderType.BUY, investor, 1, 1000, 100, 4));
//        sheetsOrder.processOrders(0);
//    }
//
//    private static void ex2(){
//        SheetsOrder sheetsOrder = new SheetsOrder(1);
//        Investor investor = new Investor(1);
//        sheetsOrder.insertOrder(new FillOrKillOrder(1, OrderType.SALE, investor, 1, 100, 100, 0));
//        sheetsOrder.insertOrder(new FillOrKillOrder(2, OrderType.SALE, investor, 1, 100, 100, 1));
//        sheetsOrder.insertOrder(new FillOrKillOrder(3, OrderType.SALE, investor, 1, 99, 100, 2));
//        sheetsOrder.insertOrder(new FillOrKillOrder(4, OrderType.BUY, investor, 1, 99, 100, 3));
//        sheetsOrder.insertOrder(new FillOrKillOrder(5, OrderType.BUY, investor, 1, 100, 100, 4));
//        sheetsOrder.processOrders(1);
//    }
}
