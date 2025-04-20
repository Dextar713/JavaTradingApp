package backend.repositories;

import backend.database.IDatabase;
import backend.models.Asset;

import java.util.List;

public class AssetRepo implements IAssetRepo{
    private final IDatabase db;

    public AssetRepo(IDatabase db) {
        this.db = db;
    }

    @Override
    public List<Asset> getAllAssets() {
        return db.getAssets();
    }

    @Override
    public Asset getByName(String name) {
        return db.getAssets().stream()
                .filter(a -> a.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Asset getById(int id) {
        return db.getAssets().stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void add(Asset asset) {
        db.getAssets().add(asset);
    }
}
