import java.time.Month;
import java.time.Year;
import java.util.Map;

public interface IBudgetStorage {
    void saveBudgetMonthDataToCSV(Map<String,Budget> budgets,double monthlyIncome,Month month, Year year);
    Map<String,Budget> loadMonthBudgetData(Month month,Year year);
    double loadMonthlyIncome(Month month,Year year);
}
