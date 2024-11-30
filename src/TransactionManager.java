import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionManager implements TransactionManagerInterface {
    private List<Transaction> transactions;
    private int transactionCounter;
    private final TransactionStorage storage;
    private Month currentMonth;
    private final BudgetManager budgetManager;

    public TransactionManager(TransactionStorage storage, BudgetManager budgetManager, Month month) {
        this.storage = storage;
        this.budgetManager = budgetManager;
        this.currentMonth = month;
        try {
            this.transactions = storage.loadAllTransactions(month);
            this.transactionCounter = transactions.size() + 1;
        } catch (Exception e) {
            System.err.println("Error loading transactions for month " + month + ": " + e.getMessage());
            e.printStackTrace();
            this.transactions = new ArrayList<>();
            this.transactionCounter = 1;
        }
    }

    public void setMonth(Month month) {
        this.currentMonth = month;
        try {
            this.transactions = storage.loadAllTransactions(month);
            this.transactionCounter = transactions.size() + 1;
        } catch (Exception e) {
            System.err.println("Error switching to month " + month + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public Transaction addTransaction(double amount, String category, String subCategory, Date date, String description) throws IOException {
        if (!budgetManager.budgetExists(category)) {
            throw new IllegalStateException("No budget exists for category: " + category + " in " + currentMonth.name());
        }

        Transaction transaction = new Transaction(transactionCounter, amount, category, subCategory, date, description);
        transactions.add(transaction);
        transactionCounter++;

        try {
            storage.saveTransaction(transaction, currentMonth);
        } catch (Exception e) {
            System.err.println("Error saving transaction for month " + currentMonth + ": " + e.getMessage());
            throw e;
        }

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

            try {
                storage.saveAllTransactions(transactions, currentMonth);
            } catch (Exception e) {
                System.err.println("Error saving edited transactions for month " + currentMonth + ": " + e.getMessage());
                throw e;
            }
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transactionID + " not found.");
        }
    }

    @Override
    public void deleteTransaction(int transactionID) throws IOException, TransactionNotFoundException {
        boolean removed = transactions.removeIf(t -> t.getTransactionID() == transactionID);

        if (removed) {
            try {
                storage.saveAllTransactions(transactions, currentMonth);
            } catch (Exception e) {
                System.err.println("Error saving transactions after deletion for month " + currentMonth + ": " + e.getMessage());
                throw e;
            }
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transactionID + " not found.");
        }
    }

    @Override
    public String getTransactionsSummary() {
        StringBuilder summary = new StringBuilder();

        if (transactions.isEmpty()) {
            return "No transactions available.";
        }

        try {
            for (Transaction t : transactions) {
                summary.append("Transaction ID: ").append(t.getTransactionID())
                        .append(", Amount: ").append(t.getAmount())
                        .append(", Category: ").append(t.getCategory())
                        .append(", SubCategory: ").append(t.getSubCategory())
                        .append(", Date: ").append(new SimpleDateFormat("yyyy-MM-dd").format(t.getDate()))
                        .append(", Description: ").append(t.getDescription())
                        .append("\n");
            }
        } catch (Exception e) {
            System.err.println("Error generating transactions summary: " + e.getMessage());
            e.printStackTrace();
        }

        return summary.toString().trim();
    }

    @Override
    public boolean transactionExists(int transactionID) {
        try {
            return transactions.stream().anyMatch(t -> t.getTransactionID() == transactionID);
        } catch (Exception e) {
            System.err.println("Error checking transaction existence: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
