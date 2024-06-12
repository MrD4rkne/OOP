package StockExchange.Investors;

import StockExchange.Core.ITransactionInfoProvider;

public abstract class Investor {
    private int id;
    
    public Investor() {
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public abstract void makeOrder(ITransactionInfoProvider transactionInfoProvider);
}
