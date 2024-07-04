package pl.edu.mimuw.ms459531.stockexchange.stockexchange.orders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.orders.GoodTillEndOfTurnOrder;
import pl.edu.mimuw.ms459531.stockexchange.orders.OrderType;

class GoodTillEndOfTurnOrderTest {
    @Test
    @DisplayName("is expired - without transaction")
    void expiry() {
        // Arrange
        final int amount = 10;
        final int orderRound = 10;
        StockCompany company = new StockCompany(1, "A");
        GoodTillEndOfTurnOrder goodTillEndOfTurnOrder = new GoodTillEndOfTurnOrder(0, OrderType.SALE, 0, company, amount, 1, 0, orderRound);

        // Act & Assert
        for (int i = 0; i <= orderRound + 10; i++) {
            boolean isExpired = goodTillEndOfTurnOrder.isExpired(i);

            if (i > orderRound) {
                Assertions.assertTrue(isExpired);
            } else {
                Assertions.assertFalse(isExpired);
            }
        }
    }

    @Test
    @DisplayName("is expired - complete by amount")
    void expiryByAmountBeforeRound() {
        // Arrange
        final int amount = 10;
        final int orderRound = 1;
        StockCompany company = new StockCompany(1, "A");
        GoodTillEndOfTurnOrder orderExpiryByAmmountBeforeRound = new GoodTillEndOfTurnOrder(0, OrderType.SALE, 0, company, amount, 1, 0, orderRound);

        // Act
        boolean isExpiredBefore = orderExpiryByAmmountBeforeRound.isExpired(0);
        orderExpiryByAmmountBeforeRound.complete(0, amount);
        boolean isExpiredAfterRound0 = orderExpiryByAmmountBeforeRound.isExpired(0);
        boolean isExpiredAfterRound1 = orderExpiryByAmmountBeforeRound.isExpired(1);

        // Assert
        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertTrue(isExpiredAfterRound0);
        Assertions.assertTrue(isExpiredAfterRound1);
    }

    @Test
    @DisplayName("is expired - complete by round no")
    void expiryByRound() {
        // Arrange
        final int amount = 10;
        final int orderRound = 0;
        StockCompany company = new StockCompany(1, "A");
        GoodTillEndOfTurnOrder orderExpiryByRound = new GoodTillEndOfTurnOrder(0, OrderType.SALE, 0, company, amount, 1, 0, orderRound);

        // Act
        boolean isExpiredBefore = orderExpiryByRound.isExpired(0);
        orderExpiryByRound.complete(0, amount - 1);
        boolean isExpiredAfterRound0 = orderExpiryByRound.isExpired(orderRound);
        boolean isExpiredAfterRound1 = orderExpiryByRound.isExpired(orderRound + 1);

        // Assert
        Assertions.assertThrows(IllegalStateException.class, () -> orderExpiryByRound.complete(orderRound + 1, 1));

        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertFalse(isExpiredAfterRound0);
        Assertions.assertTrue(isExpiredAfterRound1);
    }
}
