import backend.database.DBInitializer;
import backend.database.InMemoryDB;
import backend.models.MatchingOrderBook;
import backend.models.Transaction;
import backend.models.User;
import backend.repositories.*;
import backend.services.LoginService;
import backend.services.TradingService;
import frontend.TradingAppGUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        InMemoryDB db = new InMemoryDB();
        DBInitializer dbInitializer = new DBInitializer(db);
        dbInitializer.seed();
        IUserRepo userRepo = new UserRepo(db);
        IAssetRepo assetRepo = new AssetRepo(db);
        ITransactionRepo transactionRepo = new TransactionRepo(db);
        MatchingOrderBook orderBook = new MatchingOrderBook();
        TradingService tradingService = new TradingService(userRepo, assetRepo,
                transactionRepo, orderBook);
        LoginService loginService = new LoginService(userRepo);
        testScenario(tradingService, assetRepo, userRepo, db);

        SwingUtilities.invokeLater(() -> {
            TradingAppGUI gui = new TradingAppGUI(userRepo, assetRepo, db, loginService, tradingService);
            gui.setVisible(true);
        });
    }

    private static void testScenario(TradingService tradingService,
                                     IAssetRepo assetRepo, IUserRepo userRepo,
                                     InMemoryDB db) {
        try {
            int btcId = assetRepo.getByName("Bitcoin").getId();
            int aliceId = userRepo.getByUsername("alice123").getId();
            int bobId = userRepo.getByUsername("bob456").getId();
            System.out.println("== TEST: Alice sells 0.2 BTC at $31000 ==");

            tradingService.Sell(aliceId, btcId, 0.2, 31000);

            System.out.println("== TEST: Bob buys 0.2 BTC at $31000 ==");
            tradingService.Buy(bobId, btcId, 0.2, -1);

            User alice = userRepo.getById(aliceId);
            User bob = userRepo.getById(bobId);
            System.out.printf("Alice balance: %.2f\n", alice.getPortfolio().getBalance());
            System.out.printf("Bob BTC: %.4f\n", bob.getPortfolio().getAsset(btcId).getQuantity());
            for(Transaction transaction: db.getTransactions()) {
                System.out.println(transaction);
                System.out.println("------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
