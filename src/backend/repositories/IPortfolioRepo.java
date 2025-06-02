package backend.repositories;

import backend.models.Asset;
import backend.models.Portfolio;
import backend.models.PortfolioAsset;

import java.util.List;

public interface IPortfolioRepo {
    Portfolio getByUserId(int userId);
    void updateBalance(int portfolioId, double newBalance);
    void addOrUpdateAsset(int portfolioId, int assetId, double quantity);
    void removeAssetQuantity(int portfolioId, int assetId, double quantity);
    List<PortfolioAsset> getAssets(int portfolioId);
    boolean hasAsset(int portfolioId, int assetId);
    double getAssetQuantity(int portfolioId, int assetId);
    public double getBalance(int portfolioId);
}