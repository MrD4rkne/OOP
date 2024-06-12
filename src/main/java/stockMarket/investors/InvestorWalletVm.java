package stockMarket.investors;

import stockMarket.stock.Share;
import stockMarket.stock.ShareVm;

import java.util.Arrays;

public class InvestorWalletVm {
    private final int investorId;

    private final int funds;

    private final ShareVm[] shares;

    public InvestorWalletVm(int investorId, int funds, ShareVm[] stocks) {
        this.investorId = investorId;
        this.funds = funds;
        this.shares = Arrays.copyOf(stocks, stocks.length);
    }

    public int getInvestorId() {
        return investorId;
    }

    public int getFunds() {
        return funds;
    }

    public ShareVm getShares(int stockId) {
        if(stockId < 0 || stockId >= shares.length) {
            throw new IllegalArgumentException("Stock with this id does not exist");
        }
        return shares[stockId];
    }
    
    public int getStocksCount() {
        return shares.length;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getInvestorId());
        sb.append(" ");
        sb.append(getFunds());
        sb.append(" ");
        for(int i = 0; i < shares.length; i++) {
            ShareVm share = this.shares[i];
            sb.append(share.getStockCompany().getName());
            sb.append(": ");
            sb.append(share.getAmount());
            if(i < shares.length - 1) sb.append(" ");
        }
        return sb.toString();
    }
}