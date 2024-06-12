package main;

import stockMarket.core.ITradingSystem;
import stockMarket.core.TradingSystem;
import stockMarket.investors.IInvestorService;
import stockMarket.investors.InvestorService;
import stockMarket.investors.RandomInvestor;
import stockMarket.investors.SmaInvestor;

import javax.xml.catalog.Catalog;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if(args.length != 2){
            System.out.println("Usage: <roundsCount> <inputFile>");
            System.exit(1);
            return;
        }
        
        try(Scanner scanner = new Scanner(new File(args[1]))) {
            ISimulationBuilder simulationBuilder = new SimulationBuilder(scanner);
            Integer roundsCount = ScannerHelper.tryParseInt(args[0]);
            if(roundsCount == null || roundsCount <= 0){
                System.out.println("Invalid rounds count");
                System.exit(1);
                return;
            }
            SimulationData simulationData = simulationBuilder.buildSimulation();
            
            System.out.println("Simulation data:");
            System.out.println(simulationData);
            
            IInvestorService investorService = new InvestorService(simulationData.getCompanies());
            seedInvestors(investorService, simulationData);
            ITradingSystem tradingSystem = new TradingSystem(new ConsoleLogger(), investorService, simulationData.getCompanies(),simulationData.getCompaniesStartingPrices());
            
            for(int i = 0; i < roundsCount; i++){
                tradingSystem.nextRound();
            }
            
        }
        catch(FileNotFoundException e){
            System.out.println("File not found: " + args[1]);
            System.exit(1);
        }
        catch (InvalidDataException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }
    
    private static void seedInvestors(IInvestorService investorService, SimulationData simulationData){
        for(int i = 0; i < simulationData.getRandomInvestorsCount(); i++){
            investorService.registerInvestor(new RandomInvestor());
        }
        
        for(int i = 0; i < simulationData.getSmaInvestorCount(); i++){
            investorService.registerInvestor(new SmaInvestor());
        }
        int totalInvestorsCount = simulationData.getRandomInvestorsCount() + simulationData.getSmaInvestorCount();
        for(int i = 0; i< totalInvestorsCount; i++){
            investorService.addFunds(i, simulationData.getStartingFundAmount());
            for(int companyId = 0; companyId < simulationData.getCompanies().length; companyId++){
                investorService.addStock(i, companyId, simulationData.getStartingAmounts()[companyId]);
            }
        }
    }
}
