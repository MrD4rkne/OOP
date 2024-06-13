package stockMarket.core;

import stockMarket.investors.*;
import stockMarket.orders.Order;
import stockMarket.stock.OrderSheet;

import java.util.Arrays;
import java.util.List;

public class TradingSystem implements ITradingSystem {
    private static final int FIRST_ROUND_NO = 0;
    
    private final ILogger logger;

    private final OrderSheet[] sheetsOrders;

    private final IInvestorService investorService;
    
    private final IOrderGatherer orderGatherer;
    
    private final StockCompany[] stockCompanies;

    private int roundNo;

    public TradingSystem(ILogger logger, IInvestorService investorService, StockCompany[] stockCompanies,int[] lastRoundPrices) {
        this.logger = logger;
        this.investorService = investorService;
        this.roundNo = FIRST_ROUND_NO;
        this.sheetsOrders = new OrderSheet[lastRoundPrices.length];
        this.stockCompanies = Arrays.copyOf(stockCompanies, stockCompanies.length);
        seedSheetsOrders(lastRoundPrices);
        orderGatherer = new OrderGatherer(investorService, sheetsOrders);
    }

    public void nextRound(){
        logger.startRound(roundNo);
        askInvestorsForOrders();
        
        for(OrderSheet sheetsOrder : sheetsOrders){
            logger.logSheet(sheetsOrder);
        }
        
        processStocks();
        roundNo++;
    }
    
    private void processStocks(){
        for (OrderSheet sheetsOrder : sheetsOrders) {
            List<TransactionInfo> transactionInfos = sheetsOrder.processOrders(roundNo);
            logger.logTransactionsForStock(sheetsOrder.getStockCompany(), transactionInfos);
        }
        
        logger.endRound(roundNo, investorService);
    }

    private void askInvestorsForOrders(){
        List<Order> ordersToAdd = orderGatherer.getOrders(roundNo);
        ordersToAdd.forEach(order -> sheetsOrders[order.getStockId()].insertOrder(order));
    }

    private void seedSheetsOrders(int[] lastRoundPrices){
        for(int i = 0; i < lastRoundPrices.length; i++){
            sheetsOrders[i] = new OrderSheet(stockCompanies[i],lastRoundPrices[i],investorService);
        }
    }
}
