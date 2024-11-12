import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileTransactionStorage implements TransactionStorage {
    private static final String FILE_NAME = "transactions.txt";

    @Override
    public void saveTransaction(Transaction transaction) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(transaction.toString());
            bw.newLine();
        }
    }

    @Override
    public void saveAllTransactions(List<Transaction> transactions) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction transaction : transactions) {
                bw.write(transaction.toString());
                bw.newLine();
            }
        }
    }

    @Override
    public List<Transaction> loadAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        



        return transactions;
    }

}
