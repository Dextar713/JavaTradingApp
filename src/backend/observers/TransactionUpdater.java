package backend.observers;

import backend.models.Order;
import backend.models.Transaction;
import backend.repositories.ITransactionRepo;

public class TransactionUpdater implements IOrderListener{
    private final ITransactionRepo transactionRepo;

    public TransactionUpdater(ITransactionRepo transactionRepo) {
        this.transactionRepo = transactionRepo;
    }

    @Override
    public void onOrderFilled(Order order, double filledQuantity, double fillPrice) {
        Transaction transaction = new Transaction(order.getUserId(),
                order.getAssetId(), filledQuantity, fillPrice, order.isBuy());
        transaction.updateStatus(Transaction.Status.COMPLETED);
        transactionRepo.save(transaction);
    }
}
