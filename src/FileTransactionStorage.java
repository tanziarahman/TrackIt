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
    public List<Transaction> loadAllTransactions() throws IOException {
        List<Transaction> transactions = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    transactions.add(Transaction.fromString(line));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return transactions;
    }
}
