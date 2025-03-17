import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SavingsManager {
    private Month month;
    private Year year;
    private BudgetManager budgetManager;
    private List<Expense> expenses;
    private List<Savings> savings;

    public SavingsManager(Month month, Year year, BudgetManager budgetManager, ExpenseManager expenseManager){
        this.month = month;
        this.year = year;
        this.budgetManager = budgetManager;
        this.expenses = expenseManager.getExpenses();
        this.savings = new ArrayList<>();
    }

    public List<Savings> getSavings(){
        savings.clear();
        calculateSavings();
        return savings;
    }

    private void calculateSavings(){
        for(Map.Entry<String,Budget> bg:budgetManager.getBudgets().entrySet()){
            String bgCategory = StringFormatter.capitalizeFirstLetter(bg.getKey());
            double saving = 0;
            for(Expense exp:expenses){
                if(exp.getCategory().equals(bgCategory)){
                    saving = bg.getValue().getAmount() - exp.getAmount();
                }
            }
            Savings sav = new Savings(bgCategory,saving);
            savings.add(sav);
        }
    }
    public double getTotalSavings(){
        double totalSavings = 0;
        for(Savings sav:savings){
            totalSavings+=sav.getAmount();
        }
        return totalSavings;
    }
}
