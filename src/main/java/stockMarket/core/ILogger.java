package stockMarket.core;

import stockMarket.investors.IInvestorService;
import stockMarket.orders.Order;
import stockMarket.stock.OrderSheet;

import java.util.List;

public interface ILogger {
    void startRound(int round);
    
    void logSheet(OrderSheet sheetsOrder);
    
    void logTransactionsForStock(StockCompany stockCompany, List<TransactionInfo> transactionInfos);
    
    void endRound(int round, IInvestorService investorService);
}
