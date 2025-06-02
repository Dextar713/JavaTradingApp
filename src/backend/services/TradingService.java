package backend.services;

import backend.models.*;
import backend.observers.BalanceUpdater;
import backend.observers.IOrderListener;
import backend.observers.TransactionUpdater;
import backend.repositories.IAssetRepo;
import backend.repositories.IPortfolioRepo;
import backend.repositories.ITransactionRepo;
import backend.repositories.IUserRepo;
import exceptions.CustomExceptions;

import java.util.Set;

public class TradingService {
    private final IUserRepo userRepo;
    private final IAssetRepo assetRepo;
    private final IPortfolioRepo portfolioRepo;
    //private final ITransactionRepo transactionRepo;
    private final MatchingOrderBook orderBook;

    public TradingService(IUserRepo userRepo,
                          IAssetRepo assetRepo,
                          ITransactionRepo transactionRepo,
                          IPortfolioRepo portfolioRepo,
                          MatchingOrderBook orderBook) {
        this.userRepo = userRepo;
        this.assetRepo = assetRepo;
        this.portfolioRepo = portfolioRepo;
        this.orderBook = orderBook;
        this.orderBook.addListener(new BalanceUpdater(userRepo, assetRepo, portfolioRepo));
        this.orderBook.addListener(new TransactionUpdater(transactionRepo));
    }

    public void Buy(int userId, int assetId, double amount, double price)
            throws CustomExceptions.UserNotExists,
            CustomExceptions.AssetNotExists,
            CustomExceptions.NotEnoughBalance {

        User user = userRepo.getById(userId);
        if(user == null) {
            throw new CustomExceptions.UserNotExists("User not found");
        }
        Asset asset = assetRepo.getById(assetId);
        if(asset == null) {
            throw new CustomExceptions.AssetNotExists("Asset not found");
        }
        //double assetPrice = asset.getPricePerUnit();
        double totalValue = amount * asset.getPricePerUnit();
        Portfolio portfolio = user.getPortfolio();
        if (totalValue > portfolioRepo.getBalance(portfolio.getId())) {
            throw new CustomExceptions.NotEnoughBalance(
                    "You dont have enough balance to buy");
        }
        Order order = new Order(price, amount, true, assetId, userId);
        orderBook.HandleOrder(order);
    }

    public void Sell(int userId, int assetId, double amount, double price)
            throws CustomExceptions.UserNotExists, CustomExceptions.AssetNotExists,
            CustomExceptions.NotEnoughAssetQuantity {
        User user = userRepo.getById(userId);
        if(user == null) {
            throw new CustomExceptions.UserNotExists("User not found");
        }
        Portfolio portfolio = user.getPortfolio();
        if(!portfolioRepo.hasAsset(portfolio.getId(), assetId)) {
            throw new CustomExceptions.AssetNotExists("Asset not found in portfolio");
        }
        //PortfolioAsset asset = portfolio.getAsset(assetId);
        if (amount > portfolioRepo.getAssetQuantity(portfolio.getId(), assetId)) {
            throw new CustomExceptions.NotEnoughAssetQuantity(
                    "You dont have enough quantity of " + assetRepo.getById(assetId).getName());
        }
        Order order = new Order(price, amount, false, assetId, userId);
        orderBook.HandleOrder(order);
    }

}
