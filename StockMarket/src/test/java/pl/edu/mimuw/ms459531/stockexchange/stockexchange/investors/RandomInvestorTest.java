package pl.edu.mimuw.ms459531.stockexchange.stockexchange.investors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.stockexchange.companies.CompanySheet;
import pl.edu.mimuw.ms459531.stockexchange.companies.IReadonlySheet;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.investors.*;
import pl.edu.mimuw.ms459531.stockexchange.orders.Order;
import pl.edu.mimuw.ms459531.stockexchange.orders.OrderType;

import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RandomInvestorTest {
    @Test
    void doesMakeOrder() {
        // Arrange
        final int investorId = 0;

        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);

        RandomInvestor randomInvestor = new RandomInvestor(random);
        randomInvestor.setId(0);

        IInvestorService investorService = new InvestorService(new StockCompany[]{company});
        investorService.registerInvestor(randomInvestor);
        investorService.addFunds(randomInvestor.getId(), 1000);

        IReadonlySheet[] sheets = new IReadonlySheet[]{new CompanySheet(company, 100, investorService)};

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);
        when(random.nextBoolean()).thenReturn(true);
        when(random.nextInt(2 * 10 + 1)).thenReturn(0);
        when(random.nextInt(1, 11 + 1)).thenReturn(2);
        when(random.nextDouble()).thenReturn(0.1);

        // Act & assert
        Assertions.assertDoesNotThrow(() -> {
            List<Order> orders = orderGatherer.getOrders(0);
            Assertions.assertEquals(1, orders.size());
            Order order = orders.get(0);
            Assertions.assertEquals(0, order.getInvestorId());
            Assertions.assertEquals(company, order.getStockCompany());
            Assertions.assertEquals(2, order.getAmount());
            Assertions.assertEquals(90, order.getLimit());
            Assertions.assertEquals(order.getType(), OrderType.BUY);
        });
    }

    @Test
    void toLessFunds() {
        // Arrange
        final int investorId = 0;

        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);

        RandomInvestor randomInvestor = new RandomInvestor(random);
        randomInvestor.setId(0);

        IInvestorService investorService = new InvestorService(new StockCompany[]{company});
        investorService.registerInvestor(randomInvestor);
        investorService.addFunds(randomInvestor.getId(), 10);

        IReadonlySheet[] sheets = new IReadonlySheet[]{new CompanySheet(company, 100, investorService)};

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);
        when(random.nextBoolean()).thenReturn(true);
        when(random.nextInt(2 * 10 + 1)).thenReturn(0);
        when(random.nextInt(1, 11 + 1)).thenReturn(2);
        when(random.nextDouble()).thenReturn(0.1);

        // Act
        Assertions.assertDoesNotThrow(() -> {
            List<Order> orders = orderGatherer.getOrders(0);
            Assertions.assertEquals(0, orders.size());
        });
    }

    @Test
    void sell() {
        // Arrange
        final int investorId = 0;

        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);

        RandomInvestor randomInvestor = new RandomInvestor(random);
        randomInvestor.setId(0);

        IInvestorService investorService = new InvestorService(new StockCompany[]{company});
        investorService.registerInvestor(randomInvestor);
        investorService.addStock(randomInvestor.getId(), 0, 1);

        IReadonlySheet[] sheets = new IReadonlySheet[]{new CompanySheet(company, 100, investorService)};

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);
        when(random.nextBoolean()).thenReturn(false);
        when(random.nextInt(2 * 10 + 1)).thenReturn(0);
        when(random.nextInt(1, 1 + 1)).thenReturn(1);
        when(random.nextDouble()).thenReturn(0.1);

        // Act
        Assertions.assertDoesNotThrow(() -> {
            List<Order> orders = orderGatherer.getOrders(0);
            Assertions.assertEquals(1, orders.size());
            Order order = orders.get(0);
            Assertions.assertEquals(0, order.getInvestorId());
            Assertions.assertEquals(company, order.getStockCompany());
            Assertions.assertEquals(1, order.getAmount());
            Assertions.assertEquals(100 - 10, order.getLimit());
            Assertions.assertEquals(order.getType(), OrderType.SALE);
        });
    }

    @Test
    void noStock() {
        // Arrange
        final int investorId = 0;

        StockCompany company = new StockCompany(0, "ABCDE");
        Random random = mock(Random.class);

        RandomInvestor randomInvestor = new RandomInvestor(random);
        randomInvestor.setId(0);

        IInvestorService investorService = new InvestorService(new StockCompany[]{company});
        investorService.registerInvestor(randomInvestor);

        IReadonlySheet[] sheets = new IReadonlySheet[]{new CompanySheet(company, 100, investorService)};

        IOrderGatherer orderGatherer = new OrderGatherer(investorService, sheets);
        when(random.nextBoolean()).thenReturn(false);
        when(random.nextInt(2 * 10 + 1)).thenReturn(0);
        when(random.nextInt(1, 1 + 1)).thenReturn(1);
        when(random.nextDouble()).thenReturn(0.1);

        // Act
        Assertions.assertDoesNotThrow(() -> {
            List<Order> orders = orderGatherer.getOrders(0);
            Assertions.assertEquals(0, orders.size());

        });
    }
}
