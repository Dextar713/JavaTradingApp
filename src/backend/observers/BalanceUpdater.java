package backend.observers;

import backend.models.*;
import backend.repositories.IAssetRepo;
import backend.repositories.IPortfolioRepo;
import backend.repositories.IUserRepo;

public class BalanceUpdater implements IOrderListener{
    private final IUserRepo userRepo;
    private final IAssetRepo assetRepo;
    private final IPortfolioRepo portfolioRepo;

    public BalanceUpdater(IUserRepo userRepo,
                          IAssetRepo assetRepo,
                          IPortfolioRepo portfolioRepo) {
        this.userRepo = userRepo;
        this.assetRepo = assetRepo;
        this.portfolioRepo = portfolioRepo;
    }

    @Override
    public void onOrderFilled(Order order, double filledQuantity, double fillPrice) {
        double totalValue = filledQuantity * fillPrice;
        try {
            User user = userRepo.getById(order.getUserId());
            Portfolio portfolio = user.getPortfolio();
            if (order.isBuy()) {
                Asset orderAsset = assetRepo.getById(order.getAssetId());
                //portfolio.addAsset(orderAsset, filledQuantity);
                //portfolio.withdraw(totalValue);
                portfolioRepo.addOrUpdateAsset(portfolio.getId(), orderAsset.getId(), filledQuantity);
                portfolioRepo.updateBalance(portfolio.getId(), portfolio.getBalance() - totalValue);
            } else {
                // Sell order
                //portfolio.removeAsset(order.getAssetId(), filledQuantity);
                portfolioRepo.removeAssetQuantity(portfolio.getId(), order.getAssetId(), filledQuantity);
                portfolioRepo.updateBalance(portfolio.getId(), portfolio.getBalance()+totalValue);
                //portfolio.deposit(totalValue);
            }

            System.out.printf("User %d: Order ID %d filled %.4f units at price %.2f%n",
                    order.getUserId(), order.getId(), filledQuantity, fillPrice);

        } catch (Exception e) {
            System.err.println("Error during order update: " + e.getMessage());
        }
    }
}
