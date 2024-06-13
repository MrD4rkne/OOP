package stockMarket.stock;

import stockMarket.core.StockCompany;

public interface IReadonlySheet {
    int getStockId();

    int getOrdersCount();

    int getLatestTransactionPrice();

    StockCompany getStockCompany();
}
