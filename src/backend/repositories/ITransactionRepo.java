package backend.repositories;

import backend.models.Transaction;

import java.util.List;

public interface ITransactionRepo {
    List<Transaction> getAllTransactions();
    Transaction getById(long id);
    void save(Transaction transaction);
}
