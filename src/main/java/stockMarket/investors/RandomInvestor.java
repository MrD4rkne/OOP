package stockMarket.investors;

import stockMarket.core.StockCompany;
import stockMarket.orders.*;

import java.util.Random;
import java.util.logging.Logger;

public class RandomInvestor extends Investor{
    private final static int PRICE_MARGIN = 10;
    
    private final Random random;
    
    public RandomInvestor() {
        this.random = new Random();
    }
    
    @Override
    public void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet) {
        // Choose order type & company.
        OrderType orderType = random.nextBoolean() ? OrderType.BUY : OrderType.SALE;
        int stock = random.nextInt(wallet.getStocksCount());
        StockCompany company = transactionInfoProvider.getStock(stock);
        
        // Choose price.
        int limit = Math.max(1,random.nextInt(2*PRICE_MARGIN + 1) + transactionInfoProvider.getLastTransactionPrice(stock) - PRICE_MARGIN);
        int amount = random.nextInt(1,100);
        if(orderType == OrderType.SALE && amount > wallet.getShares(stock).getAmount()) {
           return;
        }
        if(orderType == OrderType.BUY && amount * limit > wallet.getFunds()) {
            return;
        }
        
        Order order = InvestorHelper.createRandomTypeOrder(random, orderType, getId(), transactionInfoProvider.getStock(stock), amount, limit, transactionInfoProvider.getCurrentRoundNo());
        try{
            transactionInfoProvider.addOrder(this,order);
        }catch(Exception e){
            Logger.getGlobal().severe("Error while adding order." + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return getId() + ": Random Investor";
    }

}
