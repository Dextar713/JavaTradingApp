package backend.observers;

import backend.models.*;
import backend.repositories.IAssetRepo;
import backend.repositories.IUserRepo;

public class BalanceUpdater implements IOrderListener{
    private final IUserRepo userRepo;
    private final IAssetRepo assetRepo;

    public BalanceUpdater(IUserRepo userRepo,
                          IAssetRepo assetRepo) {
        this.userRepo = userRepo;
        this.assetRepo = assetRepo;
    }

    @Override
    public void onOrderFilled(Order order, double filledQuantity, double fillPrice) {
        double totalValue = filledQuantity * fillPrice;
        try {
            User user = userRepo.getById(order.getUserId());
            Portfolio portfolio = user.getPortfolio();
            if (order.isBuy()) {
                if (portfolio.hasAsset(order.getAssetId())) {
                    double prev = portfolio.getAsset(order.getAssetId()).getQuantity();
                    portfolio.updateAsset(order.getAssetId(), prev + filledQuantity);
                } else {
                    Asset asset = assetRepo.getById(order.getAssetId());
                    PortfolioAsset newAsset = new PortfolioAsset(
                            asset, filledQuantity, portfolio.getId());
                    portfolio.addAsset(newAsset);
                }
                portfolio.withdraw(totalValue);
            } else {
                // Sell order
                double prevQuantity = portfolio.getAsset(order.getAssetId()).getQuantity();
                portfolio.updateAsset(order.getAssetId(), prevQuantity - filledQuantity);
                portfolio.deposit(totalValue);
            }

            System.out.printf("User %d: Order ID %d filled %.4f units at price %.2f%n",
                    order.getUserId(), order.getId(), filledQuantity, fillPrice);

        } catch (Exception e) {
            System.err.println("Error during order update: " + e.getMessage());
        }
    }
}
