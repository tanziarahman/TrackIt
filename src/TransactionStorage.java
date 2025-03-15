import java.io.IOException;
import java.time.Month;
import java.time.Year;
import java.util.List;

public interface TransactionStorage {

    void saveTransaction(Transaction transaction, Month month, Year year) throws IOException;

    void saveAllTransactions(List<Transaction> transactions, Month month, Year year) throws IOException;

    List<Transaction> loadAllTransactions(Month month, Year year);
}


