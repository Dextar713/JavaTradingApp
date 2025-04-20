package backend.models;

import java.util.Objects;

public abstract class Asset implements Comparable<Asset>{
    private static int counter = 0;
    private final int id;
    private double pricePerUnit;
    private String name;
    private double marketCap;

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public void setDescription(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public double getPricePerUnit() {
        return pricePerUnit;
    }

    public String getName() {
        return name;
    }

    public Asset(double price, String name) {
        this.id = counter++;
        this.pricePerUnit = price;
        this.name = name;
    }

    public Asset(Asset asset) {
        this.id = asset.id;
        this.pricePerUnit = asset.pricePerUnit;
        this.name = asset.name;
        this.marketCap = asset.marketCap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Asset asset = (Asset) o;
        return id == asset.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(Asset asset) {
        return Double.compare(this.pricePerUnit, asset.pricePerUnit);
    }
}
