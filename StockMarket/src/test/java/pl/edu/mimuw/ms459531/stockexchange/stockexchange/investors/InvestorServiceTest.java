package pl.edu.mimuw.ms459531.stockexchange.stockexchange.investors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.investors.Investor;
import pl.edu.mimuw.ms459531.stockexchange.investors.InvestorService;
import pl.edu.mimuw.ms459531.stockexchange.investors.InvestorWalletVm;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

class InvestorServiceTest {

    private InvestorService investorService;
    private StockCompany[] stockCompanies;

    @BeforeEach
    void setUp() {
        stockCompanies = new StockCompany[]{
                new StockCompany(0, "A"),
                new StockCompany(1, "B")
        };
        investorService = new InvestorService(stockCompanies);
    }

    @Test
    void registerInvestor_shouldAddInvestor() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        assertEquals(1, investorService.count());
        assertEquals(investor, investorService.getInvestor(0));
    }

    @Test
    void getInvestor_shouldReturnInvestor() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        Investor retrievedInvestor = investorService.getInvestor(0);
        assertEquals(investor, retrievedInvestor);
    }

    @Test
    void getInvestor_shouldThrowExceptionIfNotExists() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> investorService.getInvestor(0));
    }

    @Test
    void addFunds_shouldIncreaseFunds() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addFunds(0, 1000);
        assertEquals(1000, investorService.getFunds(0));
    }

    @Test
    void removeFunds_shouldDecreaseFunds() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addFunds(0, 1000);
        investorService.removeFunds(0, 500);

        assertEquals(500, investorService.getFunds(0));
    }

    @Test
    void removeFunds_shouldThrowExceptionIfInsufficientFunds() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addFunds(0, 1000);
        Assertions.assertThrows(IllegalArgumentException.class, () -> investorService.removeFunds(0, 1500));
    }

    @Test
    void hasFunds_shouldReturnTrueIfEnoughFunds() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addFunds(0, 1000);
        Assertions.assertTrue(investorService.hasFunds(0, 500));
    }

    @Test
    void hasFunds_shouldReturnFalseIfNotEnoughFunds() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addFunds(0, 1000);
        Assertions.assertFalse(investorService.hasFunds(0, 1500));
    }

    @Test
    void addStock_shouldIncreaseStockAmount() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addStock(0, 0, 100);
        assertEquals(100, investorService.getStockAmount(0, 0));
    }

    @Test
    void removeStock_shouldDecreaseStockAmount() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addStock(0, 0, 100);
        investorService.removeStock(0, 0, 50);

        assertEquals(50, investorService.getStockAmount(0, 0));
    }

    @Test
    void removeStock_shouldThrowExceptionIfInsufficientStock() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addStock(0, 0, 100);
        Assertions.assertThrows(IllegalArgumentException.class, () -> investorService.removeStock(0, 0, 150));
    }

    @Test
    void hasStock_shouldReturnTrueIfEnoughStock() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addStock(0, 0, 100);
        Assertions.assertTrue(investorService.hasStock(0, 0, 50));
    }

    @Test
    void hasStock_shouldReturnFalseIfNotEnoughStock() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);

        investorService.addStock(0, 0, 100);
        Assertions.assertFalse(investorService.hasStock(0, 0, 150));
    }

    @Test
    void getWallet_shouldReturnWalletVm() {
        Investor investor = mock(Investor.class);
        investorService.registerInvestor(investor);
        investorService.addFunds(0, 1000);
        investorService.addStock(0, 0, 100);

        InvestorWalletVm walletVm = investorService.getWallet(0);
        assertEquals(0, walletVm.getInvestorId());
        assertEquals(1000, walletVm.getFunds());
        assertEquals(100, walletVm.getShares(0).amount());
        assertEquals(stockCompanies[0], walletVm.getShares(0).stockCompany());
    }
}
