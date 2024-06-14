package stockMarket.investors;

import stockMarket.companies.StockCompany;
import stockMarket.orders.*;

import java.util.Random;

/**
 * Helper class for creating orders for investors.
 */
public class InvestorHelper {
    private static final int MAX_ROUND_NO = 10;
    
    /**
     * Creates a random order for an investor.
     * @param random Random object to generate random values.
     * @param investorId id of the investor.
     * @param company Company for which the order is created.
     * @param roundNo Current round number.
     * @return Order with random type.
     */
    public static Order createRandomTypeOrder(Random random, OrderType orderType, int investorId, StockCompany company, int amount, int limit, int roundNo){
        int  orderMode = getRandomOrderMode(random);
        return switch (orderMode) {
            case 0 -> new UnlimitedOrder(orderType, investorId, company, amount, limit, roundNo);
            case 1 -> new FillOrKillOrder(orderType, investorId, company, amount, limit, roundNo);
            case 2 -> {
                int lastRound = roundNo + random.nextInt(1, MAX_ROUND_NO);
                yield new GoodTillEndOfTurnOrder(orderType, investorId, company, amount, limit, roundNo, lastRound);
            }
            case 3 -> new ImmediateOrder(orderType, investorId, company, amount, limit, roundNo);
            default -> throw new IllegalArgumentException("Unexpected value: " + orderMode);
        };
    }
    
    private static int getRandomOrderMode(Random random){
        // Define the probabilities for each value
        int[] values = {0, 1, 2, 3}; // Possible values
        double[] probabilities = {0.1,0.3,0.3,0.3};

        // Calculate cumulative probabilities
        double[] cumulativeProbabilities = new double[probabilities.length];
        cumulativeProbabilities[0] = probabilities[0];
        for (int i = 1; i < probabilities.length; i++) {
            cumulativeProbabilities[i] = cumulativeProbabilities[i - 1] + probabilities[i];
        }

        // Generate a random number
        int selectedValue = 0;
        double randomValue = random.nextDouble();
        for (int i = 0; i < cumulativeProbabilities.length; i++) {
            if (randomValue <= cumulativeProbabilities[i]) {
                selectedValue = values[i];
                break;
            }
        }
        return selectedValue;
    }
}
