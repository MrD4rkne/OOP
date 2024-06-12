package StockExchange.orders;

import StockExchange.Investors.Investor;
import StockExchange.Orders.GoodTillCancelledOrder;
import StockExchange.Orders.OrderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GoodTillCancelledorderTest {
    @Test
    void complete() {
        // Arrange
        final int ammount = 10;
        final int firstComplete = (int)Math.ceil(ammount/3.0);
        final int secondComplete = (int)Math.ceil(ammount/2.0);
        final int finalComplete = ammount - firstComplete -secondComplete;
        GoodTillCancelledOrder goodTillCancelledOrder = new GoodTillCancelledOrder(0,OrderType.SALE, 0, 0, ammount, 2, 0);

        // Act
        boolean isExpiredBefore = goodTillCancelledOrder.isExpired(0);
        goodTillCancelledOrder.complete(0,firstComplete);
        boolean isExpiredAfter = goodTillCancelledOrder.isExpired(0);
        goodTillCancelledOrder.complete(0,secondComplete);
        boolean isExpiredAfter2 = goodTillCancelledOrder.isExpired(0);
        goodTillCancelledOrder.complete(0,finalComplete);
        boolean isExpiredAfter3 = goodTillCancelledOrder.isExpired(0);

        // Assert
        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertFalse(isExpiredAfter);
        Assertions.assertFalse(isExpiredAfter2);
        Assertions.assertTrue(isExpiredAfter3);
    }
}
