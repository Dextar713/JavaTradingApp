package backend.models;

import java.util.*;

public class Portfolio {
    private static int counter = 0;
    private final int id;
    private final int userId;
    private double balance;
    private final Map<Integer, PortfolioAsset> assets;

    public Portfolio(int id, int userId, double balance) {
        this.id = id;
        this.userId = userId;
        this.balance = balance;
        this.assets = new HashMap<>();
    }

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
        //return (List<PortfolioAsset>) assetValues;
        return new ArrayList<>(assetValues);
    }

    public boolean hasAsset(int assetId) {
        return assets.containsKey(assetId);
    }

    public PortfolioAsset getAsset(int assetId) {
        return assets.get(assetId);
    }

    public void addAsset(Asset asset, double quantity) {
        if(assets.containsKey(asset.getId())) {
            PortfolioAsset target = assets.get(asset.getId());
            target.setQuantity(target.getQuantity() + quantity);
        } else {
            PortfolioAsset newPortfolioAsset = new PortfolioAsset(asset, quantity, this.id);
            assets.put(asset.getId(), newPortfolioAsset);
        }
    }

    public void removeAsset(int assetId, double quantity) {
        if(assets.containsKey(assetId)) {
            PortfolioAsset target = assets.get(assetId);
            target.setQuantity(target.getQuantity() - quantity);
        }
    }
}
