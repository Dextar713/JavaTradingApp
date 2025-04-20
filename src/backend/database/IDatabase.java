package backend.database;

import backend.models.User;
import backend.models.Asset;
import backend.models.Transaction;

import java.util.List;

public interface IDatabase {
    List<User> getUsers();
    List<Asset> getAssets();
    List<Transaction> getTransactions();
    User getCurrentUser();
    void setCurrentUser(User user);
    void SeedData();
}
