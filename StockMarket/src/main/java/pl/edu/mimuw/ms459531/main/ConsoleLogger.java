package pl.edu.mimuw.ms459531.main;

import pl.edu.mimuw.ms459531.stockexchange.companies.CompanySheet;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.core.IStockMarketLogger;
import pl.edu.mimuw.ms459531.stockexchange.core.TransactionInfo;
import pl.edu.mimuw.ms459531.stockexchange.investors.IInvestorService;
import pl.edu.mimuw.ms459531.stockexchange.orders.Order;

import java.util.List;

/**
 * ConsoleLogger class implements IStockLogger interface and logs the information to the console.
 * It logs the start of the round, order sheets, transactions, new orders and end of the round.
 *
 * @see IStockMarketLogger
 */
public class ConsoleLogger implements IStockMarketLogger {
    private final static String BREAK_LINE = "#".repeat(40);

    private final boolean isDebugMode;

    public ConsoleLogger(boolean isDebugMode) {
        this.isDebugMode = isDebugMode;
    }

    @Override
    public void startRound(int roundNo) {
        if (!isDebugMode)
            return;
        System.out.println();
        System.out.println("Round " + roundNo);
        System.out.println();
        System.out.println(BREAK_LINE);
    }

    @Override
    public void logSheet(CompanySheet sheetsOrder) {
        if (!isDebugMode)
            return;
        System.out.println(sheetsOrder.toString());
    }

    @Override
    public void logTransactionsForStock(StockCompany stockCompany, List<TransactionInfo> transactionInfos) {
        if (!isDebugMode)
            return;
        System.out.println("Transactions for " + stockCompany + ":");
        for (TransactionInfo transactionInfo : transactionInfos) {
            System.out.println(transactionInfo);
        }
        System.out.println();
    }

    @Override
    public void logNewOrders(List<Order> orders) {
        if (!isDebugMode)
            return;
        System.out.println("Orders:");
        for (Order order : orders) {
            System.out.println(order);
        }
        System.out.println();
    }

    @Override
    public void endRound(int roundNo, IInvestorService investorService) {
        if (!isDebugMode)
            return;
        System.out.println("End of round " + roundNo);
        System.out.println("Wallets:");
        System.out.println(investorService);
        System.out.println(BREAK_LINE);
    }
}
