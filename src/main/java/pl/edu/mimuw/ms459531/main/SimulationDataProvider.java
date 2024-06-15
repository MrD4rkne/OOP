package pl.edu.mimuw.ms459531.main;

import pl.edu.mimuw.ms459531.stockexchange.companies.StockCompany;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * SimulationDataProvider class implements ISimulationDataProvider interface and provides the simulation data.
 * It reads the data from the input and creates the SimulationData object.
 *
 * @see ISimulationDataProvider
 */
public class SimulationDataProvider implements ISimulationDataProvider {
    private final static String INVESTOR_RANDOM = "R";
    private final static String INVESTOR_SMA = "S";

    private final Scanner scanner;

    public SimulationDataProvider(Scanner scanner) {
        this.scanner = scanner;
    }

    public SimulationData getData()
            throws InvalidDataException {
        SimulationData simulationData = new SimulationData();
        scanInvestors(simulationData);
        scanCompanies(simulationData);
        scanStaringInfo(simulationData);
        return simulationData;
    }

    private void scanCompanies(SimulationData simulationData)
            throws InvalidDataException {
        String investorsLine = getNextLine();

        String[] companiesData = investorsLine.split(" ");
        if (companiesData.length == 0) {
            throw new InvalidDataException("No companies data");
        }

        List<String> companiesNames = new ArrayList<>();

        for (String companyData : companiesData) {
            String[] companyDataParts = companyData.split(":");
            if (companyDataParts.length != 2) {
                throw new InvalidDataException("Invalid company data: " + companyData);
            }

            Integer companyStartingPrice = ScannerHelper.tryParseInt(companyDataParts[1]);
            if (companyStartingPrice == null) {
                throw new InvalidDataException("Invalid company data: " + companyData);
            }
            if (companyStartingPrice < 0) {
                throw new InvalidDataException("Company starting price must be non-negative");
            }

            if (companiesNames.contains(companyDataParts[0])) {
                throw new InvalidDataException("Company name must be unique");
            }

            try {
                companiesNames.add(companyDataParts[0]);
                simulationData.addCompany(companyDataParts[0], companyStartingPrice);
            } catch (IllegalArgumentException e) {
                throw new InvalidDataException("Invalid company data: " + companyData);
            }
        }
    }

    private void scanInvestors(SimulationData simulationData)
            throws InvalidDataException {
        String investorsLine = getNextLine();
        String[] investorsData = investorsLine.split(" ");
        if (investorsData.length == 0) {
            throw new InvalidDataException("No investors data");
        }

        for (String investorData : investorsData) {
            switch (investorData) {
                case INVESTOR_RANDOM:
                    simulationData.increaseRandomInvestorsCount();
                    break;
                case INVESTOR_SMA:
                    simulationData.increaseSmaInvestorCount();
                    break;
                default:
                    throw new InvalidDataException("Invalid investor data: " + investorData);
            }
        }
    }

    private void scanStaringInfo(SimulationData simulationData)
            throws InvalidDataException {
        String globalInfo = getNextLine();
        String[] startingInfoData = globalInfo.split(" ");

        Integer startingFunds = ScannerHelper.tryParseInt(startingInfoData[0]);
        if (startingFunds == null) {
            throw new InvalidDataException("Invalid starting fund amount: " + startingInfoData[0]);
        }
        if (startingFunds <= 0) {
            throw new InvalidDataException("Starting fund amount must be a positive int value");
        }

        simulationData.setStartingFundAmount(startingFunds);

        StockCompany[] companies = simulationData.getCompanies();
        int[] startingAmounts = new int[companies.length];
        Arrays.fill(startingAmounts, -1);

        for (int i = 1; i < startingInfoData.length; i++) {
            String[] companyData = startingInfoData[i].split(":");
            if (companyData.length != 2) {
                throw new InvalidDataException("Invalid company data: " + startingInfoData[i]);
            }

            Integer startingStocksCount = ScannerHelper.tryParseInt(companyData[1]);
            if (startingStocksCount == null) {
                throw new InvalidDataException("Number of shares must be a positive integer " + startingInfoData[i]);
            }
            if (startingStocksCount < 0) {
                throw new InvalidDataException("Company stocks count must be a positive integer");
            }

            for (int j = 0; j < companies.length; j++) {
                if (companies[j].name().equals(companyData[0])) {
                    if (startingAmounts[j] != -1) {
                        throw new InvalidDataException("Company data must be unique");
                    }
                    startingAmounts[j] = startingStocksCount;
                    break;
                }
            }
        }

        simulationData.setStartingAmounts(startingAmounts);
    }

    private String getNextLine()
            throws InvalidDataException {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#")) {
                continue;
            }
            return line;
        }
        throw new InvalidDataException("No more lines in the input");
    }
}
