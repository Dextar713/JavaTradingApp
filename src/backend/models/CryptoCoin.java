package backend.models;

public class CryptoCoin extends Asset {
    private final String creatorName;
    private final String creatorCountry;

    public String getCreatorName() {
        return creatorName;
    }

    public String getCreatorCountry() {
        return creatorCountry;
    }

    public CryptoCoin(int id, double price, String name) {
        super(id, price, name);
        this.creatorCountry = "Unknown";
        this.creatorName = "Unknown";
    }

    public CryptoCoin(double price, String name) {
        super(price, name);
        this.creatorCountry = "Unknown";
        this.creatorName = "Unknown";
    }

    public CryptoCoin(double price, String name, String creatorName, String creatorCountry) {
        super(price, name);
        this.creatorCountry = creatorCountry;
        this.creatorName = creatorName;
    }
}
