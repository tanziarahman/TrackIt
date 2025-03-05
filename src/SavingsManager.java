import java.io.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavingsManager {
    private Month month;
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private List<Savings> savings;
    public SavingsManager(Month month, BudgetManager budgetManager, TransactionManager transactionManager){
        this.month = month;
        this.budgetManager = budgetManager;
        this.transactionManager = transactionManager;
        this.savings = new ArrayList<>();
    }

    public Month getMonth(){
        return month;
    }

    private void calculateSavings(){
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
    }

    public String showAllSavings(){
        calculateSavings();
        StringBuilder st = new StringBuilder();
        String m = this.month.toString();
        st.append("Savings of " + m + ":\n");
        for(Savings sav : savings){
            st.append(showSavingsForCategory(sav.getCategory()));
        }
        return  st.toString().trim();
    }

    public String showSavingsForCategory(String category){
        StringBuilder st = new StringBuilder();
        double bgt=0, exp=0;
        for(Map.Entry<String,Budget> bg:budgetManager.getBudgets().entrySet()){
            if(bg.getKey().equals(category)){
                bgt = bg.getValue().getAmount();
                String budget = String.valueOf(bg.getValue().getAmount());
                st.append("Budget in "+bg.getKey()+": "+budget + "\n");
            }
        }
        for(Transaction tr : transactionManager.getTransactions()){
            if(tr.getCategory().equals(category)){
                exp = tr.getAmount();
                String expense = String.valueOf(tr.getAmount());
                st.append("Expense: "+ expense+ "\n");
            }
        }
        double savings = bgt - exp;
        st.append("Savings: "+savings+"\n");
        return st.toString();
    }
}
