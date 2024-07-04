package pl.edu.mimuw.ms459531.stockexchange.companies;

/**
 * IReadonlySheet interface provides the methods for reading the stock sheet data of company.
 */
public interface IReadonlySheet {
    int getStockId();

    int getOrdersCount();

    int getLatestTransactionPrice();

    StockCompany getStockCompany();
}
