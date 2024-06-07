package Tests.Orders;

import StockExchange.Investors.Investor;
import StockExchange.Orders.GoodTillEndOfTurnOrder;
import StockExchange.Orders.OrderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class GoodTillEndOfTurnOrderTest {
    @Test
    @DisplayName("is expired - without transaction")
    void expiry() {
        // Arrange
        final int amount = 10;
        final int orderRound = 10;
        Investor investor = new Investor(1000);
        GoodTillEndOfTurnOrder goodTillEndOfTurnOrder = new GoodTillEndOfTurnOrder(OrderType.SALE, investor, 0, amount, 1, 0, orderRound);

        // Act & Assert
        for(int i = 0; i <= orderRound + 10; i++){
            boolean isExpired = goodTillEndOfTurnOrder.isExpired(i);

            if(i > orderRound){
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
        Investor investor = new Investor(1000);
        GoodTillEndOfTurnOrder orderExpiryByAmmountBeforeRound = new GoodTillEndOfTurnOrder(OrderType.SALE, investor, 0, amount, 1, 0, orderRound);

        // Act
        boolean isExpiredBefore = orderExpiryByAmmountBeforeRound.isExpired(0);
        orderExpiryByAmmountBeforeRound.complete(0,amount);
        boolean isExpiredAfterRound0 = orderExpiryByAmmountBeforeRound.isExpired(0);
        boolean isExpiredAfterRound1 = orderExpiryByAmmountBeforeRound.isExpired(1);

        // Assert
        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertTrue(isExpiredAfterRound0);
        Assertions.assertTrue(isExpiredAfterRound1);
    }

    @Test
    @DisplayName("is expired - complete by round no")
    void expiryByRound(){
        // Arrange
        final int amount = 10;
        final int orderRound = 0;
        Investor investor = new Investor(1000);
        GoodTillEndOfTurnOrder orderExpiryByRound = new GoodTillEndOfTurnOrder(OrderType.SALE, investor, 0, amount, 1, 0, orderRound);

        // Act
        boolean isExpiredBefore = orderExpiryByRound.isExpired(0);
        orderExpiryByRound.complete(0,amount-1);
        boolean isExpiredAfterRound0 = orderExpiryByRound.isExpired(orderRound);
        boolean isExpiredAfterRound1 = orderExpiryByRound.isExpired(orderRound+1);

        // Assert
        Assertions.assertThrows(IllegalStateException.class, () -> {
            orderExpiryByRound.complete(orderRound+1,1);
        });

        Assertions.assertFalse(isExpiredBefore);
        Assertions.assertFalse(isExpiredAfterRound0);
        Assertions.assertTrue(isExpiredAfterRound1);
    }
}
