import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpenseManager {
    private Month month;
    private Year year;
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private List<Expense> expenses;

    public ExpenseManager(Month month, Year year, BudgetManager budgetManager, TransactionManager transactionManager){
        this.month = month;
        this.year = year;
        this.budgetManager = budgetManager;
        this.transactionManager = transactionManager;
        this.expenses = new ArrayList<>();
    }
    public Month getMonth(){
        return month;
    }

    public List<Expense> getExpenses(){
        expenses.clear();
        calculateExpense();
        return expenses;
    }
    private void calculateExpense(){
        for(Map.Entry<String,Budget> bg: budgetManager.getBudgets().entrySet()){
            String bgCategory = bg.getKey();
            double expense = 0;
            for(Transaction t:transactionManager.getTransactions()){
                if(t.getCategory().equals(bgCategory)){
                    expense+=t.getAmount();
                }
            }
            Expense exp = new Expense(bgCategory,expense);
            expenses.add(exp);
        }
    }

    public String showAllExpenses(){
        calculateExpense();
        StringBuilder st = new StringBuilder();
        String m = this.month.toString();
        st.append("Expenses of " + m + ":\n");
        for(Expense ex : expenses){
            st.append(showExpenseForCategory(ex.getCategory()));
        }
        return  st.toString().trim();
    }

    public String showExpenseForCategory(String category){
        StringBuilder st = new StringBuilder();
        for(Map.Entry<String, Budget> bg : budgetManager.getBudgets().entrySet()){
            if(bg.getKey().equals(category)){
                String amount = String.valueOf(bg.getValue().getAmount());
                st.append("Budget in "+bg.getKey()+": "+amount + "\n");
            }
        }
        for(Transaction tr : transactionManager.getTransactions()){
            if(tr.getCategory().equals(category)){
                String amount = String.valueOf(tr.getAmount());
                st.append("Expense: "+ amount + "\n");
            }
        }
        return st.toString();
    }

    public double totalExpense(){
        double totalExpense = 0;
        for(Expense exp:expenses){
            totalExpense+=exp.getAmount();
        }
        return totalExpense;
    }
}
