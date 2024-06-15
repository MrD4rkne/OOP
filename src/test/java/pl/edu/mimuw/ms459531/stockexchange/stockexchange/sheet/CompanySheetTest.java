package pl.edu.mimuw.ms459531.stockexchange.stockexchange.sheet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.stockexchange.companies.CompanySheet;
import pl.edu.mimuw.ms459531.stockexchange.companies.ISheet;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.investors.IInvestorService;

import static org.mockito.Mockito.mock;

public class CompanySheetTest {
    @Test
    public void testLastTransactionPrice() {
        final int lastTransactionPrice = 10;
        StockCompany company = new StockCompany(0, "ABCD");
        IInvestorService investorService = mock(IInvestorService.class);
        ISheet sheet = new CompanySheet(company, lastTransactionPrice, investorService);

        // Act
        int price = sheet.getLatestTransactionPrice();
        int stockId = sheet.getStockId();
        int ordersCount = sheet.getOrdersCount();
        StockCompany stockCompany = sheet.getStockCompany();

        // Assert
        Assertions.assertEquals(lastTransactionPrice, price);
        Assertions.assertEquals(company.id(), stockId);
        Assertions.assertEquals(0, ordersCount);
        Assertions.assertEquals(company, stockCompany);
    }
}
