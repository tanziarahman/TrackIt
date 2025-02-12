import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.Month;
public class FileTransactionStorage implements TransactionStorage {
    private static final String FILE_EXTENSION = "Transactions.txt";
    private String getFileName(Month month) {
        return month.name().toLowerCase() + FILE_EXTENSION;
    }

    @Override
    public void saveTransaction(Transaction transaction, Month month) {
        String fileName = getFileName(month);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(transaction.toString());
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Error saving transaction to file: " + fileName);
            e.printStackTrace();
        }
    }

    @Override
    public void saveAllTransactions(List<Transaction> transactions, Month month) {
        String fileName = getFileName(month);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Transaction transaction : transactions) {
                bw.write(transaction.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving all transactions to file: " + fileName);
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> loadAllTransactions(Month month) {
        List<Transaction> transactions = new ArrayList<>();
        String fileName = getFileName(month);

        File file = new File(fileName);
        if (!file.exists()) {
            return transactions;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String transactionString;
            while ((transactionString = br.readLine()) != null) {
                transactionString = transactionString.trim();
                if (transactionString.isEmpty()) {
                    continue;
                }
                try {
                    Transaction transaction = Transaction.fromString(transactionString);
                    transactions.add(transaction);
                } catch (Exception e) {
                    System.err.println("Error parsing transaction: " + transactionString);
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + fileName);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error reading file: " + fileName);
            e.printStackTrace();
        }

        return transactions;
    }
}
