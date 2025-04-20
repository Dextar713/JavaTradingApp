package backend.repositories;

import backend.models.User;

import java.util.List;

public interface IUserRepo {
    List<User> getAllUsers();
    User getById(int userId);
    User getByUsername(String username);
    void save(User user);
    User getCurrentUser();
    void setCurrentUser(User user);
}
