package StockExchange.orders;

import StockExchange.Investors.Investor;
import StockExchange.Orders.Order;
import StockExchange.Orders.OrderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OrderTest {
    @Test
    @DisplayName("constructor - invalid amount")
    void amount(){
        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, -1, 1, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, 0, 1, 1);
        });
        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("constructor - invalid limit")
    void limit(){
        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0 , 0, 1, -1, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, 1, 0, 1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("constructor - invalid firstRoundNo")
    void firstRoundNo(){
        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, 1, 1, -1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, 1, 1, 0);
        });
    }

    @Test
    @DisplayName("constructor - invalid stockId")
    void stockId(){
        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, -1, 1, 1, 1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, 0, 0, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("complete - invalid amount")
    void complete(){
        // Arrange
        final int amount = 10;
        Order order = new ConcreteOrder(OrderType.BUY, 0, 0, amount, 2, 0);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            order.complete(0,-1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            order.complete(0,0);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            order.complete(0,amount+1);
        });

        Assertions.assertDoesNotThrow(() -> {
            order.complete(0,1);
        });

        Assertions.assertDoesNotThrow(() -> {
            order.complete(0,amount-1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            order.complete(0,amount);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            order.complete(0,1);
        });
    }

    // This class is used to test the abstract class Order
    private static class ConcreteOrder extends Order {
        public ConcreteOrder(OrderType type, int investorId, int stockId, int amount, int limit, int firstRoundNo) {
            super(0,type, 0, stockId, amount, limit, firstRoundNo);
        }
    }
}
