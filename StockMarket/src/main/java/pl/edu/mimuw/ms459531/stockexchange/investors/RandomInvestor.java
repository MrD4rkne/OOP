package pl.edu.mimuw.ms459531.stockexchange.investors;

import pl.edu.mimuw.ms459531.stockexchange.orders.Order;
import pl.edu.mimuw.ms459531.stockexchange.orders.OrderType;

import java.util.Random;
import java.util.logging.Logger;

/**
 * Random investor that makes random orders.
 */
public class RandomInvestor extends Investor {
    private final static int LIMIT_MARGIN = 10;

    private final Random random;

    public RandomInvestor(Random random) {
        this.random = random;
    }

    @Override
    public void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet) {
        // Choose order type & company.
        OrderType orderType = random.nextBoolean() ? OrderType.BUY : OrderType.SALE;
        int stock = random.nextInt(wallet.getStocksCount());

        // Choose price.
        int limit = Math.max(1, random.nextInt(2 * LIMIT_MARGIN + 1) + transactionInfoProvider.getLastTransactionPrice(stock) - LIMIT_MARGIN);

        int maxAmount = orderType == OrderType.BUY ? wallet.getFunds() / wallet.getStocksCount() / limit : wallet.getShares(stock).amount();
        if (maxAmount == 0)
            return;
        int amount = random.nextInt(1, maxAmount + 1);
        if (orderType == OrderType.SALE && amount > wallet.getShares(stock).amount()) {
            return;
        }
        if (orderType == OrderType.BUY && amount * limit > wallet.getFunds()) {
            return;
        }

        Order order = InvestorHelper.createRandomTypeOrder(random, orderType, getId(), transactionInfoProvider.getStock(stock), amount, limit, transactionInfoProvider.getCurrentRoundNo());
        try {
            transactionInfoProvider.addOrder(this, order);
        } catch (Exception e) {
            Logger.getGlobal().severe("Error while adding order." + e.getMessage());
            throw e;
        }
    }

    @Override
    public String toString() {
        return getId() + ": Random Investor";
    }

}
