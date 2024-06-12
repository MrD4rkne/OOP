package StockExchange.Core;

import StockExchange.Investors.IInvestorService;
import StockExchange.Investors.Investor;
import StockExchange.Orders.Order;
import StockExchange.Orders.OrderType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradingSystem implements ITradingSystem, ITransactionInfoProvider{
    private static final int FIRST_ROUND_NO = 0;

    private int nextOrderId;

    private final int stocksCount;

    private final SheetsOrder[] sheetsOrders;

    private final IInvestorService investorService;

    private final List<Order> ordersToAdd;

    private int roundNo;

    public TradingSystem(int stocksCount, IInvestorService investorService) {
        this.stocksCount = stocksCount;
        this.investorService = investorService;
        this.roundNo = FIRST_ROUND_NO;
        this.sheetsOrders = new SheetsOrder[stocksCount];
        ordersToAdd=new ArrayList<>();
        seedSheetsOrders();
        nextOrderId = 0;
    }

    public Investor addInvestor(Investor investor){
        return investorService.registerInvestor(investor);
    }

    public void nextRound(){


        for(int i = 0; i < stocksCount; i++){
            sheetsOrders[i].processOrders(roundNo);
        }

        roundNo++;
    }

    private void askInvestorsForOrders(){
        Random random = new Random();
        int investorsToAsk = investorService.count();
        boolean[] wasAsked = new boolean[investorsToAsk];

        while(investorsToAsk > 0){
            int investorIndex = random.nextInt(0, investorService.count());
            if(wasAsked[investorIndex]){
                continue;
            }

            wasAsked[investorIndex] = true;
            investorsToAsk--;
            investorService.getInvestor(investorIndex).makeOrder(this);


        }

    }

    private void validateOrder(Order order, Investor investor){
        if(order == null){
            throw new IllegalArgumentException("Order cannot be null");
        }
        if(order.getStockId() < 0 || order.getStockId() >= stocksCount){
            throw new IllegalArgumentException("Invalid stock index");
        }
        if(order.getInvestorId() != investor.getId()){
            throw new IllegalArgumentException("Invalid investor ID");
        }
        if(order.getFirstRoundNo() != roundNo){
            throw new IllegalArgumentException("Invalid first round number");
        }

        if(Math.abs(order.getLimit() - getLastTransactionPrice(order.getStockId())) > 10){
            throw new IllegalArgumentException("Limit is too high. It should be in range <= 10 from last transaction price");
        }

        if(order.getType() == OrderType.BUY){
            int neededAmount = order.getAmount() * order.getLimit();
            if(neededAmount > investorService.getFunds(investor.getId())){
                throw new IllegalArgumentException("Not enough money to buy stocks");
            }
        }
        else {
            if(order.getAmount() > investorService.getStockAmount(investor.getId(), order.getStockId())){
                throw new IllegalArgumentException("Not enough stocks to sell");
            }
        }

        // Check for already added orders
        for(Order o : ordersToAdd){
            if(o.getId() == order.getId()){
                throw new IllegalArgumentException("Order with this ID is already added");
            }
        }

    }

    private void seedSheetsOrders(){
        for(int i = 0; i < stocksCount; i++){
            sheetsOrders[i] = new SheetsOrder(i,investorService);
        }
    }

    @Override
    public int getCurrentRoundNo() {
        return roundNo;
    }

    @Override
    public int getLastTransactionPrice(int stockIndex) {
        if(stockIndex < 0 || stockIndex >= stocksCount){
            throw new IllegalArgumentException("Invalid stock index");
        }
        return sheetsOrders[stockIndex].getLatestTransactionPrice();
    }

    @Override
    public void addOrder(Investor investor, Order order) {
        validateOrder(order,investor);
        order.setId(nextOrderId++);
        sheetsOrders[order.getStockId()].insertOrder(order);
    }
}
