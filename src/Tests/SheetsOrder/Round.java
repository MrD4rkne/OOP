package Tests.SheetsOrder;

import StockExchange.Core.SheetsOrder;
import StockExchange.Core.TransactionInfo;
import StockExchange.Investors.Investor;
import StockExchange.Investors.InvestorService;
import StockExchange.Orders.FillOrKillOrder;
import StockExchange.Orders.GoodTillCancelledOrder;
import StockExchange.Orders.OrderType;
import org.junit.jupiter.api.Test;

import java.util.List;

public class Round {
    @Test
    void processOrders() {
        // Arrange
        InvestorService investorService = new InvestorService();
                Investor[] investors = seedInvestors(investorService, 5);
        SheetsOrder sheetsOrder = new SheetsOrder(0, investorService);

        investorService.addFunds(0, 150*100);
        sheetsOrder.insertOrder(new FillOrKillOrder(0, OrderType.BUY, investors[0], 0,150, 100, 0));

        investorService.addStock(1, 0, 100);
        sheetsOrder.insertOrder(new FillOrKillOrder(1, OrderType.SALE, investors[1], 0, 100, 100, 0));

        investorService.addStock(2,0,100);
        sheetsOrder.insertOrder(new FillOrKillOrder(2, OrderType.SALE, investors[2], 0, 100, 100, 0));

        investorService.addFunds(3, 200*100);
        sheetsOrder.insertOrder(new FillOrKillOrder(3, OrderType.BUY, investors[3], 0, 200, 100, 0));

        investorService.addFunds(4, 1000*100);
        sheetsOrder.insertOrder(new GoodTillCancelledOrder(4, OrderType.BUY, investors[4], 0, 1000, 100, 0));

        // Act
        sheetsOrder.processOrders(0);
        List<TransactionInfo> transactions = sheetsOrder.getTransactions();

    }

    private Investor[] seedInvestors(InvestorService investorService, int n) {
        Investor[] investors = new Investor[n];
        for (int i = 0; i < n; i++) {
            investors[i] = new Investor(investorService.registerInvestor()){};
        }
        return investors;
    }
}
