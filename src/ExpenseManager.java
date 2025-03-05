import java.io.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExpenseManager {
    private Month month;
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private List<Expense> expenses;

    public ExpenseManager(Month month, BudgetManager budgetManager, TransactionManager transactionManager){
        this.month = month;
        this.budgetManager = budgetManager;
        this.transactionManager = transactionManager;
        this.expenses = new ArrayList<>();
    }
    public Month getMonth(){
        return month;
    }


    private void calculateExpense(){
        for(Transaction tr : transactionManager.getTransactions()){
            String ct = tr.getCategory();
            double am = tr.getAmount();
            Expense exp = new Expense(ct,am);
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
}
