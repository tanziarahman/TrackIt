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
        try (BufferedReader reader = new BufferedReader(new FileReader("transactions.txt"))) {
            String transactionString;
            while ((transactionString = reader.readLine()) != null) {
                transactionString = transactionString.trim();
                if (transactionString.isEmpty()) {
                    continue;
                }
                System.out.println("Loading transaction: " + transactionString);
                try {
                    Transaction transaction = Transaction.fromString(transactionString);
                    transactions.add(transaction);
                } catch (NumberFormatException  e) {
                    System.err.println("Invalid transaction format: " + transactionString);
                    e.printStackTrace();
                } catch (Exception e) {
                    System.err.println("Error parsing transaction: " + transactionString);
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: transactions.txt");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: transactions.txt");
            e.printStackTrace();
        }
        return transactions;
    }
}
