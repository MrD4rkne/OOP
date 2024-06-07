package Tests.Orders;

import StockExchange.Investors.Investor;
import StockExchange.Orders.ImmediateOrder;
import StockExchange.Orders.Order;
import StockExchange.Orders.OrderType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ComparingTest {
    
    @Test
    public void buyOrderByLimit() {
        // Arrange
        Investor investor = new Investor(1000);
        Order biggestLimit = new ImmediateOrder(OrderType.BUY, investor, 1, 2, 100,1);
        Order smallerLimitRoundOne = new ImmediateOrder(OrderType.BUY, investor, 1, 2, 99,1);
        Order smallerLimitRoundTwo = new ImmediateOrder(OrderType.BUY, investor, 1, 2, 99,2);
        
        // Act
        int resultsBiggestSmallerOne = biggestLimit.compareTo(smallerLimitRoundOne);
        int resultsBiggestSmallerTwo = biggestLimit.compareTo(smallerLimitRoundTwo);
        int resultsSmallerOneBiggest = smallerLimitRoundOne.compareTo(biggestLimit);
        int resultsSmallerTwoBiggest = smallerLimitRoundTwo.compareTo(biggestLimit);
        int resultsSmallerOneSmallerTwo = smallerLimitRoundOne.compareTo(smallerLimitRoundTwo);
        int resultsSmallerTwoSmallerOne = smallerLimitRoundTwo.compareTo(smallerLimitRoundOne);
        
        // Assert
        Assertions.assertEquals(-1, resultsBiggestSmallerOne);
        Assertions.assertEquals(-1, resultsBiggestSmallerTwo);
        Assertions.assertEquals(1, resultsSmallerOneBiggest);
        Assertions.assertEquals(1, resultsSmallerTwoBiggest);
        Assertions.assertEquals(-1, resultsSmallerOneSmallerTwo);
        Assertions.assertEquals(1, resultsSmallerTwoSmallerOne);
    }
    
    @Test
    public void buyOrderByRound(){
        // Arrange
        Investor investor = new Investor(1000);
        Order roundOne = new ImmediateOrder(OrderType.BUY, investor, 1, 2, 100,1);
        Order roundTwo = new ImmediateOrder(OrderType.BUY, investor, 1, 2, 100,2);
        Order roundTwoDouble = new ImmediateOrder(OrderType.BUY, investor, 1, 2, 100,2);
        
        // Act
        int resultsRoundOneRoundTwo = roundOne.compareTo(roundTwo);
        int resultsRoundTwoRoundOne = roundTwo.compareTo(roundOne);
        int resultsRoundTwoRoundTwo = roundTwo.compareTo(roundTwoDouble);
        
        // Assert
        Assertions.assertEquals(-1, resultsRoundOneRoundTwo);
        Assertions.assertEquals(1, resultsRoundTwoRoundOne);
        Assertions.assertEquals(0, resultsRoundTwoRoundTwo);
    }

    @Test
    public void sellOrderByLimit() {
        // Arrange
        Investor investor = new Investor(1000);
        Order biggestLimit = new ImmediateOrder(OrderType.SALE, investor, 1, 2, 100,1);
        Order smallerLimitRoundOne = new ImmediateOrder(OrderType.SALE, investor, 1, 2, 99,1);
        Order smallerLimitRoundTwo = new ImmediateOrder(OrderType.SALE, investor, 1, 2, 99,2);

        // Act
        int resultsBiggestSmallerOne = biggestLimit.compareTo(smallerLimitRoundOne);
        int resultsBiggestSmallerTwo = biggestLimit.compareTo(smallerLimitRoundTwo);
        int resultsSmallerOneBiggest = smallerLimitRoundOne.compareTo(biggestLimit);
        int resultsSmallerTwoBiggest = smallerLimitRoundTwo.compareTo(biggestLimit);
        int resultsSmallerOneSmallerTwo = smallerLimitRoundOne.compareTo(smallerLimitRoundTwo);
        int resultsSmallerTwoSmallerOne = smallerLimitRoundTwo.compareTo(smallerLimitRoundOne);

        // Assert
        Assertions.assertEquals(1, resultsBiggestSmallerOne);
        Assertions.assertEquals(1, resultsBiggestSmallerTwo);
        Assertions.assertEquals(-1, resultsSmallerOneBiggest);
        Assertions.assertEquals(-1, resultsSmallerTwoBiggest);
        Assertions.assertEquals(-1, resultsSmallerOneSmallerTwo);
        Assertions.assertEquals(1, resultsSmallerTwoSmallerOne);
    }
    
    @Test
    public void sellOrderByRound(){
        // Arrange
        Investor investor = new Investor(1000);
        Order roundOne = new ImmediateOrder(OrderType.SALE, investor, 1, 2, 100,1);
        Order roundTwo = new ImmediateOrder(OrderType.SALE, investor, 1, 2, 100,2);
        Order roundTwoDouble = new ImmediateOrder(OrderType.SALE, investor, 1, 2, 100,2);
        
        // Act
        int resultsRoundOneRoundTwo = roundOne.compareTo(roundTwo);
        int resultsRoundTwoRoundOne = roundTwo.compareTo(roundOne);
        int resultsRoundTwoRoundTwo = roundTwo.compareTo(roundTwoDouble);
        
        // Assert
        Assertions.assertEquals(-1, resultsRoundOneRoundTwo);
        Assertions.assertEquals(1, resultsRoundTwoRoundOne);
        Assertions.assertEquals(0, resultsRoundTwoRoundTwo);
    }
    
}
