package StockExchange.Core;

import StockExchange.Investors.IInvestorService;
import StockExchange.Orders.Order;
import StockExchange.Orders.OrderType;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class SheetsOrder {
    private final int stockId;
    
    private final List<Order> orders;

    private final IInvestorService investorService;

    private List<TemporaryWallet> temporaryWallets;
    
    public SheetsOrder(int stockId, IInvestorService investorService) {
        this.stockId = stockId;
        this.orders = new ArrayList<>();
        this.investorService = investorService;
    }
    
    public int getStockId() {
        return stockId;
    }
    
    public void insertOrder(Order order) {
        orders.add(order);
    }
    
    public void processOrders(int roundNo){
        prepareTemporaryWallets();

        List<Order> buyOrders = getSorted(OrderType.BUY);
        List<Order> saleOrders = getSorted(OrderType.SALE);

        int i = 0;

        while(!buyOrders.isEmpty() || !saleOrders.isEmpty()){
            Order orderToProcess = getNextOrderToProcess(buyOrders, saleOrders);

            if(!tryProcess(orderToProcess, buyOrders.iterator(), saleOrders.iterator(), roundNo,i++)){
                if(orderToProcess.getType() == OrderType.BUY) {
                    buyOrders.remove(orderToProcess);
                }else {
                    saleOrders.remove(orderToProcess);
                }
            }
        }
    }

    private boolean tryProcess(Order orderToProcess, Iterator<Order> buyOrders, Iterator<Order> sellOrders, int roundNo, int processId){
        if(orderToProcess.isExpired(roundNo)){
            return false;
        }

        List<TransactionInfo> transactions = new ArrayList<>();
        int currentDemand = orderToProcess.getAmount();
        Order currentOrder = orderToProcess;

        while(!wasProperlyProcessed(currentOrder, currentDemand)){
            try {
                Order possibleOrder = currentOrder.getType() == OrderType.SALE ? buyOrders.next() : sellOrders.next();
                if(possibleOrder == orderToProcess)
                    continue;

                if(possibleOrder.isExpired(roundNo)){
                    continue;
                }

                TransactionInfo possibleTransaction = tryInitTransaction(currentOrder, currentDemand, roundNo, processId, possibleOrder);
                if(possibleTransaction == null)
                    break;

                if(currentDemand < possibleOrder.getAmount()){
                    currentDemand = possibleOrder.getAmount()-possibleTransaction.amount();
                    currentOrder = possibleOrder;
                }else{
                    currentDemand -= possibleTransaction.amount();
                }

                transactions.add(possibleTransaction);

            }catch (NoSuchElementException e){
                break;
            }
        }

        if(!wasProperlyProcessed(currentOrder, currentDemand))
            return false;

        transactions.forEach(this::finalizeTransaction);

        return true;
    }

    private void finalizeTransaction(TransactionInfo transaction){
        int buyerId = transaction.buyOrder().getInvestor().getId();
        int sellerId = transaction.sellOrder().getInvestor().getId();

        transaction.buyOrder().complete(transaction.roundNo(), transaction.amount());
        transaction.sellOrder().complete(transaction.roundNo(), transaction.amount());

        investorService.addStock(buyerId, stockId, transaction.amount());
        investorService.removeStock(sellerId, stockId, transaction.amount());

        investorService.removeFunds(buyerId, transaction.getTotalValue());
        investorService.addFunds(sellerId, transaction.getTotalValue());
    }

    private TransactionInfo tryInitTransaction(Order currentOrder, int currentDemand, int roundNo, int processId, Order possibleOrder)
    {
        if (!doOrdersMatch(currentOrder, possibleOrder)) {
            return null;
        }

        Order buyOrder = currentOrder.getType() == OrderType.BUY ? currentOrder : possibleOrder;
        Order sellOrder = currentOrder.getType() == OrderType.SALE ? currentOrder : possibleOrder;

        TemporaryWallet buyersWallet = updateWalletIfNecessary(buyOrder.getInvestor().getId(), processId);
        TemporaryWallet sellersWallet = updateWalletIfNecessary(sellOrder.getInvestor().getId(), processId);

        int transactionRate = getTransactionRate(buyOrder, sellOrder);
        int amountPossibleForBuyer = (int)(buyersWallet.getFunds() / transactionRate);
        int possibleAmount = Math.min(currentDemand, Math.min(amountPossibleForBuyer, sellersWallet.getStockAmount()));
        if(possibleAmount == 0)
            return null;

        // Orders need to be processed in order
        if(currentDemand< possibleAmount && possibleAmount < possibleOrder.getAmount()){
            return null;
        }

        int transactionFunds = possibleAmount * transactionRate;
        buyersWallet.removeFunds(transactionFunds);
        buyersWallet.addStock(possibleAmount);
        sellersWallet.removeStock(possibleAmount);
        sellersWallet.addFunds(transactionFunds);
        return new TransactionInfo(buyOrder, sellOrder, possibleAmount, transactionRate, roundNo);
    }

    private TemporaryWallet updateWalletIfNecessary(int investorId, int processId){
        TemporaryWallet wallet = temporaryWallets.get(investorId);
        if(wallet.getProcessCounter() != processId) {
            int stockAmount = investorService.getStockAmount(investorId, stockId);
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

    private Order getNextOrderToProcess(List<Order> buyOrders, List<Order> sellOrders){
        if(buyOrders.isEmpty() && sellOrders.isEmpty()){
            throw new IllegalArgumentException("There are no orders to process");
        }

        if(buyOrders.isEmpty())
            return sellOrders.get(0);
        if(sellOrders.isEmpty())
            return buyOrders.get(0);

        Order buyOrder = buyOrders.get(0);
        Order sellOrder = sellOrders.get(0);

        return buyOrder.compareTo(sellOrder) < 0 ? buyOrder : sellOrder;
    }

    private void prepareTemporaryWallets(){
        int investorsCount = investorService.count();
        temporaryWallets = new ArrayList<>(investorsCount);
        for(int i = 0; i < investorsCount; i++){
            temporaryWallets.add(new TemporaryWallet(i,0,0));
        }
    }
    
    private List<Order> getSorted(OrderType type){
        return orders.stream()
                .filter(order -> order.getType() == type)
                .collect(toList());
    }
 }
