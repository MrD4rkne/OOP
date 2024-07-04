package pl.edu.mimuw.ms459531.stockexchange.companies;

/**
 * StockCompany class represents a stock company.
 */
public record StockCompany(int id, String name) {
    public StockCompany(int id, String name) {
        if (id < 0) {
            throw new IllegalArgumentException("Id must be non-negative");
        }
        this.id = id;
        if (!name.matches("[a-zA-Z]{1,5}")) {
            throw new IllegalArgumentException("Name must be 1-5 characters long A-Z");
        }
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
