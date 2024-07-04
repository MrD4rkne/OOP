package pl.edu.mimuw.ms459531.stockexchange.stockexchange.orders;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.orders.Order;
import pl.edu.mimuw.ms459531.stockexchange.orders.OrderType;

public class OrderTest {
    @Test
    @DisplayName("constructor - invalid amount")
    void amount() {
        StockCompany company = new StockCompany(1, "A");
        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, -1, 1, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, 0, 1, 1);
        });
        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("constructor - invalid limit")
    void limit() {
        StockCompany company = new StockCompany(1, "A");
        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, 1, -1, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, 1, 0, 1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("constructor - invalid firstRoundNo")
    void firstRoundNo() {
        // Arrange
        StockCompany company = new StockCompany(1, "A");
        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, 1, 1, -1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, company, 1, 1, 0);
        });
    }

    @Test
    @DisplayName("complete - invalid amount")
    void complete() {
        // Arrange
        final int amount = 10;
        StockCompany company = new StockCompany(1, "A");
        Order order = new ConcreteOrder(OrderType.BUY, 0, company, amount, 2, 0);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> order.complete(0, -1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> order.complete(0, 0));

        Assertions.assertThrows(IllegalArgumentException.class, () -> order.complete(0, amount + 1));

        Assertions.assertDoesNotThrow(() -> order.complete(0, 1));

        Assertions.assertDoesNotThrow(() -> order.complete(0, amount - 1));

        Assertions.assertThrows(IllegalArgumentException.class, () -> order.complete(0, amount));

        Assertions.assertThrows(IllegalArgumentException.class, () -> order.complete(0, 1));
    }

    // This class is used to test the abstract class Order
    private static class ConcreteOrder extends Order {
        public ConcreteOrder(OrderType type, int investorId, StockCompany stockCompany, int amount, int limit, int firstRoundNo) {
            super(0, type, 0, stockCompany, amount, limit, firstRoundNo);
        }

        @Override
        public String shortName() {
            return "CONCRETE";
        }
    }
}
