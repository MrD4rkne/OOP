package stockMarket.core;

import stockMarket.companies.StockCompany;
import stockMarket.investors.IInvestorService;
import stockMarket.orders.Order;
import stockMarket.companies.CompanySheet;

import java.util.List;

/**
 * IStockLogger interface provides the methods for logging the stock market simulation data.
 */
public interface IStockMarketLogger {
    /**
     * Logs the start of the round.
     * 
     * @param roundNo the number of the round
     */
    void startRound(int roundNo);
    
    /**
     * Logs the order sheet.
     * 
     * @param sheetsOrder the order sheet
     */
    void logSheet(CompanySheet sheetsOrder);
    
    /**
     * Logs the transactions for the stock.
     * 
     * @param stockCompany the stock company
     * @param transactionInfos the list of transaction information
     */
    void logTransactionsForStock(StockCompany stockCompany, List<TransactionInfo> transactionInfos);
    
    /**
     * Logs the new orders.
     * 
     * @param orders the list of orders
     */
    void logNewOrders(List<Order> orders);
    
    /**
     * Logs the end of the round.
     * 
     * @param roundNo the number of the round
     * @param investorService the investor service
     */
    void endRound(int roundNo, IInvestorService investorService);
}
