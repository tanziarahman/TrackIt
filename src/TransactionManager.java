import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class TransactionManager implements TransactionManagerInterface {
    private List<Transaction> transactions;
    private int transactionCounter;
    private final TransactionStorage storage;
    private Month currentMonth;
    private Year currentYear;
    private final BudgetManager budgetManager;

    public TransactionManager(TransactionStorage storage, BudgetManager budgetManager, Month month, Year year) {
        this.storage = storage;
        this.budgetManager = budgetManager;
        this.currentMonth = month;
        this.currentYear = year;
        try {
            this.transactions = storage.loadAllTransactions(month, year);
            this.transactionCounter = transactions.size() + 1;
        } catch (Exception e) {
            this.transactions = new ArrayList<>();
            this.transactionCounter = 1;
        }
    }

    public void setMonthAndYear(Month month, Year year) {
        this.currentMonth = month;
        this.currentYear = year;
        try {
            this.transactions = storage.loadAllTransactions(month, year);
            this.transactionCounter = transactions.size() + 1;
        } catch (Exception e) {
            this.transactions = new ArrayList<>();
            this.transactionCounter = 1;
        }
    }

    @Override
    public Transaction addTransaction(double amount, String category, String subCategory, Date date, String description) throws IOException {
        if (!budgetManager.budgetExists(category)) {
            throw new IllegalStateException("No budget exists for category: " + category + " in " + currentMonth.name() + " " + currentYear.getValue());
        }

        double totalAfterTransaction = totalTransactionInACategory(category) + amount;
        double budgetForCategory;

        try {
            budgetForCategory = budgetManager.getBudgetAmountForCategory(category);
        } catch (Exception e) {
            throw new IllegalStateException("Error retrieving budget for category: " + category);
        }

        if (totalAfterTransaction > budgetForCategory) {
            throw new IllegalStateException("Adding this transaction will exceed the budget for category '" + category +
                    "'. Current Budget: " + budgetForCategory + ", Total After Transaction: " + totalAfterTransaction);
        }

        Transaction transaction = new Transaction(transactionCounter, amount, category, subCategory, date, description);
        transactions.add(transaction);
        transactionCounter++;

        storage.saveTransaction(transaction, currentMonth, currentYear);
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

            storage.saveAllTransactions(transactions, currentMonth, currentYear);
        } else {
            throw new TransactionNotFoundException("Transaction with ID " + transactionID + " not found.");
        }
    }

    @Override
    public void deleteTransaction(int transactionID) throws IOException, TransactionNotFoundException {
        boolean removed = transactions.removeIf(t -> t.getTransactionID() == transactionID);

        if (removed) {
            storage.saveAllTransactions(transactions, currentMonth, currentYear);
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

        for (Transaction t : transactions) {
            summary.append("Transaction ID: ").append(t.getTransactionID())
                    .append(", Amount: ").append(t.getAmount())
                    .append(", Category: ").append(t.getCategory())
                    .append(", SubCategory: ").append(t.getSubCategory())
                    .append(", Date: ").append(new SimpleDateFormat("yyyy-MM-dd").format(t.getDate()))
                    .append(", Description: ").append(t.getDescription())
                    .append("\n");
        }

        return summary.toString().trim();
    }

    @Override
    public boolean transactionExists(int transactionID) {
        return transactions.stream().anyMatch(t -> t.getTransactionID() == transactionID);
    }

    public double totalTransactionInACategory(String category) {
        return transactions.stream()
                .filter(tr -> tr.getCategory().equalsIgnoreCase(category))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}
