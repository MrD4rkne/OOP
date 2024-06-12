package stockMarket.investors;

import stockMarket.orders.*;

import java.util.Random;

public class RandomInvestor extends Investor{
    private final static int PRICE_MARGIN = 10;
    private final static int MAX_ROUND_NO = 100;
    
    private final Random random;
    
    public RandomInvestor() {
        this.random = new Random();
    }
    
    @Override
    public void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet) {
        OrderType orderType = random.nextInt(2) == 0 ? OrderType.BUY : OrderType.SALE;
        int companyId = chooseStock(wallet, transactionInfoProvider, orderType);
        if(companyId == -1) {
            // cannot make any order
            return;
        }
        
        int minPrice = transactionInfoProvider.getLastTransactionPrice(companyId) - PRICE_MARGIN;
        int maxPrice = transactionInfoProvider.getLastTransactionPrice(companyId) + PRICE_MARGIN;
        
        int amount = random.nextInt(1, Math.min(wallet.getShares(companyId).getAmount(), wallet.getFunds() / minPrice));
        int price = random.nextInt(minPrice, Math.min(maxPrice,wallet.getFunds() / amount));

        Order order = null;
        int orderMode = random.nextInt(4);
        switch (orderMode) {
            case 0:
                order = new GoodTillCancelledOrder(orderType,getId(), companyId, amount, price,
                        transactionInfoProvider.getCurrentRoundNo());
                break;
            case 1:
                order = new FillOrKillOrder(orderType, getId(), companyId, amount, price, transactionInfoProvider.getCurrentRoundNo());
                break;
            case 2:
                int lastRound = transactionInfoProvider.getCurrentRoundNo() + random.nextInt(1, MAX_ROUND_NO);
                order = new GoodTillEndOfTurnOrder(orderType, getId(), companyId, amount, price, transactionInfoProvider.getCurrentRoundNo(), lastRound);
                break;
            case 3:
                order = new ImmediateOrder(orderType, getId(), companyId, amount, price, transactionInfoProvider.getCurrentRoundNo());
                break;
        }
        
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
                if(wallet.getFunds() <=transactionInfoProvider.getLastTransactionPrice(stock)-PRICE_MARGIN) {
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
