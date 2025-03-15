import java.io.*;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class FileTransactionStorage implements TransactionStorage {
private String getFileName(Month month, Year year) {
        return month.name() + "-" + year.getValue() + ".txt";
        }

public void saveTransaction(Transaction transaction, Month month, Year year) throws IOException {
        String fileName = getFileName(month, year);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
        bw.write(transaction.toString());
        bw.newLine();
        }
        }

public void saveAllTransactions(List<Transaction> transactions, Month month, Year year) throws IOException {
        String fileName = getFileName(month, year);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
        for (Transaction transaction : transactions) {
        bw.write(transaction.toString());
        bw.newLine();

        }
        }
}

public List<Transaction> loadAllTransactions(Month month, Year year) {
        List<Transaction> transactions = new ArrayList<>();
        String fileName = getFileName(month, year);
        File file = new File(fileName);
        if (!file.exists()) return transactions;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = br.readLine()) != null) {
        transactions.add(Transaction.fromString(line));
        }
        } catch (Exception e) {
        e.printStackTrace();
        }
        return transactions;
        }
        }