package backend.models;

import java.util.Objects;

public class PortfolioAsset extends Asset {
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

    public PortfolioAsset(Asset asset, double quantity, int portfolioId) {
        super(asset);
        this.quantity = quantity;
        this.portfolioId = portfolioId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortfolioAsset that = (PortfolioAsset) o;
        return portfolioId == that.portfolioId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), portfolioId);
    }
}
