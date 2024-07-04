package pl.edu.mimuw.ms459531.stockexchange.stockexchange.core;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.edu.mimuw.ms459531.main.SimulationData;
import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;
import pl.edu.mimuw.ms459531.stockexchange.core.IStockMarketLogger;
import pl.edu.mimuw.ms459531.stockexchange.core.ITradingSystem;
import pl.edu.mimuw.ms459531.stockexchange.core.TradingSystem;
import pl.edu.mimuw.ms459531.stockexchange.investors.*;

import java.util.Random;

import static org.mockito.Mockito.mock;

public class TradingSystemTest {
    @Test
    void simulate_random() {
        // Arrange
        SimulationData simulationData = new SimulationData();
        simulationData.addCompany("A", 100);
        simulationData.addCompany("B", 10000);
        simulationData.addCompany("C", 10);

        addRandomInvestors(simulationData, 3);
        simulationData.setStartingFundAmount(1000);
        simulationData.setStartingAmounts(new int[]{100, 20, 3000});

        final int roundsCount = 100000;
        IInvestorService investorService = new InvestorService(simulationData.getCompanies());
        ITradingSystem tradingSystem = createTradingSystem(simulationData, investorService, roundsCount);
        int initialFundsSum = sumFunds(investorService);
        int[] initialStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        // Act
        int actualRoundsCount = runSimilation(tradingSystem, roundsCount);

        // Assert
        Assertions.assertEquals(roundsCount, actualRoundsCount);

        int finalFundsSum = sumFunds(investorService);
        int[] finalStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        Assertions.assertEquals(initialFundsSum, finalFundsSum);
        Assertions.assertArrayEquals(initialStocksSum, finalStocksSum);
    }

    @Test
    void simulate_randomAndSma() {
        // Arrange
        SimulationData simulationData = new SimulationData();
        simulationData.addCompany("A", 100);
        simulationData.addCompany("B", 10000);
        simulationData.addCompany("C", 10);

        addRandomInvestors(simulationData, 3);
        addSMAInvestors(simulationData, 2);
        simulationData.setStartingFundAmount(1000);
        simulationData.setStartingAmounts(new int[]{100, 20, 3000});

        final int roundsCount = 100000;
        IInvestorService investorService = new InvestorService(simulationData.getCompanies());
        ITradingSystem tradingSystem = createTradingSystem(simulationData, investorService, roundsCount);
        int initialFundsSum = sumFunds(investorService);
        int[] initialStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        // Act
        int actualRoundsCount = runSimilation(tradingSystem, roundsCount);

        // Assert
        Assertions.assertEquals(roundsCount, actualRoundsCount);

        int finalFundsSum = sumFunds(investorService);
        int[] finalStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        Assertions.assertEquals(initialFundsSum, finalFundsSum);
        Assertions.assertArrayEquals(initialStocksSum, finalStocksSum);
    }

    @Test
    void simulate_example() {
        // Arrange
        SimulationData simulationData = new SimulationData();
        simulationData.addCompany("APL", 145);
        simulationData.addCompany("MSFT", 300);
        simulationData.addCompany("GOOGL", 2700);

        addRandomInvestors(simulationData, 4);
        addSMAInvestors(simulationData, 2);
        simulationData.setStartingFundAmount(100000);
        simulationData.setStartingAmounts(new int[]{5, 15, 3});

        final int roundsCount = 100000;
        IInvestorService investorService = new InvestorService(simulationData.getCompanies());
        ITradingSystem tradingSystem = createTradingSystem(simulationData, investorService, roundsCount);
        int initialFundsSum = sumFunds(investorService);
        int[] initialStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        // Act
        int actualRoundsCount = runSimilation(tradingSystem, roundsCount);

        // Assert
        Assertions.assertEquals(roundsCount, actualRoundsCount);

        int finalFundsSum = sumFunds(investorService);
        int[] finalStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        Assertions.assertEquals(initialFundsSum, finalFundsSum);
        Assertions.assertArrayEquals(initialStocksSum, finalStocksSum);
    }

    @Test
    void simulate_one_stock() {
        // Arrange
        SimulationData simulationData = new SimulationData();
        simulationData.addCompany("APL", 145);

        addRandomInvestors(simulationData, 4);
        addSMAInvestors(simulationData, 2);
        simulationData.setStartingFundAmount(100000);
        simulationData.setStartingAmounts(new int[]{5});

        final int roundsCount = 100000;
        IInvestorService investorService = new InvestorService(simulationData.getCompanies());
        ITradingSystem tradingSystem = createTradingSystem(simulationData, investorService, roundsCount);
        int initialFundsSum = sumFunds(investorService);
        int[] initialStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        // Act
        int actualRoundsCount = runSimilation(tradingSystem, roundsCount);

        // Assert
        Assertions.assertEquals(roundsCount, actualRoundsCount);

        int finalFundsSum = sumFunds(investorService);
        int[] finalStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        Assertions.assertEquals(initialFundsSum, finalFundsSum);
        Assertions.assertArrayEquals(initialStocksSum, finalStocksSum);
    }

    @Test
    void one_random() {
        // Arrange
        SimulationData simulationData = new SimulationData();
        simulationData.addCompany("APL", 145);
        simulationData.addCompany("SAMSU", 20000);

        addRandomInvestors(simulationData, 1);
        simulationData.setStartingFundAmount(100000);
        simulationData.setStartingAmounts(new int[]{5, 1});

        final int roundsCount = 100000;
        IInvestorService investorService = new InvestorService(simulationData.getCompanies());
        ITradingSystem tradingSystem = createTradingSystem(simulationData, investorService, roundsCount);
        int initialFundsSum = sumFunds(investorService);
        int[] initialStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        // Act
        int actualRoundsCount = runSimilation(tradingSystem, roundsCount);

        // Assert
        Assertions.assertEquals(roundsCount, actualRoundsCount);

        int finalFundsSum = sumFunds(investorService);
        int[] finalStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        Assertions.assertEquals(initialFundsSum, finalFundsSum);
        Assertions.assertArrayEquals(initialStocksSum, finalStocksSum);
    }

    @Test
    void one_sma() {
        // Arrange
        SimulationData simulationData = new SimulationData();
        simulationData.addCompany("APL", 145);
        simulationData.addCompany("SAMSU", 20000);

        addSMAInvestors(simulationData, 1);
        simulationData.setStartingFundAmount(100000);
        simulationData.setStartingAmounts(new int[]{5, 1});

        final int roundsCount = 100000;
        IInvestorService investorService = new InvestorService(simulationData.getCompanies());
        ITradingSystem tradingSystem = createTradingSystem(simulationData, investorService, roundsCount);
        int initialFundsSum = sumFunds(investorService);
        int[] initialStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        // Act
        int actualRoundsCount = runSimilation(tradingSystem, roundsCount);

        // Assert
        Assertions.assertEquals(roundsCount, actualRoundsCount);

        int finalFundsSum = sumFunds(investorService);
        int[] finalStocksSum = sumStocks(investorService, simulationData.getCompanies().length);

        Assertions.assertEquals(initialFundsSum, finalFundsSum);
        Assertions.assertArrayEquals(initialStocksSum, finalStocksSum);
    }

    private void addRandomInvestors(SimulationData simulationData, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            simulationData.increaseRandomInvestorsCount();
        }
    }

    private void addSMAInvestors(SimulationData simulationData, int count) {
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            simulationData.increaseSmaInvestorCount();
        }
    }

    private ITradingSystem createTradingSystem(SimulationData simulationData, IInvestorService service, int roundsCount) {
        IStockMarketLogger logger = mock(IStockMarketLogger.class);
        return createTradingSystem(logger, simulationData, service, roundsCount);
    }

    private ITradingSystem createTradingSystem(IStockMarketLogger logger, SimulationData simulationData, IInvestorService service, int roundsCount) {
        StockCompany[] stockCompanies = simulationData.getCompanies();
        int[] lastRoundPrices = simulationData.getCompaniesStartingPrices();

        seedInvestors(service, simulationData.getRandomInvestorsCount(), simulationData.getSmaInvestorCount(), simulationData.getStartingFundAmount(), simulationData.getStartingAmounts());

        return new TradingSystem(logger, service, stockCompanies, lastRoundPrices, roundsCount);
    }

    private void seedInvestors(IInvestorService investorService, int randomCount, int smaCount, int initialFunds, int[] initialShares) {
        for (int i = 0; i < randomCount; i++) {
            investorService.registerInvestor(new RandomInvestor(new Random()));
        }

        SmaCalculator smaCalculator = new SmaCalculator(initialShares.length);
        for (int i = 0; i < smaCount; i++) {
            investorService.registerInvestor(new SmaInvestor(smaCalculator, new Random()));
        }

        for (int i = 0; i < investorService.count(); i++) {
            investorService.addFunds(i, initialFunds);
            for (int j = 0; j < initialShares.length; j++) {
                investorService.addStock(i, j, initialShares[j]);
            }
        }
    }

    private int runSimilation(ITradingSystem tradingSystem, int expectedRoundsCount) {
        int i = 0;
        while (tradingSystem.hasNextRound()) {
            Assertions.assertDoesNotThrow(tradingSystem::nextRound);
            i++;
        }
        return i;
    }

    private int sumFunds(IInvestorService investorService) {
        int sum = 0;
        for (int i = 0; i < investorService.count(); i++) {
            sum += investorService.getFunds(i);
        }
        return sum;
    }

    private int[] sumStocks(IInvestorService investorService, int companiesCount) {
        int[] sum = new int[companiesCount];
        for (int i = 0; i < investorService.count(); i++) {
            for (int j = 0; j < companiesCount; j++) {
                sum[j] += investorService.getStockAmount(i, j);
            }
        }
        return sum;
    }
}
