package main;

import stockMarket.core.IStockMarketLogger;
import stockMarket.core.ITradingSystem;
import stockMarket.core.TradingSystem;
import stockMarket.investors.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Use with caution, each round transactions, sheets and wallets will be logged.
        final boolean shouldLog = false;

        // Validate arguments' count.
        if (args.length != 2) {
            System.out.println("Usage: <inputFile> <roundsCount>");
            System.exit(1);
            return;
        }

        // Validate rounds count.
        Integer roundsCount = ScannerHelper.tryParseInt(args[1]);
        if (roundsCount == null || roundsCount <= 0) {
            System.out.println("Invalid rounds count");
            System.exit(1);
            return;
        }

        // Read simulation data.
        SimulationData simulationData = null;
        try (Scanner scanner = new Scanner(new File(args[0]))) {
            ISimulationDataProvider simulationDataProvider = new SimulationDataProvider(scanner);
            simulationData = simulationDataProvider.getData();
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + args[1]);
            System.exit(1);
        } catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        System.out.println("Simulation data:");
        System.out.println(simulationData);

        // Seed investors
        IInvestorService investorService = new InvestorService(simulationData.getCompanies());
        seedInvestors(investorService, simulationData);

        System.out.println("Investors:");
        System.out.println(investorService);

        IStockMarketLogger logger = new ConsoleLogger(shouldLog);

        // Simulate
        ITradingSystem tradingSystem = new TradingSystem(logger, investorService, simulationData.getCompanies(), simulationData.getCompaniesStartingPrices(), roundsCount);
        runSimulation(tradingSystem);
    }

    private static void runSimulation(ITradingSystem tradingSystem) {
        System.out.println("Starting simulation");
        while (tradingSystem.hasNextRound()) {
            tradingSystem.nextRound();
        }

        System.out.println("Simulation finished");
        System.out.println();
        System.out.println("Investors:");
        System.out.println(tradingSystem);
    }

    private static void seedInvestors(IInvestorService investorService, SimulationData simulationData) {
        SmaCalculator smaCalculator = new SmaCalculator(simulationData.getCompanies().length);
        // Seed random investors
        for (int i = 0; i < simulationData.getRandomInvestorsCount(); i++) {
            investorService.registerInvestor(new RandomInvestor(new Random()));
        }

        // Seed SMA investors
        for (int i = 0; i < simulationData.getSmaInvestorCount(); i++) {
            investorService.registerInvestor(new SmaInvestor(smaCalculator, new Random()));
        }

        int totalInvestorsCount = simulationData.getRandomInvestorsCount() + simulationData.getSmaInvestorCount();
        for (int i = 0; i < totalInvestorsCount; i++) {
            investorService.addFunds(i, simulationData.getStartingFundAmount());
            for (int companyId = 0; companyId < simulationData.getCompanies().length; companyId++) {
                investorService.addStock(i, companyId, simulationData.getStartingAmounts()[companyId]);
            }
        }
    }
}
