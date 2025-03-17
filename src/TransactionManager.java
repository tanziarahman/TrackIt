import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.*;
public class TransactionManager implements TransactionManagerInterface{
private List<Transaction> transactions;
private int transactionCounter;
private final TransactionStorage storage;
private final BudgetManager budgetManager;
private static Month currentMonth;
private static Year currentYear;

private static final String RESET = "\u001B[0m";
private static final String BLUE = "\u001B[34m";
private static final String GREEN = "\u001B[32m";
private static final String RED = "\u001B[31m";
private static final String YELLOW = "\u001B[33m";
private static final String PURPLE = "\u001B[35m";
private static final String CYAN = "\u001B[36m";

public TransactionManager(TransactionStorage storage, BudgetManager budgetManager, Month month, Year year) throws IOException {
        this.storage = storage;
        this.budgetManager = budgetManager;
        this.currentMonth = month;
        this.currentYear = year;
        this.transactions = storage.loadAllTransactions(month, year);
        this.transactionCounter = transactions.size() + 1;
        }

public Transaction addTransaction(double amount, String category, String subCategory, Date date, String description) throws IOException, BudgetNotFoundException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        Month transactionMonth = Month.of(cal.get(Calendar.MONTH) + 1);
        Year transactionYear = Year.of(cal.get(Calendar.YEAR));

        if (!transactionMonth.equals(currentMonth) || !transactionYear.equals(currentYear)) {
        throw new IOException("Invalid transaction date. Transactions must be within " + currentMonth + " " + currentYear);
        }

        if (!budgetManager.budgetExists(category)) {
        throw new IOException("No budget exists for category: " + category);
        }

        double budgetAmount = budgetManager.getBudgetAmountForCategory(category);
        double totalAfterTransaction = totalTransactionInACategory(category) + amount;

        Transaction transaction = new Transaction(transactionCounter++, amount, category, subCategory, date, description);
        transactions.add(transaction);
        storage.saveTransaction(transaction, currentMonth, currentYear);

        if (totalAfterTransaction > budgetAmount) {
        System.out.println();
        System.out.println(YELLOW +"Warning: This transaction has exceeded the budget for category '" + category + "'.");
        System.out.println("Budget Limit: " + budgetAmount + ", Total After Transaction: " + totalAfterTransaction + RESET);
        }
        return transaction;
        }

public void editTransaction(int transactionID, double newAmount, String newCategory, String newSubCategory, Date newDate, String newDescription) throws IOException, BudgetNotFoundException {
        for (Transaction t : transactions) {
        if (t.getTransactionID() == transactionID) {
        if (!budgetManager.budgetExists(newCategory)) {
        throw new IOException("No budget exists for category: " + newCategory);
        }

        double budgetAmount = budgetManager.getBudgetAmountForCategory(newCategory);
        double totalAfterTransaction = (totalTransactionInACategory(newCategory) - t.getAmount()) + newAmount;

        t.setAmount(newAmount);
        t.setCategory(newCategory);
        t.setSubCategory(newSubCategory);
        t.setDate(newDate);
        t.setDescription(newDescription);
        storage.saveAllTransactions(transactions, currentMonth, currentYear);

        if (totalAfterTransaction > budgetAmount) {
        System.out.println();
        System.out.println(YELLOW + "Warning: This edited transaction has exceeded the budget for category '" + newCategory + "'.");
        System.out.println("Budget Limit: " + budgetAmount + ", Total After Transaction: " + totalAfterTransaction + RESET);
        }

        return;
        }
        }
        throw new IOException("Transaction with ID " + transactionID + " not found.");
        }

public void deleteTransaction(int transactionID) throws IOException {
        transactions.removeIf(t -> t.getTransactionID() == transactionID);
        for (int i = 0; i < transactions.size(); i++) transactions.get(i).setTransactionID(i + 1);
        transactionCounter = transactions.size() + 1;
        storage.saveAllTransactions(transactions, currentMonth, currentYear);
        }

public String getTransactionsSummary() {
        transactions.sort(Comparator.comparing(Transaction::getDate));
        for (int i = 0; i < transactions.size(); i++) {
                transactions.get(i).setTransactionID(i + 1);
        }
        StringBuilder summary = new StringBuilder();
        for (Transaction t : transactions) {
        summary.append("[").append(t.getTransactionID()).append("] Amount: ")
        .append(t.getAmount()).append(", Category: ").append(t.getCategory())
        .append(", Sub: ").append(t.getSubCategory())
        .append(", Date: ").append(new SimpleDateFormat("yyyy-MM-dd").format(t.getDate()))
        .append(", Desc: ").append(t.getDescription()).append("\n");
        }
        return summary.toString();
        }

@Override
public boolean transactionExists(int transactionID) {
        return transactions.stream().anyMatch(t -> t.getTransactionID() == transactionID);
        }

public double totalTransactionInACategory(String category) {
        return transactions.stream()
        .filter(t -> t.getCategory().equalsIgnoreCase(category))
        .mapToDouble(Transaction::getAmount)
        .sum();
        }
public List<Transaction> getTransactions(){
        return transactions;
        }

public static Month getCurrentMonth() {
        return currentMonth;
        }

public static Year getCurrentYear() {
        return currentYear;
        }
        }
