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
        final int ammount = 10;
        final Investor investor = new Investor(0);
        FillOrKillOrder fillOrKillOrder = new FillOrKillOrder(0, OrderType.SALE, investor, 0, ammount, 1, 0);


        // Act
        boolean isExpiredBefore = fillOrKillOrder.isExpired(0);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fillOrKillOrder.complete(0,ammount-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fillOrKillOrder.complete(0,ammount-2);
        });
        fillOrKillOrder.complete(0,ammount);
        boolean isExpiredAfter = fillOrKillOrder.isExpired(0);

        // Assert
        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertTrue(isExpiredAfter);
    }
}
