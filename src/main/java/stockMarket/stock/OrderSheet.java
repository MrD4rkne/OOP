package stockMarket.stock;

import stockMarket.core.StockCompany;
import stockMarket.core.TransactionInfo;
import stockMarket.investors.IInvestorService;
import stockMarket.orders.Order;
import stockMarket.orders.OrderType;

import java.util.*;

public class OrderSheet implements ISheet {
    private final StockCompany stockCompany;
    
    private final List<Order> buyOrders;

    private final List<Order> saleOrders;

    private final IInvestorService investorService;
    

    private List<SingleStockWallet> temporaryWallets;
    
    private int lastTransactionRate;
    
    public OrderSheet(StockCompany stockCompany, int startingTransactionRate, IInvestorService investorService) {
        this.stockCompany = stockCompany;
        this.buyOrders = new ArrayList<>();
        this.saleOrders = new ArrayList<>();
        this.investorService = investorService;
        this.lastTransactionRate = startingTransactionRate;
    }
    
    public int getStockId() {
        return stockCompany.getId();
    }
    
    public StockCompany getStockCompany() {
        return stockCompany;
    }
    
    public void insertOrder(Order order) {
        switch (order.getType()) {
            case BUY:
                binInsert(buyOrders, order);
                break;
            case SALE:
                binInsert(saleOrders, order);
                break;
            default:
                throw new IllegalArgumentException("Unknown order type");
        }
    }
    
    public List<TransactionInfo> processOrders(int roundNo){
        prepareTemporaryWallets();

        int i = 0;
        
        List<TransactionInfo> transactionsForThisRound = new ArrayList<>();
        
        List<Order> tempBuyOrders = new ArrayList<>(buyOrders);
        List<Order> tempSaleOrders = new ArrayList<>(saleOrders);

        while(!tempBuyOrders.isEmpty() || !tempSaleOrders.isEmpty()){
            Order orderToProcess = getNextOrderToProcess(tempBuyOrders, tempSaleOrders, roundNo);
            if(orderToProcess == null)
                break;
            
            if(orderToProcess.isExpired(roundNo))
                continue;
            
            
            
            List<TransactionInfo> transactions = tryProcess(orderToProcess, tempBuyOrders.iterator(), tempSaleOrders.iterator(), roundNo,i++);
            if(transactions.isEmpty()){
                if(orderToProcess.getType() == OrderType.BUY){
                    tempBuyOrders.remove(orderToProcess);
                }
                else {
                    tempSaleOrders.remove(orderToProcess);
                }
            }
            transactionsForThisRound.addAll(transactions);
        }

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
        sb.append(stockCompany.getName());
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

    private List<TransactionInfo> tryProcess(Order orderToProcess, Iterator<Order> buyOrders, Iterator<Order> sellOrders, int roundNo, int processId){
        List<TransactionInfo> transactions = new ArrayList<>();
        int currentDemand = orderToProcess.getAmount();
        Order currentOrder = orderToProcess;

        while(!wasProperlyProcessed(currentOrder, currentDemand)){
            try {
                if(orderToProcess.isExpired(roundNo)){
                    return Collections.emptyList();
                }
                
                Order possibleOrder = currentOrder.getType() == OrderType.SALE ? buyOrders.next() : sellOrders.next();
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

            }catch (NoSuchElementException ignored){
                break;
            }
        }

        if(!wasProperlyProcessed(currentOrder, currentDemand))
            return Collections.emptyList();

        for (TransactionInfo transaction : transactions) {
            finalizeTransaction(transaction);
        }

        return transactions;
    }

    private void finalizeTransaction(TransactionInfo transaction){
        try {
            int buyerId = transaction.buyOrder().getInvestorId();
            int sellerId = transaction.sellOrder().getInvestorId();

            transaction.buyOrder().complete(transaction.roundNo(), transaction.amount());
            transaction.sellOrder().complete(transaction.roundNo(), transaction.amount());

            investorService.addStock(buyerId, stockCompany.getId(), transaction.amount());
            investorService.removeStock(sellerId, stockCompany.getId(), transaction.amount());

            investorService.removeFunds(buyerId, transaction.getTotalValue());
            investorService.addFunds(sellerId, transaction.getTotalValue());

            lastTransactionRate = transaction.rate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private InitTransactionResult tryInitTransaction(Order currentOrder, int currentDemand, int roundNo, int processId, Order possibleOrder)
    {
        if (!doOrdersMatch(currentOrder, possibleOrder)) {
            return InitTransactionResult.failed();
        }

        Order buyOrder = currentOrder.getType() == OrderType.BUY ? currentOrder : possibleOrder;
        Order sellOrder = currentOrder.getType() == OrderType.SALE ? currentOrder : possibleOrder;

        SingleStockWallet buyersWallet = updateWalletIfNecessary(buyOrder.getInvestorId(), processId);
        SingleStockWallet sellersWallet = updateWalletIfNecessary(sellOrder.getInvestorId(), processId);

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

    private SingleStockWallet updateWalletIfNecessary(int investorId, int processId){
        SingleStockWallet wallet = temporaryWallets.get(investorId);
        if(wallet.getProcessCounter() != processId) {
            int stockAmount = investorService.getStockAmount(investorId, stockCompany.getId());
            int funds = investorService.getFunds(investorId);
            wallet.setProcessInfo(processId, funds, stockAmount);
        }
        return wallet;
    }

    private int getTransactionRate(Order orderA, Order orderB){
        if(!doOrdersMatch(orderA, orderB))
            throw new IllegalArgumentException("Orders do not match");
        return orderA.compareTo(orderB) < 0 ? orderA.getLimit() : orderB.getLimit();
    }

    private void binInsert(List<Order> list, Order order){
        int left = 0;
        int right = list.size();
        while(left < right){
            int mid = (left+right)/2;
            if(list.get(mid).compareTo(order) < 0){
                left = mid+1;
            }else{
                right = mid;
            }
        }

        int index = left;
        list.add(order);

        for(int i = list.size()-1; i > index; i--){
            list.set(i, list.get(i-1));
        }
        list.set(index, order);
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
        return orders.stream()
                .filter(order -> !order.isExpired(roundNo))
                .findFirst()
                .orElse(null);
    }

    private void prepareTemporaryWallets(){
        int investorsCount = investorService.count();
        temporaryWallets = new ArrayList<>(investorsCount);
        for(int i = 0; i < investorsCount; i++){
            temporaryWallets.add(new SingleStockWallet(i,0,0));
        }
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
