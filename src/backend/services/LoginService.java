package backend.services;

import backend.models.User;
import backend.repositories.IUserRepo;
import exceptions.CustomExceptions;

public class LoginService {
    private final IUserRepo userRepo;

    public LoginService(IUserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void register(String username, String email,
                            String password, String confirmPassword)
            throws CustomExceptions.UserAlreadyExists,
            CustomExceptions.ConfirmPasswordMismatch,
            CustomExceptions.EmptyInput {

        User user = userRepo.getByUsername(username);
        if(user != null) {
            throw new CustomExceptions.UserAlreadyExists(
                    "User " + username + " already exists", username);
        }
        if(!password.equals(confirmPassword)) {
            throw new CustomExceptions.ConfirmPasswordMismatch("Incorrect confirm password");
        }
        if(username.equals("") || password.equals("")) {
            throw new CustomExceptions.EmptyInput("Username and password cannot be null");
        }
        User newUser = new User(username, password, email);
        userRepo.save(newUser);
        userRepo.setCurrentUser(newUser);
    }

    public void login(String username, String password)
            throws CustomExceptions.UserNotExists, CustomExceptions.WrongPassword {

        User user = userRepo.getByUsername(username);
        if(user == null) {
            throw new CustomExceptions.UserNotExists("This user does not exist");
        }
        if(!user.getPassword().equals(password)) {
            throw new CustomExceptions.WrongPassword("Wrong password");
        }
        userRepo.setCurrentUser(user);
    }
}
