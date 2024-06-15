package stockMarket.core;

import stockMarket.companies.CompanySheet;
import stockMarket.companies.StockCompany;
import stockMarket.investors.IInvestorService;
import stockMarket.investors.IOrderGatherer;
import stockMarket.investors.OrderGatherer;
import stockMarket.orders.Order;

import java.util.Arrays;
import java.util.List;

public class TradingSystem implements ITradingSystem {
    private static final int FIRST_ROUND_NO = 0;

    private final IStockMarketLogger logger;

    private final CompanySheet[] sheetsOrders;

    private final IInvestorService investorService;

    private final IOrderGatherer orderGatherer;

    private final StockCompany[] stockCompanies;

    private final int roundsCount;

    private int roundNo;

    public TradingSystem(IStockMarketLogger logger, IInvestorService investorService, StockCompany[] stockCompanies, int[] lastRoundPrices, int roundsCount) {
        this.logger = logger;
        this.investorService = investorService;
        this.roundNo = FIRST_ROUND_NO;
        this.sheetsOrders = new CompanySheet[lastRoundPrices.length];
        this.stockCompanies = Arrays.copyOf(stockCompanies, stockCompanies.length);
        seedSheetsOrders(lastRoundPrices);
        orderGatherer = new OrderGatherer(investorService, sheetsOrders);
        this.roundsCount = roundsCount;
    }

    @Override
    public boolean hasNextRound() {
        return roundNo < roundsCount;
    }

    @Override
    public void nextRound() {
        if (!hasNextRound())
            throw new IllegalStateException("No more rounds to simulate");
        logger.startRound(roundNo);
        askInvestorsForOrders();

        for (CompanySheet sheetsOrder : sheetsOrders) {
            logger.logSheet(sheetsOrder);
        }

        processStocks();
        roundNo++;
    }

    @Override
    public String toString() {
        return investorService.toString();
    }

    private void processStocks() {
        for (CompanySheet sheetsOrder : sheetsOrders) {
            List<TransactionInfo> newTransactions = sheetsOrder.processOrders(roundNo);
            logger.logTransactionsForStock(sheetsOrder.getStockCompany(), newTransactions);
        }

        logger.endRound(roundNo, investorService);
    }

    private void askInvestorsForOrders() {
        List<Order> ordersToAdd = orderGatherer.getOrders(roundNo);
        ordersToAdd.forEach(order -> sheetsOrders[order.getStockId()].insertOrder(order));
        logger.logNewOrders(ordersToAdd);
    }

    private void seedSheetsOrders(int[] lastRoundPrices) {
        for (int i = 0; i < lastRoundPrices.length; i++) {
            sheetsOrders[i] = new CompanySheet(stockCompanies[i], lastRoundPrices[i], investorService);
        }
    }
}
