package backend.database;

import backend.models.*;

import java.util.ArrayList;

public class InMemoryDB implements IDatabase{
    private final ArrayList<User> users;
    private final ArrayList<Asset> assets;
    private final ArrayList<Transaction> transactions;
    private User currentUser;

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public InMemoryDB() {
        this.users = new ArrayList<>();
        this.assets = new ArrayList<>();
        this.transactions = new ArrayList<>();
        currentUser = null;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Asset> getAssets() {
        return assets;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public void updateAssetPrice(int assetId, double newPrice) {
        for(Asset asset: assets) {
            if(asset.getId() == assetId) {
                asset.setPricePerUnit(newPrice);
                break;
            }
        }
    }

    public void SeedData() {
        if (!this.users.isEmpty() || !this.assets.isEmpty() || !this.transactions.isEmpty()) {
            return; // Already seeded
        }

        // Create Assets
        CryptoCoin bitcoin = new CryptoCoin(30000, "BTC",
                "Satoshi Nakamoto", "Japan");
        CryptoCoin ethereum = new CryptoCoin(1800, "ETH",
                "Vitalik Buterin", "Canada");
        //Stock apple = new Stock(160, "AAPL", "Apple Inc.");

        assets.add(bitcoin);
        assets.add(ethereum);
        //assets.add(apple);

        // Create Users
        User alice = new User("alice123", "password", "alice@example.com");
        alice.setFirstName("Alice");
        alice.setLastName("Smith");

        User bob = new User("bob456", "secure123", "bob@example.com");
        bob.setFirstName("Bob");
        bob.setLastName("Johnson");

        users.add(alice);
        users.add(bob);

        alice.getPortfolio().deposit(100000);
        bob.getPortfolio().deposit(50000);

        Portfolio alicePortfolio = alice.getPortfolio();
        Portfolio bobPortfolio = bob.getPortfolio();

        PortfolioAsset aliceBTC = new PortfolioAsset(bitcoin, 1.0, alicePortfolio.getId());
        PortfolioAsset aliceETH = new PortfolioAsset(ethereum, 5.0, alicePortfolio.getId());

        //PortfolioAsset bobAAPL = new PortfolioAsset(apple, 50, bobPortfolio.getId());
        PortfolioAsset bobETH = new PortfolioAsset(ethereum, 2.0, bobPortfolio.getId());

        alicePortfolio.addAsset(bitcoin, 1.0);
        alicePortfolio.addAsset(ethereum, 5.0);

        //bobPortfolio.addAsset(bobAAPL);
        bobPortfolio.addAsset(ethereum, 2.0);
    }

    @Override
    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

}
