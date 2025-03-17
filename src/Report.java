import java.util.List;
import java.util.Map;

public class Report {
    private BudgetManager budgetManager;
    private ExpenseManager expenseManager;
    private SavingsManager savingsManager;
    private List<Expense> expenses;
    private List<Savings> savings;
    private Map<String,Budget> budgets;

    private static final String RESET = "\u001B[0m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";


    public Report(ExpenseManager expenseManager, SavingsManager savingsManager, BudgetManager budgetManager){
        this.budgetManager = budgetManager;
        this.expenseManager = expenseManager;
        this.savingsManager = savingsManager;
    }

    private Map<String,Budget> getBudgets(){
        return budgetManager.getBudgets();
    }
    private List<Expense> getExpenses(){
        return expenseManager.getExpenses();
    }
    private List<Savings> getSavings(){
        return savingsManager.getSavings();
    }
    public String showReport(){
        StringBuilder st = new StringBuilder();
        budgets = getBudgets();
        expenses = getExpenses();
        savings = getSavings();

        // Add table header once
        String separator = "-------------------+-------------+-------------+-------------";
        String headerFormat = "%-20s | %-12s | %-12s | %-12s%n";
        st.append(separator).append("\n");
        st.append(PURPLE);
        st.append(String.format(headerFormat,"Category", "Budget", "Expense", "Savings"));
        st.append(RESET);
        st.append(separator).append("\n");

        for(Map.Entry<String,Budget> bg:budgets.entrySet()){
            String category = StringFormatter.capitalizeFirstLetter(bg.getKey());
            double budget = bg.getValue().getAmount();
            double expense = 0;
            double saving = 0;

            for(Expense exp:expenses){
                if(exp.getCategory().equals(category)){
                    expense = exp.getAmount();
                }
            }

            for(Savings sav:savings){
                if(sav.getCategory().equals(category)){
                    saving = sav.getAmount();
                }
            }
            String bgt = String.format("%.2f", budget);
            String exp = String.format("%.2f", expense);
            String sav = String.format("%.2f", saving);
            String footerFormat = "%-20s | %-12s | %-12s | %-12s%n";

            String rowFormat = "%-20s | %-12s | %-12s | %-12s%n";
            st.append(String.format(rowFormat, category, bgt, exp, sav));
        }
        st.append("-------------------+-------------+-------------+-------------\n");
        String totalBudget = String.format("%.2f", budgetManager.getTotalBudget());
        String totalExpense = String.format("%.2f", expenseManager.totalExpense());
        String totalSaving = String.format("%.2f", savingsManager.getTotalSavings());
        String footerFormat = "%-20s | %-12s | %-12s | %-12s%n";
        st.append(CYAN);
        st.append(String.format(footerFormat, "Total", totalBudget, totalExpense, totalSaving));
        st.append(RESET);
        st.append(separator).append("\n"); // Final separator after the total row
        st.append(grandSaving()+"\n");
        return st.toString();
    }

    private String grandSaving(){
        StringBuilder stb = new StringBuilder();
        double income = budgetManager.getMonthlyIncome();
        if(income>0){
            double grandTotalSaving = income - expenseManager.totalExpense();
            stb.append("Total Income: "+budgetManager.getMonthlyIncome()+"\n");
            stb.append("Total Budget: "+budgetManager.getTotalBudget()+"\n");
            stb.append("Total Expense: "+expenseManager.totalExpense()+"\n");
            stb.append("Grand Saving: "+((budgetManager.getMonthlyIncome())-(budgetManager.getTotalBudget()-expenseManager.totalExpense()))+"\n");
        }
        else{
            stb.append("**Set monthly income to get grand saving**"+"\n");
        }
        return stb.toString();
    }
}