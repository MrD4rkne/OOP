package pl.edu.mimuw.ms459531.stockexchange.stockexchange.orders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.orders.OrderType;
import pl.edu.mimuw.ms459531.stockexchange.orders.UnlimitedOrder;

public class GoodTillCancelledorderTest {
    @Test
    void complete() {
        // Arrange
        StockCompany company = new StockCompany(1, "A");
        final int ammount = 10;
        final int firstComplete = (int) Math.ceil(ammount / 3.0);
        final int secondComplete = (int) Math.ceil(ammount / 2.0);
        final int finalComplete = ammount - firstComplete - secondComplete;
        UnlimitedOrder goodTillCancelledOrder = new UnlimitedOrder(0, OrderType.SALE, 0, company, ammount, 2, 0);

        // Act
        boolean isExpiredBefore = goodTillCancelledOrder.isExpired(0);
        goodTillCancelledOrder.complete(0, firstComplete);
        boolean isExpiredAfter = goodTillCancelledOrder.isExpired(0);
        goodTillCancelledOrder.complete(0, secondComplete);
        boolean isExpiredAfter2 = goodTillCancelledOrder.isExpired(0);
        goodTillCancelledOrder.complete(0, finalComplete);
        boolean isExpiredAfter3 = goodTillCancelledOrder.isExpired(0);

        // Assert
        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertFalse(isExpiredAfter);
        Assertions.assertFalse(isExpiredAfter2);
        Assertions.assertTrue(isExpiredAfter3);
    }
}
