package backend.repositories;

import backend.models.Asset;
import backend.models.CryptoCoin;
import backend.models.Portfolio;
import backend.models.PortfolioAsset;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLitePortfolioRepo implements IPortfolioRepo {
    private final String url = "jdbc:sqlite:trading_app.db";

    @Override
    public Portfolio getByUserId(int userId) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Portfolio WHERE userId = ?")) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Portfolio(userId, rs.getDouble("balance"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void updateBalance(int portfolioId, double newBalance) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement("UPDATE Portfolio SET balance = ? WHERE id = ?")) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, portfolioId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean hasAsset(int portfolioId, int assetId) {
        String sql = "SELECT 1 FROM PortfolioAsset WHERE portfolioId = ? AND assetId = ? LIMIT 1";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, portfolioId);
            stmt.setInt(2, assetId);

            ResultSet rs = stmt.executeQuery();
            return rs.next(); // true if any row is returned

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public double getAssetQuantity(int portfolioId, int assetId) {
        String sql = "SELECT * FROM PortfolioAsset WHERE portfolioId = ? AND assetId = ? LIMIT 1";
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, portfolioId);
            stmt.setInt(2, assetId);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getDouble("quantity");
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public double getBalance(int portfolioId) {
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement("Select balance from Portfolio WHERE id = ?")) {
            stmt.setInt(1, portfolioId);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                return rs.getDouble("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public void addOrUpdateAsset(int portfolioId, int assetId, double quantity) {
        try (Connection conn = DriverManager.getConnection(url)) {
            // Check if asset exists
            PreparedStatement check = conn.prepareStatement(
                    "SELECT quantity FROM PortfolioAsset WHERE portfolioId = ? AND assetId = ?");
            check.setInt(1, portfolioId);
            check.setInt(2, assetId);
            ResultSet rs = check.executeQuery();

            if (rs.next()) {
                double existing = rs.getDouble("quantity");
                PreparedStatement update = conn.prepareStatement(
                        "UPDATE PortfolioAsset SET quantity = ? WHERE portfolioId = ? AND assetId = ?");
                update.setDouble(1, existing + quantity);
                update.setInt(2, portfolioId);
                update.setInt(3, assetId);
                update.executeUpdate();
            } else {
                PreparedStatement insert = conn.prepareStatement(
                        "INSERT INTO PortfolioAsset (assetId, portfolioId, quantity) VALUES (?, ?, ?)");
                insert.setInt(1, assetId);
                insert.setInt(2, portfolioId);
                insert.setDouble(3, quantity);
                insert.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeAssetQuantity(int portfolioId, int assetId, double quantity) {
        try (Connection conn = DriverManager.getConnection(url)) {
            PreparedStatement update = conn.prepareStatement(
                    "UPDATE PortfolioAsset SET quantity = quantity - ? WHERE portfolioId = ? AND assetId = ?");
            update.setDouble(1, quantity);
            update.setInt(2, portfolioId);
            update.setInt(3, assetId);
            update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PortfolioAsset> getAssets(int portfolioId) {
        List<PortfolioAsset> assets = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url);
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT pa.assetId, pa.quantity, c.name, c.pricePerUnit, c.id FROM PortfolioAsset pa " +
                             "JOIN Asset c ON pa.assetId = c.id WHERE pa.portfolioId = ?")) {

            stmt.setInt(1, portfolioId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Asset asset = new CryptoCoin(rs.getInt("id"),
                        rs.getDouble("pricePerUnit"),
                        rs.getString("name"));
                PortfolioAsset portfolioAsset = new PortfolioAsset(
                        asset,
                        rs.getDouble("quantity"),
                        portfolioId
                );
                assets.add(portfolioAsset);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return assets;
    }
}
