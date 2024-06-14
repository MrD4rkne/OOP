package stockMarket.investors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SmaCalculatorTest {

    @Test
    public void testInitialSignalIsNone() {
        ITransactionInfoProvider providerMock = mock(ITransactionInfoProvider.class);
        int stocksCount = 1;
        SmaCalculator calculator = new SmaCalculator(stocksCount);
        
        for(int i = 0; i<20; i++) {
            when(providerMock.getCurrentRoundNo()).thenReturn(0);
            when(providerMock.getLastTransactionPrice(0)).thenReturn(100);

            SmaSignal signal = calculator.getSignal(0, providerMock);
            assertEquals(SmaSignal.NONE, signal);
        }
    }

    /**
     * 9	9	9	9	9	9	7	8	9	10	11	12
     * 								SMA5	8,6	9	10
     * 								SMA10	8,8	9	9,3
     */
    @Test
    public void testBuySignal() {
        // Arrange
        ITransactionInfoProvider providerMock = mock(ITransactionInfoProvider.class);
        int stocksCount = 1;
        SmaCalculator calculator = new SmaCalculator(stocksCount);

        int[] rates = {9,9,9,9,9,9,7,8,9,10, 11};

        // Simulate rounds to fill SMA arrays
        for (int i = 0; i < rates.length; i++) {
            when(providerMock.getCurrentRoundNo()).thenReturn(i);
            when(providerMock.getLastTransactionPrice(0)).thenReturn(rates[i]);
            calculator.getSignal(0, providerMock);
        }

        // Simulate a scenario where short SMA crosses long SMA
        when(providerMock.getCurrentRoundNo()).thenReturn(rates.length);
        when(providerMock.getLastTransactionPrice(0)).thenReturn(12);

        // Act
        SmaSignal signal = calculator.getSignal(0, providerMock);
        
        // Assert
        assertEquals(SmaSignal.BUY, signal);
    }

    /**
     * 1	2	3	4	5	6	7	8	2	1	1
     * 								SMA5	4,8	3,8
     * 								SMA10	3,9	3,9
     */
    @Test
    public void testSellSignal() {
        // Arrange
        ITransactionInfoProvider providerMock = mock(ITransactionInfoProvider.class);
        int stocksCount = 1;
        SmaCalculator calculator = new SmaCalculator(stocksCount);
        
        int[] rates = {1,2,3,4,5,6,7,8,2,1};

        // Simulate rounds to fill SMA arrays
        for (int i = 0; i < rates.length; i++) {
            when(providerMock.getCurrentRoundNo()).thenReturn(i);
            when(providerMock.getLastTransactionPrice(0)).thenReturn(rates[i]);
            calculator.getSignal(0, providerMock);
        }

        // Simulate a scenario where short SMA crosses long SMA
        when(providerMock.getCurrentRoundNo()).thenReturn(rates.length);
        when(providerMock.getLastTransactionPrice(0)).thenReturn(1);

        // Act
        SmaSignal signal = calculator.getSignal(0, providerMock);
        
        // Assert
        assertEquals(SmaSignal.SELL, signal);
    }

    /**
     * 1	2	3	4	5	6	7	8	2	1	1	1
     * 								SMA5	4,8	3,8	2,6
     * 								SMA10	3,9	3,9	3,8
     */
    @Test
    public void testNoSignalChange() {
        ITransactionInfoProvider providerMock = mock(ITransactionInfoProvider.class);
        int stocksCount = 1;
        SmaCalculator calculator = new SmaCalculator(stocksCount);

        int[] rates = {1,2,3,4,5,6,7,8,2,1,1};
        
        // Simulate rounds to fill SMA arrays
        for (int i = 0; i < rates.length; i++) {
            when(providerMock.getCurrentRoundNo()).thenReturn(i);
            when(providerMock.getLastTransactionPrice(0)).thenReturn(rates[i]);
            calculator.getSignal(0, providerMock);
        }

        // Simulate a scenario where there's no significant change in SMA
        when(providerMock.getCurrentRoundNo()).thenReturn(rates.length);
        when(providerMock.getLastTransactionPrice(0)).thenReturn(1);

        SmaSignal signal = calculator.getSignal(0, providerMock);
        assertEquals(SmaSignal.NONE, signal);
    }
    
    @Test
    public void testRoundMustBeCalledInOrder() {
        ITransactionInfoProvider providerMock = mock(ITransactionInfoProvider.class);
        int stocksCount = 1;
        SmaCalculator calculator = new SmaCalculator(stocksCount);

        when(providerMock.getCurrentRoundNo()).thenReturn(0);
        when(providerMock.getLastTransactionPrice(0)).thenReturn(100);
        calculator.getSignal(0, providerMock);

        when(providerMock.getCurrentRoundNo()).thenReturn(2);
        when(providerMock.getLastTransactionPrice(0)).thenReturn(100);

        Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.getSignal(0, providerMock));

        when(providerMock.getCurrentRoundNo()).thenReturn(1);
        when(providerMock.getLastTransactionPrice(0)).thenReturn(100);
        Assertions.assertDoesNotThrow(() -> {
            calculator.getSignal(0, providerMock);
        });
        
        when(providerMock.getCurrentRoundNo()).thenReturn(0);
        when(providerMock.getLastTransactionPrice(0)).thenReturn(100);
        Assertions.assertThrows(IllegalArgumentException.class, () -> calculator.getSignal(0, providerMock));
    }
}
