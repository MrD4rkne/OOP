package StockExchange.Core;

import StockExchange.Investors.*;
import StockExchange.Orders.Order;
import StockExchange.Sheet.SheetsOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradingSystem implements ITradingSystem {
    private static final int FIRST_ROUND_NO = 0;

    private int nextOrderId;

    private final int stocksCount;

    private final SheetsOrder[] sheetsOrders;

    private final IInvestorService investorService;
    
    private final IOrderGatherer orderGatherer;

    private int roundNo;

    public TradingSystem(int stocksCount, IInvestorService investorService) {
        this.stocksCount = stocksCount;
        this.investorService = investorService;
        this.roundNo = FIRST_ROUND_NO;
        this.sheetsOrders = new SheetsOrder[stocksCount];
        seedSheetsOrders();
        nextOrderId = 0;
        orderGatherer = new OrderGatherer(investorService, sheetsOrders);
    }

    public Investor addInvestor(Investor investor){
        return investorService.registerInvestor(investor);
    }

    public void nextRound(){
        askInvestorsForOrders();
        processStocks();
        roundNo++;
    }
    
    private void processStocks(){
        for(int i = 0; i < stocksCount; i++){
            sheetsOrders[i].processOrders(roundNo);
        }
    }

    private void askInvestorsForOrders(){
        List<Order> ordersToAdd = orderGatherer.getOrders(roundNo);
        ordersToAdd.forEach(order -> sheetsOrders[order.getStockId()].insertOrder(order));
    }

    private void seedSheetsOrders(){
        for(int i = 0; i < stocksCount; i++){
            sheetsOrders[i] = new SheetsOrder(i,investorService);
        }
    }
}
