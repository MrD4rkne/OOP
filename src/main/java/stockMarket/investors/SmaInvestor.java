package stockMarket.investors;

import stockMarket.orders.*;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Investor that uses Simple Moving Average to make decisions. 
 * It buys when the short-term average crosses the long-term average from below and sells when the short-term average crosses the long-term average from above.
 * Short-term: SMA5, long-term: SMA10.
 */
public class SmaInvestor extends Investor{
    private final int LIMIT_MARGIN = 10;
    
    private final SmaCalculator smaCalculator;
    
    private final Random random;

    public SmaInvestor(SmaCalculator smaCalculator, Random random){
        super();
        this.smaCalculator=smaCalculator;
        this.random = random;
    }

    @Override
    public void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet) {
        // Check for signals.
        int stockId = -1;
        SmaSignal sma = SmaSignal.NONE;
        for(int i = 0; i< wallet.getStocksCount(); i++){
            SmaSignal curr = smaCalculator.getSignal(i,transactionInfoProvider);
            if(curr== SmaSignal.NONE){
                continue;
            }

            if(canMakeOrder(wallet,transactionInfoProvider,curr,i)){
                stockId = i;
                sma = curr;
                break;
            }
        }
        
        // If no satisfiable signals, do nothing this time.
        if(sma == SmaSignal.NONE)
            return;
        
        try{
            Order order = createOrder(transactionInfoProvider,wallet,stockId,sma);
            transactionInfoProvider.addOrder(this,order);
        }catch(Exception e){
            Logger.getGlobal().severe("Error while adding order." + e.getMessage());
            throw e;
        }
    }

    @Override
    public String toString() {
        return getId() + ": SMA Investor";
    }

    private Order createOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet, int stockId, SmaSignal sma){
        if(sma == SmaSignal.NONE)
            throw new IllegalArgumentException("Signal must be BUY or SELL");

        int minLimit = Math.max(1,transactionInfoProvider.getLastTransactionPrice(stockId) - LIMIT_MARGIN);
        int maxLimit = transactionInfoProvider.getLastTransactionPrice(stockId) + LIMIT_MARGIN;
        maxLimit = sma == SmaSignal.BUY ? Math.min(maxLimit, wallet.getFunds()) : maxLimit;
        int limit = random.nextInt(minLimit, maxLimit + 1);

        int amount = sma == SmaSignal.BUY ? wallet.getFunds() / limit : random.nextInt(wallet.getShares(stockId).amount()) + 1;

        OrderType orderType = sma == SmaSignal.BUY ? OrderType.BUY : OrderType.SALE;
        return InvestorHelper.createRandomTypeOrder(random, orderType, getId(), transactionInfoProvider.getStock(stockId), amount, limit, transactionInfoProvider.getCurrentRoundNo());
    }
    
    private boolean canMakeOrder(InvestorWalletVm wallet, ITransactionInfoProvider transactionInfoProvider, SmaSignal smaSignal, int stockId){
        return switch (smaSignal) {
            case BUY -> hasEnoughMoney(wallet, transactionInfoProvider, stockId);
            case SELL -> hasAnyShares(wallet, stockId);
            default -> false;
        };
    }

    private boolean hasEnoughMoney(InvestorWalletVm wallet, ITransactionInfoProvider transactionInfoProvider, int stockId){
        return wallet.getFunds() >= Math.max(1,transactionInfoProvider.getLastTransactionPrice(stockId) - LIMIT_MARGIN);
    }
    
    private boolean hasAnyShares(InvestorWalletVm wallet, int stockId){
        return wallet.getShares(stockId).amount() > 0;
    }
}
