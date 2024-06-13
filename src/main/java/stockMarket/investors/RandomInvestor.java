package stockMarket.investors;

import stockMarket.core.StockCompany;
import stockMarket.orders.*;

import java.util.Random;

public class RandomInvestor extends Investor{
    private final static int PRICE_MARGIN = 10;
    private final static int MAX_ROUND_NO = 100;
    
    private final Random random;
    
    public RandomInvestor() {
        this.random = new Random();
    }
    
    // todo: max round number ?
    @Override
    public void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet) {
        // Choose order type & company.
        OrderType orderType = random.nextInt(2) == 0 ? OrderType.BUY : OrderType.SALE;
        int companyId = chooseStock(wallet, transactionInfoProvider, orderType);
        if(companyId == -1) {
            // try other order type.
            orderType = orderType == OrderType.BUY ? OrderType.SALE : OrderType.BUY;
            companyId = chooseStock(wallet, transactionInfoProvider, orderType);
            // Both types are impossible.
            if(companyId == -1) {
                return;
            }
        }
        
        StockCompany company = transactionInfoProvider.getStock(companyId);
        
        // Calculate limit bounds.
        int minPrice = Math.max(1,transactionInfoProvider.getLastTransactionPrice(companyId) - PRICE_MARGIN);
        int maxPrice = transactionInfoProvider.getLastTransactionPrice(companyId) + PRICE_MARGIN;
        
        int maxAmount = orderType == OrderType.BUY ? wallet.getFunds() / minPrice : wallet.getShares(companyId).getAmount();
        
        int amount = random.nextInt(1, maxAmount+1);
        if(orderType == OrderType.BUY) {
            int maxPriceForAmount = wallet.getFunds() / amount;
            maxPrice = Math.min(maxPrice, maxPriceForAmount);
        }
        
        int price = random.nextInt(minPrice, maxPrice+1);

        // Create order.
        Order order = null;
        int orderMode = random.nextInt(4);
        order = switch (orderMode) {
            case 0 -> new UnlimitedOrder(orderType, getId(), company, amount, price,
                    transactionInfoProvider.getCurrentRoundNo());
            case 1 ->
                    new FillOrKillOrder(orderType, getId(), company, amount, price, transactionInfoProvider.getCurrentRoundNo());
            case 2 -> {
                int lastRound = transactionInfoProvider.getCurrentRoundNo() + random.nextInt(1, MAX_ROUND_NO);
                yield new GoodTillEndOfTurnOrder(orderType, getId(), company, amount, price, transactionInfoProvider.getCurrentRoundNo(), lastRound);
            }
            case 3 ->
                    new ImmediateOrder(orderType, getId(), company, amount, price, transactionInfoProvider.getCurrentRoundNo());
            default -> order;
        };
        
        transactionInfoProvider.addOrder(this,order);
    }
    
    private int chooseStock(InvestorWalletVm wallet, ITransactionInfoProvider transactionInfoProvider, OrderType orderType) {
        boolean[] wasChecked = new boolean[wallet.getStocksCount()];
        int toBeChecked = wallet.getStocksCount();
        
        while(toBeChecked>0) {
            int stock = random.nextInt(wallet.getStocksCount());
            if(wasChecked[stock]) {
                continue;
            }
            wasChecked[stock] = true;
            toBeChecked--;
            
            if(orderType == OrderType.BUY) {
                if(wallet.getFunds() <transactionInfoProvider.getLastTransactionPrice(stock)-PRICE_MARGIN) {
                    continue;
                }
            } else {
                if(wallet.getShares(stock).getAmount()==0) {
                    continue;
                }
            }
            
            return stock;
        }
        
        return -1;
    }
}
