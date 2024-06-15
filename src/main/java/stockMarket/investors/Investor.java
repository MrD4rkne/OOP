package stockMarket.investors;

/**
 * Represents an investor in the stock market.
 */
public abstract class Investor {
    private int id;

    public Investor() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * Give investor possibility to make an order.
     *
     * @param transactionInfoProvider the transaction information provider
     * @param wallet                  the wallet of the investor
     */
    public abstract void makeOrder(ITransactionInfoProvider transactionInfoProvider, InvestorWalletVm wallet);

    public abstract String toString();
}
