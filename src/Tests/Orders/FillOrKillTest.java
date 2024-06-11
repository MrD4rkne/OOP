package Tests.Orders;

import StockExchange.Investors.Investor;
import StockExchange.Orders.FillOrKillOrder;
import StockExchange.Orders.OrderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FillOrKillTest {
    @Test
    void complete() {
        // Arrange
        final int amount = 10;
        FillOrKillOrder fillOrKillOrder = new FillOrKillOrder(0, OrderType.SALE, 0, 0, amount, 1, 0);


        // Act
        boolean isExpiredBefore = fillOrKillOrder.isExpired(0);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fillOrKillOrder.complete(0,amount-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fillOrKillOrder.complete(0,amount-2);
        });
        fillOrKillOrder.complete(0,amount);
        boolean isExpiredAfter = fillOrKillOrder.isExpired(0);

        // Assert
        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertTrue(isExpiredAfter);
    }
}
