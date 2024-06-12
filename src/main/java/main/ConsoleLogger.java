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
    
    public ConsoleLogger() {
    }
    
    @Override
    public void startRound(int round) {
        System.out.println();
        System.out.println("Round " + round);
        System.out.println();
        System.out.println(BREAK_LINE);
    }

    @Override
    public void logSheet(OrderSheet sheetsOrder) {
        logMessage(sheetsOrder.toString());
    }

    @Override
    public void logMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void logTransactionsForStock(StockCompany stockCompany, List<TransactionInfo> transactionInfos) {
        logMessage("Transactions for " + stockCompany+":");
        for (TransactionInfo transactionInfo : transactionInfos) {
            System.out.println(transactionInfo);
        }
        System.out.println();
    }

    @Override
    public void endRound(int round, IInvestorService investorService) {
        logMessage("End of round " + round);
        logMessage("Wallets:");
        for (int i = 0; i < investorService.count(); i++) {
            System.out.println(investorService.getWallet(i));
        }
        System.out.println(BREAK_LINE);
    }
}
