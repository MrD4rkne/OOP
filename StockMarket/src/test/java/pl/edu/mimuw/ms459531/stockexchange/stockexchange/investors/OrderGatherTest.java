package pl.edu.mimuw.ms459531.stockexchange.stockexchange.investors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.stockexchange.companies.IReadonlySheet;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.core.ShareVm;
import pl.edu.mimuw.ms459531.stockexchange.investors.*;
import pl.edu.mimuw.ms459531.stockexchange.orders.Order;
import pl.edu.mimuw.ms459531.stockexchange.orders.OrderType;
import pl.edu.mimuw.ms459531.stockexchange.orders.UnlimitedOrder;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderGatherTest {

    private IReadonlyInvestorService investorService;
    private IReadonlySheet sheet;
    private IOrderGatherer orderGatherer;
    private StockCompany company;
    private Investor investor;

    @BeforeEach
    public void setUp() {
        investorService = mock(IReadonlyInvestorService.class);
        sheet = mock(IReadonlySheet.class);
        company = new StockCompany(0, "ABCD");
        investor = mock(Investor.class);
        when(sheet.getLatestTransactionPrice()).thenReturn(1);
        when(sheet.getStockCompany()).thenReturn(company);
        when(sheet.getStockId()).thenReturn(company.id());
        orderGatherer = new OrderGatherer(investorService, new IReadonlySheet[]{sheet});
    }

    @Test
    public void testEmptyOrders() {
        // Arrange
        when(investorService.count()).thenReturn(0);

        // Act
        List<Order> orders = orderGatherer.getOrders(0);

        // Assert
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(0, orders.size());
    }

    @Test
    public void testValidBuy() {
        // Arrange
        setUpInvestorWithFunds(1000);

        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10, 10, 0);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act
        List<Order> orders = orderGatherer.getOrders(0);

        // Assert
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(order, orders.get(0));
    }

    @Test
    public void testValidSell() {
        // Arrange
        setUpInvestorWithFundsAndShares(1000, 100);

        Order order = new UnlimitedOrder(0, OrderType.SALE, 0, company, 10, 10, 0);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act
        List<Order> orders = orderGatherer.getOrders(0);

        // Assert
        Assertions.assertNotNull(orders);
        Assertions.assertEquals(1, orders.size());
        Assertions.assertEquals(order, orders.get(0));
    }

    @Test
    public void nullOrder() {
        // Arrange
        setUpInvestorWithFunds(1000);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.addOrder(investor, null);
        });
    }

    @Test
    public void invalidStockId() {
        // Arrange
        setUpInvestorWithFunds(1000);
        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, new StockCompany(1, "DDD"), 10, 10, 0);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.addOrder(investor, order);
        });
    }

    @Test
    public void invalidInvestorId() {
        // Arrange
        setUpInvestorWithFunds(1000);
        Order order = new UnlimitedOrder(0, OrderType.BUY, 1, company, 10, 10, 0);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void invalidRoundNo() {
        // Arrange
        setUpInvestorWithFunds(1000);
        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10, 10, 1);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void toLargeLimit() {
        // Arrange
        setUpInvestorWithFunds(1000);
        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10, 11, 1);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void toSmallLimit() {
        // Arrange
        setUpInvestorWithFunds(1000);
        when(sheet.getLatestTransactionPrice()).thenReturn(12);
        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 10, 1, 1);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void noFunds() {
        // Arrange
        setUpInvestorWithFunds(1000);
        Order order = new UnlimitedOrder(0, OrderType.BUY, 0, company, 100, 11, 1);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    @Test
    public void noStock() {
        // Arrange
        setUpInvestorWithFundsAndShares(1000, 100);
        Order order = new UnlimitedOrder(0, OrderType.SALE, 0, company, 101, 11, 1);
        doAnswer(invocation -> {
            orderGatherer.addOrder(investor, order);
            return null;
        }).when(investor).makeOrder(any(), any());

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            orderGatherer.getOrders(0);
        });
    }

    private void setUpInvestorWithFunds(int funds) {
        when(investor.getId()).thenReturn(0);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(funds);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, funds, new ShareVm[0]));
    }

    private void setUpInvestorWithFundsAndShares(int funds, int shares) {
        when(investor.getId()).thenReturn(0);
        when(investorService.count()).thenReturn(1);
        when(investorService.getInvestor(0)).thenReturn(investor);
        when(investorService.getFunds(0)).thenReturn(funds);
        when(investorService.getWallet(0)).thenReturn(new InvestorWalletVm(0, funds, new ShareVm[]{new ShareVm(company, shares)}));
        when(investorService.getStockAmount(0, company.id())).thenReturn(shares);
    }
}
