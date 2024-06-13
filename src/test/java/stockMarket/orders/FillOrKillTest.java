package stockMarket.orders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import stockMarket.core.StockCompany;

public class FillOrKillTest {
    @Test
    void complete() {
        // Arrange
        StockCompany company = new StockCompany(1, "A");
        final int amount = 10;
        FillOrKillOrder completedOrder = new FillOrKillOrder(0, OrderType.SALE, 0, company, amount, 1, 0);
        FillOrKillOrder notCompletedOrder = new FillOrKillOrder(1, OrderType.SALE, 0, company, amount, 1, 0);


        // Act
        boolean completedIsExpiredBefore = completedOrder.isExpired(0);
        completedOrder.complete(0,amount);
        boolean completedIsExpiredAfter = completedOrder.isExpired(1);
        
        boolean notCompletedIsExpiredBefore = notCompletedOrder.isExpired(0);
        notCompletedOrder.complete(0,amount-1);
        boolean notCompletedIsExpiredAfter = notCompletedOrder.isExpired(1);

        // Assert
        Assertions.assertFalse(completedIsExpiredBefore);
        Assertions.assertTrue(completedIsExpiredAfter);
        Assertions.assertFalse(notCompletedIsExpiredBefore);
        Assertions.assertTrue(notCompletedIsExpiredAfter);
    }
}
