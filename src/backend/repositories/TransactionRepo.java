package backend.repositories;

import backend.database.IDatabase;
import backend.models.Transaction;

import java.util.List;

public class TransactionRepo implements ITransactionRepo {
    private final IDatabase db;

    public TransactionRepo(IDatabase db) {
        this.db = db;
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return db.getTransactions();
    }

    @Override
    public Transaction getById(long id) {
        return db.getTransactions().stream()
                .filter(tr -> tr.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Transaction transaction) {
        db.getTransactions().add(transaction);
    }
}
