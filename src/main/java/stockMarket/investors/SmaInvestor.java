package stockMarket.investors;

import stockMarket.core.StockCompany;
import stockMarket.orders.*;

import java.util.Random;

public class SmaInvestor extends Investor{
    private final int RATE_INTERVAL = 10;
    private final SmaCalculator smaCalculator;
    private final Random random;

    public SmaInvestor(SmaCalculator smaCalculator){
        super();
        this.smaCalculator=smaCalculator;
        this.random = new Random();
    }

    @Override
    public void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet) {
        int stockId = -1;
        int limit=0, amount=0;
//        SMA sma = SMA.NONE;
//        for(int i = 0; i< wallet.getStocksCount(); i++){
//            SMA curr = smaCalculator.getSignal(i,transactionInfoProvider);
//            if(curr==SMA.NONE){
//                continue;
//            }
//
//            if(curr == SMA.BUY && hasEnoughMoney(wallet,transactionInfoProvider,i)){
//                stockId=i;
//                sma=curr;
//                limit = transactionInfoProvider.getLastTransactionPrice(i)-RATE_INTERVAL;
//                amount = wallet.getFunds() / Math.max(1,limit);
//                break;
//            }
//
//            if(curr == SMA.SELL && wallet.getShares(i).getAmount() > 0){
//                stockId=i;
//                sma=curr;
//                limit = transactionInfoProvider.getLastTransactionPrice(i)+RATE_INTERVAL;
//                amount = wallet.getShares(i).getAmount();
//                break;
//            }
//        }
//
//        if(sma == SMA.NONE)
//            return;
//
//        OrderType orderType = sma == SMA.BUY ? OrderType.BUY : OrderType.SALE;
//        Order order = null;
//        int orderMode = random.nextInt(4);
//        StockCompany company = transactionInfoProvider.getStock(stockId);
//        order = switch (orderMode) {
//            case 0 -> new UnlimitedOrder(orderType, getId(), company, amount, limit,
//                    transactionInfoProvider.getCurrentRoundNo());
//            case 1 ->
//                    new FillOrKillOrder(orderType, getId(), company, amount, limit, transactionInfoProvider.getCurrentRoundNo());
//            case 2 -> {
//                int lastRound = transactionInfoProvider.getCurrentRoundNo() + random.nextInt(1, limit);
//                yield new GoodTillEndOfTurnOrder(orderType, getId(), company, amount, limit, transactionInfoProvider.getCurrentRoundNo(), lastRound);
//            }
//            case 3 ->
//                    new ImmediateOrder(orderType, getId(), company, amount, limit, transactionInfoProvider.getCurrentRoundNo());
//            default -> order;
//        };
//
//        transactionInfoProvider.addOrder(this,order);
    }

    private boolean hasEnoughMoney(InvestorWalletVm wallet, ITransactionInfoProvider transactionInfoProvider, int stockId){
        return wallet.getFunds() >= transactionInfoProvider.getLastTransactionPrice(stockId) - RATE_INTERVAL;
    }
}
