package pl.edu.mimuw.ms459531.stockexchange.investors;

import java.util.Arrays;

/**
 * Calculates the Simple Moving Average (SMA) for the stock.
 */
public class SmaCalculator {
    private final int LONG_SMA_LENGTH = 10;
    private final int SHORT_SMA_LENGTH = 5;

    private final int[][] lastTenTransactionRates;
    private final SmaSignal[] signals;
    private int lastRoundNo;

    public SmaCalculator(int stocksCount) {
        lastTenTransactionRates = new int[stocksCount][LONG_SMA_LENGTH];
        signals = new SmaSignal[stocksCount];
        Arrays.fill(signals, SmaSignal.NONE);
        this.lastRoundNo = -1;
    }

    public SmaSignal getSignal(int stockId, ITransactionInfoProvider provider) {
        int roundNo = provider.getCurrentRoundNo();
        if (lastRoundNo != roundNo) {
            if (lastRoundNo + 1 != roundNo)
                throw new IllegalArgumentException("Round must be called in order");
            lastRoundNo = roundNo;
            calculateSMA(provider);
        }
        return signals[stockId];
    }

    private void calculateSMA(ITransactionInfoProvider provider) {
        int index = LONG_SMA_LENGTH;
        if (lastRoundNo <= LONG_SMA_LENGTH - 1) {
            index = lastRoundNo;
        }
        for (int stockId = 0; stockId < lastTenTransactionRates.length; stockId++) {
            int lastRate = provider.getLastTransactionPrice(stockId);
            if (index != LONG_SMA_LENGTH) {
                // less than LONG_SMA_LENGTH rounds
                lastTenTransactionRates[stockId][index] = lastRate;
                continue;
            }

            double prevDifference = calculateLongSMA(stockId) - calculateShortSMA(stockId);
            shift(lastTenTransactionRates[stockId]);
            lastTenTransactionRates[stockId][LONG_SMA_LENGTH - 1] = provider.getLastTransactionPrice(stockId);

            double currDifference = calculateLongSMA(stockId) - calculateShortSMA(stockId);
            signals[stockId] = interpret(prevDifference, currDifference);
        }
    }

    private SmaSignal interpret(double prevDifference, double currentDifference) {
        if (prevDifference >= 0 && currentDifference < 0) {
            // Short SMA gets ahead of long SMA10.
            return SmaSignal.BUY;
        }
        if (prevDifference <= 0 && currentDifference > 0) {
            return SmaSignal.SELL;
        }
        return SmaSignal.NONE;
    }

    private double calculateLongSMA(int stockId) {
        return calculate(stockId, LONG_SMA_LENGTH);
    }

    private double calculateShortSMA(int stockId) {
        return calculate(stockId, SHORT_SMA_LENGTH);
    }

    private double calculate(int stockId, int n) {
        if (n == 0)
            return 0;
        int sum = 0;
        for (int i = 0; i < n; i++) {
            sum += lastTenTransactionRates[stockId][LONG_SMA_LENGTH - 1 - i];
        }
        return ((double) sum) / n;
    }

    private void shift(int[] array) {
        for (int i = 1; i < array.length; i++) {
            array[i - 1] = array[i];
        }
    }
}
