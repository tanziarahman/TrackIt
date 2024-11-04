import java.io.IOException;
import java.util.List;

public interface TransactionStorage {
    void saveTransaction(Transaction transaction) throws IOException;
    void saveAllTransactions(List<Transaction> transactions) throws IOException;
    List<Transaction> loadAllTransactions() throws IOException;
}


