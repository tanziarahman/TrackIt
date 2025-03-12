import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavingsManager {
    private Month month;
    private Year year;
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private ExpenseManager expenseManager;
    private List<Savings> savings;
    public SavingsManager(Month month, Year year, BudgetManager budgetManager, TransactionManager transactionManager,ExpenseManager expenseManager){
        this.month = month;
        this.year = year;
        this.budgetManager = budgetManager;
        this.transactionManager = transactionManager;
        this.expenseManager = expenseManager;
        this.savings = new ArrayList<>();
    }

    public Month getMonth(){
        return month;
    }

    /*private void calculateSavings(){
        for(Map.Entry<String,Budget> bg:budgetManager.getBudgets().entrySet()){
            double saving=bg.getValue().getAmount();
            for(Transaction tr:transactionManager.getTransactions()){
                if(tr.getCategory().equals(bg.getKey())){
                    saving = saving - tr.getAmount();
                }
            }
            Savings sav = new Savings(bg.getKey(),saving);
            savings.add(sav);
        }
    }*/


    public String showAllSavings() {
        StringBuilder st = new StringBuilder();
        double totalSaving = totalSavings();

        // Add table header once
        String separator = "-------------------+-------------+-------------+-------------";
        String headerFormat = "%-20s | %-12s | %-12s | %-12s%n";
        st.append(separator).append("\n");
        st.append(String.format(headerFormat, "Category", "Budget", "Expense", "Savings"));
        st.append(separator).append("\n");

        // Append savings for each category without adding extra separators
        int count = 0;
        int totalCategories = budgetManager.getBudgets().size();

        for (Map.Entry<String, Budget> bg : budgetManager.getBudgets().entrySet()) {
            count++;
            st.append(showSavingsForCategory(bg.getKey(), count == totalCategories)); // Pass true if it's the last category
        }

        // Append total row after the last category separator
        String budget = String.format("%.2f", budgetManager.getTotalBudget());
        String expense = String.format("%.2f", expenseManager.totalExpense());
        String saving = String.format("%.2f", totalSavings());
        String footerFormat = "%-20s | %-12s | %-12s | %-12s%n";
        st.append(String.format(footerFormat, "Total", budget, expense, saving));

        st.append(separator).append("\n"); // Final separator after the total row
        return st.toString();
    }

    // Modified method to control separator placement
    private String showSavingsForCategory(String category, boolean isLastCategory) {
        StringBuilder st = new StringBuilder();
        double bgt = 0, exp = 0;

        if (budgetManager.getBudgets().containsKey(category)) {
            Budget budgetObj = budgetManager.getBudgets().get(category);
            bgt = budgetObj.getAmount();
        }

        for (Transaction tr : transactionManager.getTransactions()) {
            if (tr.getCategory().equals(category)) {
                exp += tr.getAmount();
            }
        }

        double saving = bgt - exp;
        Savings sav = new Savings(category, saving);
        savings.add(sav);

        String rowFormat = "%-20s | %-12s | %-12s | %-12s%n";
        st.append(String.format(rowFormat, category, bgt, exp, saving));

        // Append separator only if it's the last category
        if (isLastCategory) {
            st.append("-------------------+-------------+-------------+-------------\n");
        }

        return st.toString();
    }

    public double totalSavings(){
        double totalExpense;
        double totalSavings = 0;
        for(Map.Entry<String,Budget> bg: budgetManager.getBudgets().entrySet()){
            totalExpense = 0;
            for(Transaction t:transactionManager.getTransactions()){
                if(bg.getKey().equals(t.getCategory())){
                    totalExpense+=t.getAmount();
                }
            }
            double saving = bg.getValue().getAmount()-totalExpense;
            totalSavings+=saving;
        }
        return totalSavings;
    }
}
