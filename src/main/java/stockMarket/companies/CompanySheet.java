package stockMarket.companies;

import stockMarket.core.TransactionInfo;
import stockMarket.investors.IInvestorService;
import stockMarket.orders.Order;
import stockMarket.orders.OrderType;

import java.util.*;

/**
 * Holds and process orders of shares of stock company.
 */
public class CompanySheet implements ISheet {
    private final StockCompany stockCompany;
    
    private final List<Order> buyOrders;

    private final List<Order> saleOrders;

    private final IInvestorService investorService;
    
    private List<SingleStockWallet> temporaryWallets;
    
    private int lastTransactionRate;
    
    public CompanySheet(StockCompany stockCompany, int startingTransactionRate, IInvestorService investorService) {
        this.stockCompany = stockCompany;
        this.buyOrders = new ArrayList<>();
        this.saleOrders = new ArrayList<>();
        this.investorService = investorService;
        this.lastTransactionRate = startingTransactionRate;
    }
    
    public int getStockId() {
        return stockCompany.id();
    }
    
    public StockCompany getStockCompany() {
        return stockCompany;
    }
    
    public void insertOrder(Order order) {
        switch (order.getType()) {
            case BUY:
                buyOrders.add(order);
                break;
            case SALE:
                saleOrders.add(order);
                break;
            default:
                throw new IllegalArgumentException("Unknown order type");
        }
    }
    
    public List<TransactionInfo> processOrders(int roundNo){
        Collections.sort(buyOrders);
        Collections.sort(saleOrders);
        
        List<TransactionInfo> transactionsForThisRound = runProcessing(roundNo);
        buyOrders.removeIf(order->order.isExpired(roundNo+1));
        saleOrders.removeIf(order->order.isExpired(roundNo+1));
        return transactionsForThisRound;
    }

    public int getOrdersCount(){
        return buyOrders.size() + saleOrders.size();
    }

    public int getLatestTransactionPrice() {
        return lastTransactionRate;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Stock: ");
        sb.append(stockCompany.name());
        sb.append("\n");
        
        sb.append("Buy orders:\n");
        if(buyOrders.isEmpty()){
            sb.append("None \n");
        }
        for (Order order : buyOrders) {
            sb.append(order);
            sb.append("\n");
        }
        
        sb.append("\n");
        
        sb.append("Sale orders:\n");
        if(saleOrders.isEmpty()){
            sb.append("None \n");
        }
        for (Order order : saleOrders) {
            sb.append(order);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    private List<TransactionInfo> runProcessing(int roundNo){
        prepareTemporaryWallets();
        List<Order> tempBuyOrders = new ArrayList<>(buyOrders);
        List<Order> tempSaleOrders = new ArrayList<>(saleOrders);
        List<TransactionInfo> transactionsForThisRound = new ArrayList<>();
        
        int innerLoopProcessNo = 0; // Used to verify if temporaries wallets are up-to-date.

        while(!tempBuyOrders.isEmpty() || !tempSaleOrders.isEmpty()){
            Order orderToProcess = getNextOrderToProcess(tempBuyOrders, tempSaleOrders, roundNo);
            if(orderToProcess == null)
                break;

            if(orderToProcess.isExpired(roundNo))
                continue;

            List<TransactionInfo> transactions = tryProcessOrder(orderToProcess, tempBuyOrders.iterator(), tempSaleOrders.iterator(), roundNo,innerLoopProcessNo++);
            if(transactions.isEmpty()){
                if(!orderToProcess.doNeedToBeProcessedFullyAtOnce())
                    break;
                if(orderToProcess.getType() == OrderType.BUY){
                    tempBuyOrders.remove(orderToProcess);
                }
                else {
                    tempSaleOrders.remove(orderToProcess);
                }
            }
            transactionsForThisRound.addAll(transactions);
        }
        
        return transactionsForThisRound;
    }

    private List<TransactionInfo> tryProcessOrder(Order orderToProcess, Iterator<Order> buyOrders, Iterator<Order> sellOrders, int roundNo, int processId){
        List<TransactionInfo> transactions = new ArrayList<>();
        int currentDemand = orderToProcess.getAmount();
        Order currentOrder = orderToProcess;

        while(!wasProperlyProcessed(currentOrder, currentDemand)){
            if(orderToProcess.isExpired(roundNo)){
                return Collections.emptyList();
            }
            
            Order possibleOrder;
            try {
                possibleOrder = currentOrder.getType() == OrderType.SALE ? buyOrders.next() : sellOrders.next();
            }catch (NoSuchElementException ignored){
                break;
            }

            if(possibleOrder == orderToProcess)
                continue;
            if(possibleOrder.isExpired(roundNo)){
                continue;
            }
            
            InitTransactionResult result = tryInitTransaction(currentOrder, currentDemand, roundNo, processId, possibleOrder);
            if(result.isImpossible())
                break;
            if(result.isNotEnoughFunds())
                continue;

            TransactionInfo possibleTransaction = result.getTransaction();
            if(possibleTransaction.amount() < possibleOrder.getAmount()){
                currentDemand = possibleOrder.getAmount()-possibleTransaction.amount();
                currentOrder = possibleOrder;
            }else{
                currentDemand -= possibleTransaction.amount();
            }

            transactions.add(possibleTransaction);
        }

        if(!wasProperlyProcessed(currentOrder, currentDemand))
            return Collections.emptyList();

        for (TransactionInfo transaction : transactions) {
            finalizeTransaction(transaction);
        }

        return transactions;
    }

    private void finalizeTransaction(TransactionInfo transaction){
        int buyerId = transaction.buyOrder().getInvestorId();
        int sellerId = transaction.sellOrder().getInvestorId();

        transaction.buyOrder().complete(transaction.roundNo(), transaction.amount());
        transaction.sellOrder().complete(transaction.roundNo(), transaction.amount());

        investorService.addStock(buyerId, stockCompany.id(), transaction.amount());
        investorService.removeStock(sellerId, stockCompany.id(), transaction.amount());
        investorService.removeFunds(buyerId, transaction.getTotalValue());
        investorService.addFunds(sellerId, transaction.getTotalValue());

        lastTransactionRate = transaction.rate();
    }

    private InitTransactionResult tryInitTransaction(Order currentOrder, int currentDemand, int roundNo, int processId, Order possibleOrder)
    {
        if (!doOrdersMatch(currentOrder, possibleOrder)) {
            return InitTransactionResult.failed();
        }

        Order buyOrder = currentOrder.getType() == OrderType.BUY ? currentOrder : possibleOrder;
        Order sellOrder = currentOrder.getType() == OrderType.SALE ? currentOrder : possibleOrder;

        SingleStockWallet buyersWallet = updateWalletIfNecessary(buyOrder.getInvestorId(), roundNo,processId);
        SingleStockWallet sellersWallet = updateWalletIfNecessary(sellOrder.getInvestorId(), roundNo, processId);

        int stockAmountForWholeTransaction = Math.min(currentDemand,Math.min(buyOrder.getAmount(), sellOrder.getAmount()));
        int transactionRate = getTransactionRate(buyOrder, sellOrder);
        int amountPossibleForBuyer = buyersWallet.getFunds() / transactionRate;
        int amountPossibleForSeller = sellersWallet.getStockAmount();

        int possibleAmount = Math.min(currentDemand, Math.min(amountPossibleForBuyer, amountPossibleForSeller));

        if(possibleAmount < stockAmountForWholeTransaction){
            // Found matching transaction, but not enough funds or stock in wallet
            if(amountPossibleForBuyer < stockAmountForWholeTransaction){
                buyOrder.cancel();
            }
            if(amountPossibleForSeller < stockAmountForWholeTransaction){
                sellOrder.cancel();
            }
            return InitTransactionResult.notEnoughFunds();
        }

        int transactionFunds = stockAmountForWholeTransaction * transactionRate;
        buyersWallet.removeFunds(transactionFunds);
        buyersWallet.addStock(stockAmountForWholeTransaction);
        sellersWallet.removeStock(stockAmountForWholeTransaction);
        sellersWallet.addFunds(transactionFunds);
        return InitTransactionResult.successful(new TransactionInfo(buyOrder, sellOrder, stockAmountForWholeTransaction, transactionRate, roundNo));
    }

    private int getTransactionRate(Order orderA, Order orderB){
        if(!doOrdersMatch(orderA, orderB))
            throw new IllegalArgumentException("Orders do not match");
        return orderA.compareTo(orderB) < 0 ? orderA.getLimit() : orderB.getLimit();
    }

    private boolean wasProperlyProcessed(Order order, int currentDemand){
        if(order.doNeedToBeProcessedFullyAtOnce())
            return currentDemand == 0;
        return order.getAmount() > currentDemand;
    }

    private boolean doOrdersMatch(Order orderA, Order orderB){
        if(orderA.getType() == orderB.getType())
            throw new IllegalArgumentException("Order types must differ");

        Order buyOrder = orderA.getType() == OrderType.BUY ? orderA : orderB;
        Order sellOrder = orderA.getType() == OrderType.SALE ? orderA : orderB;
        return buyOrder.getLimit() >= sellOrder.getLimit();
    }

    private Order getNextOrderToProcess(List<Order> buyOrders, List<Order> sellOrders, int roundNo){
        Order buyOrder = getFirstNonExpiredOrder(buyOrders, roundNo);
        Order sellOrder = getFirstNonExpiredOrder(sellOrders, roundNo);
        
        if(buyOrder == null && sellOrder == null)
            return null;
        
        if(buyOrder == null)
            return sellOrder;
        
        if(sellOrder == null)
            return buyOrder;

        return buyOrder.compareTo(sellOrder) < 0 ? buyOrder : sellOrder;
    }
    
    private Order getFirstNonExpiredOrder(List<Order> orders, int roundNo){
        for (Order order : orders) {
            if(!order.isExpired(roundNo))
                return order;
        }
        return null;
    }

    private void prepareTemporaryWallets(){
        int investorsCount = investorService.count();
        temporaryWallets = new ArrayList<>(investorsCount);
        for(int i = 0; i < investorsCount; i++){
            temporaryWallets.add(new SingleStockWallet(i,0,0));
        }
    }

    private SingleStockWallet updateWalletIfNecessary(int investorId, int roundNo, int processId){
        SingleStockWallet wallet = temporaryWallets.get(investorId);
        if(wallet.getRoundNo() != roundNo || wallet.getProcessCounter() != processId) {
            int stockAmount = investorService.getStockAmount(investorId, stockCompany.id());
            int funds = investorService.getFunds(investorId);
            wallet.setProcessInfo(roundNo,processId, funds, stockAmount);
        }
        return wallet;
    }

    private static class InitTransactionResult{
        private final TransactionInfo transaction;

        private final boolean notEnoughFunds;

        public InitTransactionResult(TransactionInfo transaction){
            this.transaction = transaction;
            this.notEnoughFunds = false;
        }

        public InitTransactionResult(boolean notEnoughFunds){
            this.transaction = null;
            this.notEnoughFunds = notEnoughFunds;
        }

        public boolean wasSuccessful(){
            return transaction != null;
        }

        public boolean isNotEnoughFunds() {
            return notEnoughFunds;
        }

        public TransactionInfo getTransaction(){
            return transaction;
        }

        public boolean isImpossible(){
            return !wasSuccessful() && !isNotEnoughFunds();
        }

        public static InitTransactionResult notEnoughFunds(){
            return new InitTransactionResult(true);
        }

        public static InitTransactionResult successful(TransactionInfo transaction){
            return new InitTransactionResult(transaction);
        }

        public static InitTransactionResult failed(){
            return new InitTransactionResult(false);
        }
    }
}
