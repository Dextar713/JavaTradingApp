package backend.database;

import backend.models.*;
import backend.services.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabase implements IDatabase {
    private final String url = "jdbc:sqlite:trading_app.db";
    private User currentUser;

    public SQLiteDatabase() {
        createTables();
    }

    private void createTables() {
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
             stmt.execute("""
                CREATE TABLE IF NOT EXISTS User (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    email TEXT,
                    firstName TEXT,
                    lastName TEXT,
                    joinDate TEXT
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Asset (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    pricePerUnit REAL NOT NULL,
                    marketCap REAL,
                    type TEXT NOT NULL
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Portfolio (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER NOT NULL,
                    balance REAL DEFAULT 0,
                    FOREIGN KEY(userId) REFERENCES User(id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS PortfolioAsset (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    assetId INTEGER NOT NULL,
                    portfolioId INTEGER NOT NULL,
                    quantity REAL NOT NULL,
                    FOREIGN KEY(assetId) REFERENCES Asset(id),
                    FOREIGN KEY(portfolioId) REFERENCES Portfolio(id)
                );
            """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS UserTransaction (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    userId INTEGER NOT NULL,
                    assetId INTEGER NOT NULL,
                    amount REAL NOT NULL,
                    pricePerUnit REAL NOT NULL,
                    status TEXT,
                    time TEXT,
                    isBuy INTEGER,
                    FOREIGN KEY(userId) REFERENCES User(id),
                    FOREIGN KEY(assetId) REFERENCES Asset(id)
                );
            """);



        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM User");
             ResultSet rs = stmt.executeQuery();
            AuditService.getInstance().log("Read", "User");
            while (rs.next()) {
                PreparedStatement stmt_portfolio = conn.prepareStatement(
                        "SELECT * FROM Portfolio where userId = ?");
                stmt_portfolio.setInt(1, rs.getInt("id"));
                ResultSet rs_portfolio = stmt_portfolio.executeQuery();
                AuditService.getInstance().log("Read", "Portfolio");
                Portfolio portfolio = new Portfolio(rs_portfolio.getInt("id"),
                        rs.getInt("id"),
                        rs_portfolio.getDouble("balance"));
                User user = new User(rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("email"),
                        portfolio);
                user.setFirstName(rs.getString("firstName"));
                user.setLastName(rs.getString("lastName"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public List<Asset> getAssets() {
        List<Asset> assets = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Asset");
             ResultSet rs = stmt.executeQuery()) {
            AuditService.getInstance().log("Read", "Asset");
            while (rs.next()) {
                String type = rs.getString("type");
                Asset asset;
                if ("crypto".equals(type)) {
                    asset = new CryptoCoin(rs.getInt("id"),
                            rs.getDouble("pricePerUnit"),
                            rs.getString("name"));
                } else {
                    asset = new Stock(rs.getDouble("pricePerUnit"),
                            rs.getString("name"));
                }
                assets.add(asset);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return assets;
    }

    @Override
    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM UserTransaction");
            ResultSet rs = stmt.executeQuery();
            AuditService.getInstance().log("Read", "UserTransaction");
            while(rs.next()) {
                Transaction transaction = new Transaction(rs.getInt("id"),
                        rs.getInt("userId"),
                        rs.getInt("assetId"),
                        rs.getDouble("amount"),
                        rs.getDouble("pricePerUnit"),
                        rs.getBoolean("isBuy"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement stmt = conn.prepareStatement(
                    "insert into UserTransaction (id, userId, assetId, amount, pricePerUnit, time, isBuy) " +
                            "values (?, ?, ?, ?, ?, ?, ?)");
            stmt.setLong(1, transaction.getId());
            stmt.setInt(2, transaction.getUserId());
            stmt.setInt(3, transaction.getAssetId());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setDouble(5, transaction.getPricePerUnit());
            stmt.setString(6, transaction.getTime().toString());
            stmt.setBoolean(7, transaction.isBuy());
            stmt.executeUpdate();

            AuditService.getInstance().log("Create", "UserTransaction");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateAssetPrice(int assetId, double newPrice) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement stmt = conn.prepareStatement("UPDATE Asset set pricePerUnit = ? " +
                    "where id = ?");
            stmt.setDouble(1, newPrice);
            stmt.setInt(2, assetId);
            stmt.executeUpdate();

            AuditService.getInstance().log("Update", "Asset");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    @Override
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    @Override
    public void SeedData() {
        if (!getUsers().isEmpty()) return;

        try (Connection conn = DriverManager.getConnection(url)) {
             PreparedStatement insertUser = conn.prepareStatement(
                     "INSERT INTO User (username, password, email, firstName, lastName, joinDate) VALUES (?, ?, ?, ?, ?, ?)");
             insertUser.setString(1, "alice123");
             insertUser.setString(2, "password");
             insertUser.setString(3, "alice@example.com");
             insertUser.setString(4, "Alice");
             insertUser.setString(5, "Smith");
             insertUser.setString(6, new java.util.Date().toString());
             insertUser.executeUpdate();

             insertUser.setString(1, "bob456");
             insertUser.setString(2, "secure123");
             insertUser.setString(3, "bob@example.com");
             insertUser.setString(4, "Bob");
             insertUser.setString(5, "Johnson");
             insertUser.setString(6, new java.util.Date().toString());
             insertUser.executeUpdate();

            PreparedStatement insertCoin = conn.prepareStatement(
                    "INSERT INTO Asset (name, pricePerUnit, marketCap, type) VALUES (?, ?, ?, ?)");
            insertCoin.setString(1, "BTC");
            insertCoin.setDouble(2, 30000);
            insertCoin.setDouble(3, 600_000_000_000.0);
            insertCoin.setString(4, "crypto");
            insertCoin.executeUpdate();

            insertCoin.setString(1, "ETH");
            insertCoin.setDouble(2, 1800);
            insertCoin.setDouble(3, 220_000_000_000.0);
            insertCoin.setString(4, "crypto");
            insertCoin.executeUpdate();

            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT id FROM User WHERE username='alice123'");
            int aliceId = rs.next() ? rs.getInt("id") : -1;
            rs = stmt.executeQuery("SELECT id FROM User WHERE username='bob456'");
            int bobId = rs.next() ? rs.getInt("id") : -1;
            rs = stmt.executeQuery("SELECT id FROM Asset WHERE name='BTC'");
            int btcId = rs.next() ? rs.getInt("id") : -1;
            rs = stmt.executeQuery("SELECT id FROM Asset WHERE name='ETH'");
            int ethId = rs.next() ? rs.getInt("id") : -1;

            PreparedStatement insertPortfolio = conn.prepareStatement(
                    "INSERT INTO Portfolio (userId, balance) VALUES (?, ?)");
            insertPortfolio.setInt(1, aliceId);
            insertPortfolio.setDouble(2, 100000);
            insertPortfolio.executeUpdate();

            insertPortfolio.setInt(1, bobId);
            insertPortfolio.setDouble(2, 50000);
            insertPortfolio.executeUpdate();

            rs = stmt.executeQuery("SELECT id FROM Portfolio WHERE userId = " + aliceId);
            int alicePortfolioId = rs.next() ? rs.getInt("id") : -1;
            rs = stmt.executeQuery("SELECT id FROM Portfolio WHERE userId = " + bobId);
            int bobPortfolioId = rs.next() ? rs.getInt("id") : -1;

            PreparedStatement insertPA = conn.prepareStatement(
                    "INSERT INTO PortfolioAsset (assetId, portfolioId, quantity) VALUES (?, ?, ?)");
            insertPA.setInt(1, btcId);
            insertPA.setInt(2, alicePortfolioId);
            insertPA.setDouble(3, 1.0);
            insertPA.executeUpdate();

            insertPA.setInt(1, ethId);
            insertPA.setInt(2, alicePortfolioId);
            insertPA.setDouble(3, 5.0);
            insertPA.executeUpdate();

            insertPA.setInt(1, ethId);
            insertPA.setInt(2, bobPortfolioId);
            insertPA.setDouble(3, 2.0);
            insertPA.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
