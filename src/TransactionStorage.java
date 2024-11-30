import java.io.IOException;
import java.time.Month;
import java.util.List;

public interface TransactionStorage {

    void saveTransaction(Transaction transaction, Month month);

    void saveAllTransactions(List<Transaction> transactions, Month month);

    List<Transaction> loadAllTransactions(Month month);
}


