package stockMarket.investors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Stubber;
import stockMarket.companies.IReadonlySheet;
import stockMarket.companies.StockCompany;
import stockMarket.core.ShareVm;
import stockMarket.orders.Order;
import stockMarket.orders.OrderType;
import stockMarket.orders.UnlimitedOrder;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderGatherTest {
    @Test
    public void testEmptyOrders() {
        // Arrange
        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(0);
        
        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);
        int roundNo = 0;
        
        // Act
        List<Order> orders = orderGatherer.getOrders(roundNo);
        
        // Assert
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(0, orders.size());
    }

    @Test
    public void testValidBuy() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);
        
        StockCompany company = new StockCompany(0, "ABCD");
        
        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());
        
        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);
        
        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10,10,0);

        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());
        
        int roundNo = 0;

        // Act
        List<Order> orders = orderGatherer.getOrders(roundNo);

        // Assert
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(order, orders.get(0));
    }

    @Test
    public void testValidSell() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[]{new ShareVm(company, 100)}));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);

        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10,10,0);

        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());

        int roundNo = 0;

        // Act
        List<Order> orders = orderGatherer.getOrders(roundNo);

        // Assert
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(order, orders.get(0));
    }

    @Test
    public void nullOrder() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);

        Order order = null;

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.addOrder(investor, order);
        });
    }

    @Test
    public void invalidStockId() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);

        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, new StockCompany(1, "DDD"), 10,10,0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.addOrder(investor, order);
        });
    }

    @Test
    public void invalidInvestorId() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);
        

        Order order = new UnlimitedOrder(0, OrderType.BUY, 1, company, 10,10,0);
        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void invalidRoundNo() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);


        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10,10,1);
        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void toLargeLimit() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);


        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10,11,1);
        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void toSmallLimit() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(12);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);


        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10,1,1);
        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void noFunds() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[0]));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);


        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 100,11,1);
        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void noStock() {
        // Arrange
        Investor investor = mock(Investor.class);
        when(investor.getId()).thenReturn(0);

        StockCompany company = new StockCompany(0, "ABCD");

        IReadonlyInvestorService investorService = mock(IReadonlyInvestorService.class);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(1000);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, 1000, new ShareVm[]{new ShareVm(company, 100)}));

        IReadonlySheet[] sheets = new IReadonlySheet[]{mock(IReadonlySheet.class)};
        when(sheets[0].getLatestTransactionPrice()).thenReturn(1);
        when(sheets[0].getStockCompany()).thenReturn(company);
        when(sheets[0].getStockId()).thenReturn(company.id());

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);


        Order order = new UnlimitedOrder(0, OrderType.SALE, 0, company, 101,11,1);
        doAnswer(invocationOnMock ->
                {
                    orderGatherer.addOrder(investor, order);
                    return null;
                }
        ).when(investor).makeOrder(any(), any());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }
}
