import java.io.IOException;
import java.util.Date;

public interface TransactionManagerInterface {
    Transaction addTransaction(double amount, String category, String subCategory, Date date, String description) throws IOException;
    void editTransaction(int transactionID, double newAmount, String newCategory, String newSubCategory, Date newDate, String newDescription) throws IOException, TransactionNotFoundException;
    void deleteTransaction(int transactionID) throws IOException, TransactionNotFoundException;
    String getTransactionsSummary();
    boolean transactionExists(int transactionID);
}

