package stockMarket.investors;

import stockMarket.stock.ShareVm;

import java.util.Arrays;

public class InvestorWalletVm {
    private final int investorId;

    private final int funds;

    private final ShareVm[] stocks;

    public InvestorWalletVm(int investorId, int funds, ShareVm[] stocks) {
        this.investorId = investorId;
        this.funds = funds;
        this.stocks = Arrays.copyOf(stocks, stocks.length);
    }

    public int getInvestorId() {
        return investorId;
    }

    public int getFunds() {
        return funds;
    }

    public ShareVm getStock(int stockId) {
        if(stockId < 0 || stockId >= stocks.length) {
            throw new IllegalArgumentException("Stock with this id does not exist");
        }
        return stocks[stockId];
    }

    @Override
    public String toString() {
        return "InvestorWallet{" +
                "investorId=" + investorId +
                ", funds=" + funds +
                ", stocks=" + Arrays.toString(stocks) +
                '}';
    }
}
