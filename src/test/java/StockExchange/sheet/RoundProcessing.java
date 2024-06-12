package StockExchange.sheet;

import StockExchange.Sheet.SheetsOrder;
import StockExchange.Core.TransactionInfo;
import StockExchange.Investors.Investor;
import StockExchange.Investors.InvestorService;
import StockExchange.Orders.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;

class RoundProcessing {
    
    /**
     * SALE:
     * 1) WA order 2, 100 stock, limit: 100
     * 2) WA order 3, 100 stock, limit:100
     * BUY:
     * 3) WA order 1, 150 stock, limit: 100
     * 4) WA order 4, 200 stock, limit: 100
     * 5) BT order 5, 1000 stock, limit: 100
     * </p>
     * Firstly 3) will be cancelled, then 1) will be completed with 4) and 2). 5) will not be completed, nor cancelled.
     * Expected transactions:
     * 1) 100 stock, 100 price, 2, 3
     * 2) 100 stock, 100 price, 2, 3
     */
    @Test
    void orders3() {
        final int investorsCount=5;
        final int stocksCount=1;
        // Arrange
        InvestorService investorService = new InvestorService(stocksCount);
        Investor[] investors = seedInvestors(investorService, investorsCount);
        SheetsOrder sheetsOrder = new SheetsOrder(0, investorService);
        Order[] orders = new Order[investorsCount];

        investorService.addFunds(0, 150*100);
        orders[0]=new FillOrKillOrder(0, OrderType.BUY, investors[0].getId(), 0,150, 100, 0);
        sheetsOrder.insertOrder(orders[0]);

        investorService.addStock(1, 0, 100);
        orders[1]=new FillOrKillOrder(1, OrderType.SALE, investors[1].getId(), 0, 100, 100, 0);
        sheetsOrder.insertOrder(orders[1]);

        investorService.addStock(2,0,100);
        orders[2]=new FillOrKillOrder(2, OrderType.SALE, investors[2].getId(), 0, 100, 100, 0);
        sheetsOrder.insertOrder(orders[2]);

        investorService.addFunds(3, 200*100);
        orders[3]=new FillOrKillOrder(3, OrderType.BUY, investors[3].getId(), 0, 200, 100, 0);
        sheetsOrder.insertOrder(orders[3]);

        investorService.addFunds(4, 1000*100);
        orders[4]=new GoodTillCancelledOrder(4, OrderType.BUY, investors[4].getId(), 0, 1000, 100, 0);
        sheetsOrder.insertOrder(orders[4]);

        final int fundsSumBefore = sumFunds(investorService, investorsCount);
        final int[] stocksSumBefore = sumStocks(investorService, stocksCount, investorsCount);

        // Act
        List<TransactionInfo> transactions = sheetsOrder.processOrders(0);
        final int fundsSumAfter = sumFunds(investorService, investorsCount);
        final int[] stocksSumAfter = sumStocks(investorService, stocksCount, investorsCount);
        final int ordersCount = sheetsOrder.getOrdersCount();
        
        // Assert
        Assertions.assertEquals(2, transactions.size());
        final List<TransactionInfo> expectedTransactions = List.of(
                new TransactionInfo(orders[3], orders[1], 100, 100, 0),
                new TransactionInfo(orders[3], orders[2], 100, 100, 0)
        );
        
        Assertions.assertEquals(expectedTransactions, transactions);
        Assertions.assertEquals(fundsSumBefore, fundsSumAfter);
        Assertions.assertArrayEquals(stocksSumBefore, stocksSumAfter);
        Assertions.assertEquals(1, ordersCount);
    }
    
    /**
     * SALE:
     * 1) WA order 1, 10 stock, limit: 100
     * BUY:
     * 2) WA order 2, 100 stock, limit: 100
     * 3) WA order 3, 100 stock, limit: 100
     * 4) WA order 4, 10 stock, limit: 100
     * </p>
     * All orders will be cancelled.
     * Expected transactions: none
     */
    @Test
    void orders1() {
        final int investorsCount=4;
        final int stocksCount=3;
        // Arrange
        InvestorService investorService = new InvestorService(stocksCount);
        Investor[] investors = seedInvestors(investorService, investorsCount);
        SheetsOrder sheetsOrder = new SheetsOrder(1, investorService);
        Order[] orders = new Order[investorsCount];

        investorService.addStock(investors[0].getId(), 1, 10);
        orders[0]=new FillOrKillOrder(0, OrderType.SALE, investors[0].getId(), 1,10, 100, 0);
        sheetsOrder.insertOrder(orders[0]);

        investorService.addFunds(investors[1].getId(), 100*100);
        orders[1]=new FillOrKillOrder(1, OrderType.BUY, investors[1].getId(), 1,100, 100, 0);
        sheetsOrder.insertOrder(orders[1]);

        investorService.addFunds(investors[2].getId(), 100*100);
        orders[2]=new FillOrKillOrder(2, OrderType.BUY, investors[2].getId(), 1,100, 100, 0);
        sheetsOrder.insertOrder(orders[2]);

        investorService.addFunds(investors[3].getId(), 10*100);
        orders[3]=new FillOrKillOrder(3, OrderType.BUY, investors[3].getId(), 1,10, 100, 0);
        sheetsOrder.insertOrder(orders[3]);

        final int fundsSumBefore = sumFunds(investorService, investorsCount);
        final int[] stocksSumBefore = sumStocks(investorService, stocksCount, investorsCount);
        
        // Act
        List<TransactionInfo> transactions = sheetsOrder.processOrders(0);
        final int fundsSumAfter = sumFunds(investorService, investorsCount);
        final int[] stocksSumAfter = sumStocks(investorService, stocksCount, investorsCount);
        int ordersCount = sheetsOrder.getOrdersCount();
        
        // Assert
        Assertions.assertEquals(0, transactions.size());
        Assertions.assertEquals(fundsSumBefore, fundsSumAfter);
        Assertions.assertArrayEquals(stocksSumBefore, stocksSumAfter);
        Assertions.assertEquals(0, ordersCount);
    }


    /**
     * SALE:
     * 1) WA order 1, 100 stock, limit: 100
     * 2) WA order 2, 100 stock, limit: 100
     * 3) WA order 3, 99 stock, limit: 100
     * BUY:
     * 4) WA order 4, 99 stock, limit: 100
     * 5) WA order 5, 100 stock, limit: 100
     * </p>
     * 1) will be cancelled, 2) will be completed with 4), 5) and 3).
     */
    @Test
    void orders2() {
        final int investorsCount=5;
        final int stocksCount=3;
        final int stockId = 2;
        final int roundNo=1;
        // Arrange
        InvestorService investorService = new InvestorService(stocksCount);
        Investor[] investors = seedInvestors(investorService, investorsCount);
        SheetsOrder sheetsOrder = new SheetsOrder(stockId, investorService);
        Order[] orders = new Order[investorsCount];
        
        investorService.addStock(investors[0].getId(), stockId, 100);
        orders[0]=new FillOrKillOrder(0, OrderType.SALE, investors[0].getId(), stockId,100, 100, roundNo);
        sheetsOrder.insertOrder(orders[0]);

        investorService.addStock(investors[1].getId(), stockId, 100);
        orders[1]=new FillOrKillOrder(1, OrderType.SALE, investors[1].getId(), stockId,100, 100, roundNo);
        sheetsOrder.insertOrder(orders[1]);

        investorService.addStock(investors[2].getId(), stockId, 99);
        orders[2]=new FillOrKillOrder(2, OrderType.SALE, investors[2].getId(), stockId,99, 100, roundNo);
        sheetsOrder.insertOrder(orders[2]);
        
        investorService.addFunds(investors[3].getId(), 99*100);
        orders[3]=new FillOrKillOrder(3, OrderType.BUY, investors[3].getId(), stockId,99, 100, roundNo);
        sheetsOrder.insertOrder(orders[3]);
        
        investorService.addFunds(investors[4].getId(), 100*100);
        orders[4]=new FillOrKillOrder(4, OrderType.BUY, investors[4].getId(), stockId,100, 100, roundNo);
        sheetsOrder.insertOrder(orders[4]);
        
        final int fundsSumBefore = sumFunds(investorService, investorsCount);
        final int[] stocksSumBefore = sumStocks(investorService, stocksCount, investorsCount);
        
        // Act
        List<TransactionInfo> transactions = sheetsOrder.processOrders(roundNo);
        final int fundsSumAfter = sumFunds(investorService, investorsCount);
        final int[] stocksSumAfter = sumStocks(investorService, stocksCount, investorsCount);
        
        // Assert
        Assertions.assertEquals(3, transactions.size());
        final List<TransactionInfo> expectedTransactions = List.of(
                new TransactionInfo(orders[3], orders[1], 99, 100, roundNo),
                new TransactionInfo(orders[4], orders[1], 1, 100, roundNo),
                new TransactionInfo(orders[4], orders[2], 99, 100, roundNo)
        );
        Assertions.assertEquals(expectedTransactions, transactions);
        Assertions.assertEquals(fundsSumBefore, fundsSumAfter);
        Assertions.assertArrayEquals(stocksSumBefore, stocksSumAfter);
    }

    /**
     * BUY:
     * 1) FOK order 0, 100 stock, limit: 1
     * 2) FOK order 1, 100 stock, limit: 1
     * SALE:
     * 3) FOK order 2, 200 stock, limit: 1
     * </p>
     * 1) will be completed with 3) and 2).
     */
    @Test
    void example1() {
        final int investorsCount=3;
        final int stocksCount=1;
        final int stockId = 0;
        final int roundNo=0;
        // Arrange
        InvestorService investorService = new InvestorService(stocksCount);
        Investor[] investors = seedInvestors(investorService, investorsCount);
        SheetsOrder sheetsOrder = new SheetsOrder(stockId, investorService);
        Order[] orders = new Order[investorsCount];
        
        investorService.addFunds(investors[0].getId(), 100*1);
        orders[0]=new FillOrKillOrder(0, OrderType.BUY, investors[0].getId(), stockId,100, 1, roundNo);
        sheetsOrder.insertOrder(orders[0]);

        investorService.addFunds(investors[1].getId(), 100*1);
        orders[1]=new FillOrKillOrder(1, OrderType.BUY, investors[1].getId(), stockId,100, 1, roundNo);
        sheetsOrder.insertOrder(orders[1]);

        investorService.addStock(investors[2].getId(), stockId, 200);
        orders[2]=new FillOrKillOrder(2, OrderType.SALE, investors[2].getId(), stockId,200, 1, roundNo);
        sheetsOrder.insertOrder(orders[2]);
        
        final int fundsSumBefore = sumFunds(investorService, investorsCount);
        final int[] stocksSumBefore = sumStocks(investorService, stocksCount, investorsCount);
        
        // Act
        List<TransactionInfo> transactions = sheetsOrder.processOrders(roundNo);
        final int fundsSumAfter = sumFunds(investorService, investorsCount);
        final int[] stocksSumAfter = sumStocks(investorService, stocksCount, investorsCount);
        int ordersCount = sheetsOrder.getOrdersCount();
        
        // Assert
        Assertions.assertEquals(2, transactions.size());
        final List<TransactionInfo> expectedTransactions = List.of(
                new TransactionInfo(orders[0], orders[2], 100, 1, roundNo),
                new TransactionInfo(orders[1], orders[2], 100, 1, roundNo)
        );
        
        Assertions.assertEquals(expectedTransactions, transactions);
        Assertions.assertEquals(fundsSumBefore, fundsSumAfter);
        Assertions.assertArrayEquals(stocksSumBefore, stocksSumAfter);
        Assertions.assertEquals(0, ordersCount);
    }

    /**
     * BUY:
     * 1) FOK order 0, 100 stock, limit: 1
     * 2) I order 1, 90 stock, limit: 1
     * SALE:
     * 3) FOK order 2, 90 stock, limit: 1
     * 4) I order 3, 10 stock, limit: 1
     * </p>
     * 1) will be completed with 3) and 4). 2) will be cancelled.
     */
    @Test
    void example2() {
        final int investorsCount=4;
        final int stocksCount=1;
        final int stockId = 0;
        final int roundNo=0;
        // Arrange
        InvestorService investorService = new InvestorService(stocksCount);
        Investor[] investors = seedInvestors(investorService, investorsCount);
        SheetsOrder sheetsOrder = new SheetsOrder(stockId, investorService);
        Order[] orders = new Order[investorsCount];

        investorService.addFunds(investors[0].getId(), 100*1);
        orders[0]=new FillOrKillOrder(0, OrderType.BUY, investors[0].getId(), stockId,100, 1, roundNo);
        sheetsOrder.insertOrder(orders[0]);

        investorService.addFunds(investors[1].getId(), 90*1);
        orders[1]=new ImmediateOrder(1, OrderType.BUY, investors[1].getId(), stockId,90, 1, roundNo);
        sheetsOrder.insertOrder(orders[1]);

        investorService.addStock(investors[2].getId(), stockId, 90);
        orders[2]=new FillOrKillOrder(2, OrderType.SALE, investors[2].getId(), stockId,90, 1, roundNo);
        sheetsOrder.insertOrder(orders[2]);
        
        investorService.addStock(investors[3].getId(), stockId, 10);
        orders[3]=new ImmediateOrder(3, OrderType.SALE, investors[3].getId(), stockId,10, 1, roundNo);
        sheetsOrder.insertOrder(orders[3]);

        final int fundsSumBefore = sumFunds(investorService, investorsCount);
        final int[] stocksSumBefore = sumStocks(investorService, stocksCount, investorsCount);

        // Act
        List<TransactionInfo> transactions = sheetsOrder.processOrders(roundNo);
        final int fundsSumAfter = sumFunds(investorService, investorsCount);
        final int[] stocksSumAfter = sumStocks(investorService, stocksCount, investorsCount);
        int ordersCount = sheetsOrder.getOrdersCount();

        // Assert
        Assertions.assertEquals(2, transactions.size());
        final List<TransactionInfo> expectedTransactions = List.of(
                new TransactionInfo(orders[0], orders[2], 90, 1, roundNo),
                new TransactionInfo(orders[0], orders[3], 10, 1, roundNo)
        );

        Assertions.assertEquals(expectedTransactions, transactions);
        Assertions.assertEquals(fundsSumBefore, fundsSumAfter);
        Assertions.assertArrayEquals(stocksSumBefore, stocksSumAfter);
        Assertions.assertEquals(0, ordersCount);
    }
    
    private int sumFunds(InvestorService investorService, int count) {
        int sum = 0;
        for (int i = 0; i < count; i++) {
            sum += investorService.getFunds(i);
        }
        return sum;
    }
    
    private int[] sumStocks(InvestorService investorService, int stocksCount, int investorsCount) {
        int[] sum = new int[stocksCount];
        
        for (int stockId = 0; stockId < stocksCount; stockId++) {
            sum[stockId]=0;
            for (int investorId = 0; investorId < investorsCount; investorId++) {
                sum[stockId] += investorService.getStockAmount(investorId, stockId);
            }
        }
        
        return sum;
    }

    private Investor[] seedInvestors(InvestorService investorService, int n) {
        Investor[] investors = new Investor[n];
        for (int i = 0; i < n; i++) {
            Investor investor = mock(Investor.class);
            investors[i] = investorService.registerInvestor(investor);
        }
        return investors;
    }
}
