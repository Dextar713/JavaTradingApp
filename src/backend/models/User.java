package backend.models;

import java.util.*;

public class User {
    private static int counter = 0;
    private final int id;
    private final Portfolio portfolio;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private final Date joinDate = new Date();
    private final Set<Transaction> transactions;

    public int getId() {
        return id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    private String email;

    public User(String userName, String password, String email) {
        this.id = counter++;
        this.userName = userName;
        this.email = email;
        this.password = new String(password);
        this.portfolio = new Portfolio(this.id, 0);
        this.transactions = new HashSet<>();
    }
}
