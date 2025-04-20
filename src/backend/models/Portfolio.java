package backend.models;

import java.util.*;

public class Portfolio {
    private static int counter = 0;
    private final int id;
    private final int userId;
    private double balance;
    private final Map<Integer, PortfolioAsset> assets;

    public Portfolio(int userId, double balance) {
        this.id = counter++;
        this.userId = userId;
        this.balance = balance;
        this.assets = new HashMap<>();
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public void withdraw(double amount) {
        this.balance -= amount;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public double getBalance() {
        return balance;
    }

    public List<PortfolioAsset> getAssets() {
        Collection<PortfolioAsset> assetValues = assets.values();
        return new ArrayList<>(assetValues);
    }

    public boolean hasAsset(int assetId) {
        return assets.containsKey(assetId);
    }

    public PortfolioAsset getAsset(int assetId) {
        return assets.get(assetId);
    }

    public void addAsset(PortfolioAsset asset) {
        assets.put(asset.getId(), asset);
    }

    public void updateAsset(int assetId, double quantity) {
        PortfolioAsset target = assets.get(assetId);
        target.setQuantity(quantity);
    }
}
