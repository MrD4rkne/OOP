package Tests.Orders;

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
        // Arrange
        Investor investor = new Investor(1000){};

        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, -1, 1, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, 0, 1, 1);
        });
        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("constructor - invalid limit")
    void limit(){
        // Arrange
        Investor investor = new Investor(1000){};

        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor , 0, 1, -1, 1);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, 1, 0, 1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("constructor - invalid firstRoundNo")
    void firstRoundNo(){
        // Arranges
        Investor investor = new Investor(1000){};

        // Act $ Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, 1, 1, -1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, 1, 1, 0);
        });
    }

    @Test
    @DisplayName("constructor - invalid stockId")
    void stockId(){
        //Arrange
        Investor investor = new Investor(1000){};

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, -1, 1, 1, 1);
        });

        Assertions.assertDoesNotThrow(() -> {
            Order ignored = new ConcreteOrder(OrderType.BUY, investor, 0, 1, 1, 1);
        });
    }

    @Test
    @DisplayName("complete - invalid amount")
    void complete(){
        // Arrange
        final int amount = 10;
        Investor investor = new Investor(1000){};
        Order order = new ConcreteOrder(OrderType.BUY, investor, 0, amount, 2, 0);

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
        public ConcreteOrder(OrderType type, Investor investor, int stockId, int amount, int limit, int firstRoundNo) {
            super(0,type, investor, stockId, amount, limit, firstRoundNo);
        }
    }
}
