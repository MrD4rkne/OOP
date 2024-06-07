package Tests.Orders;

import StockExchange.Orders.FillOrKillOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FillOrKillTest {
    @Test
    void complete() {
        // Arrange
        final int ammount = 10;
        FillOrKillOrder fillOrKillOrder = new FillOrKillOrder(null, null, 0, ammount, 1, 0);


        // Act
        boolean isExpiredBefore = fillOrKillOrder.isExpired(0);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fillOrKillOrder.complete(ammount-1);
        });
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fillOrKillOrder.complete(ammount-2);
        });
        fillOrKillOrder.complete(ammount);
        boolean isExpiredAfter = fillOrKillOrder.isExpired(0);

        // Assert
        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertTrue(isExpiredAfter);
    }
}
