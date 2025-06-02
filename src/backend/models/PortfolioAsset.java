package backend.models;

import java.util.Objects;

public class PortfolioAsset {
    private final Asset asset;
    private final int portfolioId;
    private double quantity;

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getPortfolioId() {
        return portfolioId;
    }

    public Asset getAsset() {
        return asset;
    }

    public PortfolioAsset(Asset asset, double quantity, int portfolioId) {
        this.asset = asset;
        this.quantity = quantity;
        this.portfolioId = portfolioId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PortfolioAsset portfolioAsset = (PortfolioAsset) o;
        return this.portfolioId == portfolioAsset.portfolioId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(portfolioId);
    }
}
