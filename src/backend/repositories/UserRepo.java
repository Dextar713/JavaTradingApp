package backend.repositories;

import backend.database.IDatabase;
import backend.models.User;

import java.util.List;

public class UserRepo implements IUserRepo {
    private final IDatabase db;

    public UserRepo(IDatabase db) {
        this.db = db;
    }

    @Override
    public List<User> getAllUsers() {
        return db.getUsers();
    }

    @Override
    public User getById(int userId) {
        return db.getUsers().stream()
                .filter(u -> u.getId() == userId)
                .findFirst()
                .orElse(null);
    }

    @Override
    public User getByUsername(String username) {
        return db.getUsers().stream()
                .filter(u -> u.getUserName().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(User user) {
        db.getUsers().add(user);
    }

    @Override
    public User getCurrentUser() {
        return db.getCurrentUser();
    }

    @Override
    public void setCurrentUser(User user) {
        db.setCurrentUser(user);
    }
}
