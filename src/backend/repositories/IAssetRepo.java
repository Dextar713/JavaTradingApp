package backend.repositories;

import backend.models.Asset;

import java.util.List;

public interface IAssetRepo {
    List<Asset> getAllAssets();
    Asset getByName(String name);
    Asset getById(int id);
    void add(Asset asset);
}
