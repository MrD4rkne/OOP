package StockExchange.Investors;

public abstract class Investor {
    private int id;
    
    public Investor(int id) {
        this.id = id;
    }

    public int getId(){
        return id;
    }
}
