package main;

import stockMarket.core.ILogger;
import stockMarket.core.StockCompany;
import stockMarket.core.TransactionInfo;
import stockMarket.investors.IInvestorService;
import stockMarket.orders.Order;
import stockMarket.stock.OrderSheet;

import java.util.List;

public class ConsoleLogger implements ILogger {
    private final static String BREAK_LINE = "#".repeat(40);
    
    private final boolean isDebugMode;
    
    public ConsoleLogger(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }
    
    @Override
    public void startRound(int round) {
        if(!isDebugMode)
            return;
        System.out.println();
        System.out.println("Round " + round);
        System.out.println();
        System.out.println(BREAK_LINE);
    }

    @Override
    public void logSheet(OrderSheet sheetsOrder) {
        if(!isDebugMode)
            return;
        System.out.println(sheetsOrder.toString());
    }

    @Override
    public void logTransactionsForStock(StockCompany stockCompany, List<TransactionInfo> transactionInfos) {
        if(!isDebugMode)
            return;
        System.out.println("Transactions for " + stockCompany+":");
        for (TransactionInfo transactionInfo : transactionInfos) {
            System.out.println(transactionInfo);
        }
        System.out.println();
    }

    @Override
    public void endRound(int round, IInvestorService investorService) {
        if(!isDebugMode)
            return;
        System.out.println("End of round " + round);
        System.out.println("Wallets:");
        for (int i = 0; i < investorService.count(); i++) {
            System.out.println(investorService.getWallet(i));
        }
        System.out.println(BREAK_LINE);
    }
}
