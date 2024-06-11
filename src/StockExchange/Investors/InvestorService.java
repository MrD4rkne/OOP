package StockExchange.Investors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InvestorService implements IInvestorService{
    private final List<InvestorWallet> wallets;

    private final int stocksCount;

    public InvestorService(int stocksCount) {
        this.wallets = new ArrayList<>();
        this.stocksCount = stocksCount;
    }

    @Override
    public int registerInvestor() {
        int id = wallets.size();
        wallets.add(new InvestorWallet(id, stocksCount));
        return id;
    }

    @Override
    public int count() {
        return wallets.size();
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

    private Optional<InvestorWallet> getWalletByInvestorId(int investorId){
        return wallets.stream().filter(wallet -> wallet.getInvestorId() == investorId).findFirst();
    }
}
