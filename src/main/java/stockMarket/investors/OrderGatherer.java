package stockMarket.investors;

import stockMarket.companies.StockCompany;
import stockMarket.orders.Order;
import stockMarket.orders.OrderType;
import stockMarket.companies.IReadonlySheet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class OrderGatherer implements IOrderGatherer {
    private final IReadonlyInvestorService investorService;
    private final IReadonlySheet[] sheets;
    private final List<Order> ordersToAdd;
    private int roundNo;
    private int nextOrderId;

    public OrderGatherer(IReadonlyInvestorService investorService, IReadonlySheet[] sheets) {
        this.investorService = investorService;
        this.sheets = Arrays.copyOf(sheets, sheets.length);
        this.ordersToAdd = new ArrayList<>();
        this.roundNo = -1;
        this.nextOrderId = 0;
    }

    @Override
    public List<Order> getOrders(int roundNo) {
        this.roundNo = roundNo;
        ordersToAdd.clear();

        Random random = new Random();
        int investorsToAsk = investorService.count();
        boolean[] wasAsked = new boolean[investorsToAsk];

        while (investorsToAsk > 0) {
            int investorIndex = random.nextInt(0, investorService.count());
            if (wasAsked[investorIndex]) {
                continue;
            }

            wasAsked[investorIndex] = true;
            investorsToAsk--;
            InvestorWalletVm wallet = investorService.getWallet(investorIndex);
            investorService.getInvestor(investorIndex).makeOrder(this, wallet);
        }
        return ordersToAdd;
    }

    @Override
    public int getCurrentRoundNo() {
        if (roundNo == -1) {
            throw new IllegalStateException("Round number not set");
        }
        return roundNo;
    }

    @Override
    public int getLastTransactionPrice(int stockIndex) {
        if (stockIndex < 0 || stockIndex >= sheets.length) {
            throw new IllegalArgumentException("Invalid stock index");
        }
        return sheets[stockIndex].getLatestTransactionPrice();
    }

    @Override
    public void addOrder(Investor investor, Order order) {
        validateOrder(order, investor);
        order.setId(nextOrderId++);
        ordersToAdd.add(order);
    }

    @Override
    public StockCompany getStock(int companyId) {
        if (companyId < 0 || companyId >= sheets.length) {
            throw new IllegalArgumentException("Invalid stock index");
        }
        return sheets[companyId].getStockCompany();
    }

    private void validateOrder(Order order, Investor investor) {
        if (order == null) {
            throw new IllegalArgumentException("Order cannot be null");
        }
        if (order.getStockId() >= sheets.length) {
            throw new IllegalArgumentException("Invalid stock index");
        }
        if (order.getInvestorId() != investor.getId()) {
            throw new IllegalArgumentException("Invalid investor ID");
        }
        if (order.getFirstRoundNo() != roundNo) {
            throw new IllegalArgumentException("Invalid first round number");
        }

        if (Math.abs(order.getLimit() - getLastTransactionPrice(order.getStockId())) > 10) {
            throw new IllegalArgumentException("Limit is too high. It should be in range <= 10 from last transaction price");
        }

        if (order.getType() == OrderType.BUY) {
            int neededAmount = order.getAmount() * order.getLimit();
            if (neededAmount > investorService.getFunds(investor.getId())) {
                throw new IllegalArgumentException("Not enough money to buy stocks");
            }
        } else {
            if (order.getAmount() > investorService.getStockAmount(investor.getId(), order.getStockId())) {
                throw new IllegalArgumentException("Not enough stocks to sell");
            }
        }

        // Check for already added orders
        for (Order o : ordersToAdd) {
            if (o.getInvestorId() == order.getInvestorId()) {
                throw new IllegalArgumentException("Order with this ID is already added");
            }
        }

    }
}
