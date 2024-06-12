package stockMarket.stock;

public interface IReadonlySheet {
    int getStockId();

    int getOrdersCount();

    int getLatestTransactionPrice();
}
