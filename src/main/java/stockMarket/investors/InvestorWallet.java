package stockMarket.investors;

import stockMarket.companies.StockCompany;
import stockMarket.core.Share;
import stockMarket.core.Wallet;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an investor's wallet in the stock market.
 */
public class InvestorWallet extends Wallet {
    private final List<Share> shares;

    public InvestorWallet(int investorId, StockCompany[] stockCompanies) {
        this(investorId,0,stockCompanies);
    }

    public InvestorWallet(int investorId, int funds, StockCompany[] stockCompanies) {
        super(investorId, funds);
        this.shares = new ArrayList<>();
        seedStocks(stockCompanies);
    }

    public boolean hasShares(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        validateStockId(stockId);

        return shares.get(stockId).getAmount() >= amount;
    }

    public void addShares(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        validateStockId(stockId);
        
        shares.get(stockId).addAmount(amount);
    }

    public void removeShares(int stockId, int amount) {
        if(amount <= 0) {
            throw new IllegalArgumentException("Amount cannot be non-positive");
        }
        if(!hasShares(stockId, amount)) {
            throw new IllegalArgumentException("Not enough Stocks");
        }

        shares.get(stockId).removeAmount(amount);
    }

    public int getSharesOfCompany(int stockId) {
        validateStockId(stockId);
        return shares.get(stockId).getAmount();
    }

    public List<Share> getAllOwnShares() {
        return new ArrayList<>(shares);
    }
    
    private void validateStockId(int stockId) {
        if(stockId < 0 || stockId >= shares.size()) {
            throw new IllegalArgumentException("StockId is invalid");
        }
    }
    
    private void seedStocks(StockCompany[] stockCompanies) {
        for (StockCompany stockCompany : stockCompanies) {
            shares.add(new Share(stockCompany, 0));
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFunds());
        sb.append(" ");
        for(int i = 0; i < shares.size(); i++) {
            Share share = this.shares.get(i);
            sb.append(share.getStockCompany().name());
            sb.append(": ");
            sb.append(share.getAmount());
            if(i < shares.size() - 1) sb.append(" ");
        }
        return sb.toString();
    }
}
