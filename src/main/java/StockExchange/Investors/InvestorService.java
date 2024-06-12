package StockExchange.Investors;

import StockExchange.Core.StockVm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class InvestorService implements IInvestorService{
    private final List<InvestorWallet> wallets;

    private final List<Investor> investors;

    private final int stocksCount;

    public InvestorService(int stocksCount) {
        this.wallets = new ArrayList<>();
        this.stocksCount = stocksCount;
        this.investors = new ArrayList<>();
    }

    @Override
    public Investor registerInvestor(Investor investor) {
        int id = wallets.size();
        investor.setId(id);

        wallets.add(new InvestorWallet(id, stocksCount));
        investors.add(investor);
        return investor;
    }

    @Override
    public Investor getInvestor(int investorId) {
        if(!doesInvestorExist(investorId)){
            throw new IllegalArgumentException("Investor with this id does not exist");
        }

        return investors.get(investorId);
    }

    @Override
    public int count() {
        return investors.size();
    }

    @Override
    public boolean doesInvestorExist(int investorId) {
        return investorId>=0 && investorId<wallets.size();
    }

    @Override
    public void addFunds(int investorId, int amount) {
        Optional<InvestorWallet> walletToAddFunds = getWalletByInvestorId(investorId);
        if(walletToAddFunds.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }

        walletToAddFunds.get().addFunds(amount);
    }

    @Override
    public boolean hasFunds(int investorId, int amount) {
        Optional<InvestorWallet> walletToCheckFunds = getWalletByInvestorId(investorId);
        if(walletToCheckFunds.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }

        return walletToCheckFunds.get().hasFunds(amount);
    }

    @Override
    public void removeFunds(int investorId, int amount) {
        Optional<InvestorWallet> walletToRemoveFunds = getWalletByInvestorId(investorId);
        if(walletToRemoveFunds.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }

        walletToRemoveFunds.get().removeFunds(amount);
    }

    @Override
    public int getFunds(int investorId) {
        Optional<InvestorWallet> walletToRemoveFunds = getWalletByInvestorId(investorId);
        if(walletToRemoveFunds.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }
        return walletToRemoveFunds.get().getFunds();
    }

    @Override
    public void addStock(int investorId, int stockId, int amount) {
        Optional<InvestorWallet> walletToAddStock = getWalletByInvestorId(investorId);
        if(walletToAddStock.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }

        walletToAddStock.get().addStocks(stockId, amount);
    }

    @Override
    public boolean hasStock(int investorId, int stockId, int amount) {
        Optional<InvestorWallet> walletToCheckStock = getWalletByInvestorId(investorId);
        if(walletToCheckStock.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }

        return walletToCheckStock.get().hasStocks(stockId, amount);
    }

    @Override
    public void removeStock(int investorId, int stockId, int amount) {
        Optional<InvestorWallet> walletToRemoveStock = getWalletByInvestorId(investorId);
        if(walletToRemoveStock.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }

        walletToRemoveStock.get().removeStocks(stockId, amount);
    }

    @Override
    public int getStockAmount(int investorId, int stockId) {
        Optional<InvestorWallet> walletToRemoveStock = getWalletByInvestorId(investorId);
        if(walletToRemoveStock.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }
        return walletToRemoveStock.get().getStocksAmount(stockId);
    }

    @Override
    public InvestorWalletVm getWallet(int investorId) {
        Optional<InvestorWallet> walletToRemoveStock = getWalletByInvestorId(investorId);
        if(walletToRemoveStock.isEmpty()){
            throw new IllegalArgumentException("Wallet with this investor's id does not exist");
        }

        InvestorWallet wallet = walletToRemoveStock.get();
        StockVm[] stockVms = wallet.getStocks().stream().map(stock -> new StockVm(stock.getStockId(), stock.getAmount())).toArray(StockVm[]::new);
        return new InvestorWalletVm(wallet.getInvestorId(), wallet.getFunds(), stockVms);
    }

    private Optional<InvestorWallet> getWalletByInvestorId(int investorId){
        return wallets.stream().filter(wallet -> wallet.getInvestorId() == investorId).findFirst();
    }
}
