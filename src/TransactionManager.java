import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionManager implements TransactionManagerInterface {
    private List<Transaction> transactions;
    private int transactionCounter;
    private final TransactionStorage storage;

    public TransactionManager(TransactionStorage storage) {
        this.storage = storage;
        this.transactions = new ArrayList<>();
        try {
            this.transactions = storage.loadAllTransactions();
            this.transactionCounter = transactions.size() + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Transaction addTransaction(double amount, String category, String subCategory, Date date, String description) throws IOException {
        Transaction transaction = new Transaction(transactionCounter, amount, category, subCategory, date, description);
        transactions.add(transaction);
        transactionCounter++;
        storage.saveTransaction(transaction);
        return transaction;
    }

    @Override
    public void editTransaction(int transactionID, double newAmount, String newCategory, String newSubCategory, Date newDate, String newDescription) throws IOException, TransactionNotFoundException {
        Optional<Transaction> transactionOpt = transactions.stream()
                .filter(t -> t.getTransactionID() == transactionID)
                .findFirst();

        if (transactionOpt.isPresent()) {
            Transaction transaction = transactionOpt.get();
            transaction.setAmount(newAmount);
            transaction.setCategory(newCategory);
            transaction.setSubCategory(newSubCategory);
            transaction.setDate(newDate);
            transaction.setDescription(newDescription);
            storage.saveAllTransactions(transactions);
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transactionID + " not found.");
        }
    }


    @Override
    public void deleteTransaction(int transactionID) throws IOException, TransactionNotFoundException {
        boolean removed = transactions.removeIf(t -> t.getTransactionID() == transactionID);
        if (removed) {
            storage.saveAllTransactions(transactions);
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transactionID + " not found.");
        }
    }

    @Override
    public String getTransactionsSummary() {
        StringBuilder summary = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if (transactions.isEmpty()) {
            return "No transactions available.";
        }

        for (Transaction t : transactions) {
            summary.append("Transaction ID: ").append(t.getTransactionID())
                    .append(", Amount: ").append(t.getAmount())
                    .append(", Category: ").append(t.getCategory())
                    .append(", SubCategory: ").append(t.getSubCategory())
                    .append(", Date: ").append(sdf.format(t.getDate()))
                    .append(", Description: ").append(t.getDescription())
                    .append("\n");
        }
        return summary.toString().trim();
    }

    @Override
    public boolean transactionExists(int transactionID) {
        return transactions.stream().anyMatch(t -> t.getTransactionID() == transactionID);
    }

    public List<Transaction> getTransactions(){
        return transactions;
    }
}
