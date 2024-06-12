package main;

import stockMarket.core.StockCompany;

import java.util.ArrayList;
import java.util.List;

public class SimulationData{
    private final List<StockCompany> companies;
    
    private final List<Integer> companiesStartingPrices;
    
    private int[] startingAmounts;
    
    private int randomInvestorsCount;
    
    private int smaInvestorCount;
    
    private int startingFundAmount;
    
    public SimulationData(){
        this.companies = new ArrayList<>();
        this.companiesStartingPrices = new ArrayList<>();
        this.randomInvestorsCount = 0;
        this.smaInvestorCount = 0;
        this.startingAmounts = new int[0];
        this.startingFundAmount = 0;
    }
    
    public void addCompany(String name, int startingPrice){
        companies.add(new StockCompany(companies.size(), name));
        companiesStartingPrices.add(startingPrice);
    }
    
    public StockCompany[] getCompanies(){
        return companies.toArray(StockCompany[]::new);
    }
    
    public int[] getCompaniesStartingPrices(){
        return companiesStartingPrices.stream().mapToInt(i->i).toArray();
    }
    
    public void increaseRandomInvestorsCount(){
        randomInvestorsCount++;
    }
    
    public void increaseSmaInvestorCount(){
        smaInvestorCount++;
    }
    
    public int getRandomInvestorsCount(){
        return randomInvestorsCount;
    }
    
    public int getSmaInvestorCount(){
        return smaInvestorCount;
    }


    public void setStartingAmounts(int[] startingAmounts) {
        this.startingAmounts = startingAmounts;
    }
    
    public int[] getStartingAmounts(){
        return startingAmounts;
    }

    public void setStartingFundAmount(Integer startingFunds) {
        this.startingFundAmount = startingFunds;
    }
    
    public int getStartingFundAmount(){
        return startingFundAmount;
    }
}
