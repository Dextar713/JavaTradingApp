package backend.models;

public class Stock extends Asset{
    private final String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public Stock(double price, String name, String companyName) {
        super(price, name);
        this.companyName = companyName;
    }
}
