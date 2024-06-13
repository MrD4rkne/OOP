package stockMarket.investors;

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

    public abstract void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet);
    
    public abstract String toString();
}
