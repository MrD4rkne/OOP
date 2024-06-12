package stockMarket.core;

public class StockCompany {
    private final int id;
    
    private final String name;
    
    public StockCompany(int id, String name){
        this.id = id;
        this.name = name;
    }
    
    public int getId(){
        return id;
    }
    
    public String getName(){
        return name;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
