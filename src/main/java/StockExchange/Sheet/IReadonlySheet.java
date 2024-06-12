package StockExchange.Sheet;

public interface IReadonlySheet {
    int getStockId();

    int getOrdersCount();

    int getLatestTransactionPrice();
}
