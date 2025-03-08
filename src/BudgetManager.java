import java.time.Month;
import java.time.Year;
import java.util.Map;

public class BudgetManager {

    CategoryManager categoryManager;
    private Map<String, Budget> budgets;
    private double monthlyIncome;
    private IBudgetStorage storage;
    private Month month;
    private Year year;


    public BudgetManager(CategoryManager categoryManager,Month month,Year year,IBudgetStorage storage){
        this.categoryManager = categoryManager;
        this.month=month;
        this.year=year;
        this.storage=storage;
        this.monthlyIncome= storage.loadMonthlyIncome(month,year);
        this.budgets=storage.loadMonthBudgetData(month,year);
    }

    public  Map<String,Budget> getBudgets(){
        return budgets;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void SetMonthlyIncome( double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
        storage.saveBudgetMonthDataToCSV(budgets,monthlyIncome,month,year);
    }


    public void addToIncome(double additionalIncome) {
        monthlyIncome += additionalIncome;
        storage.saveBudgetMonthDataToCSV(budgets,monthlyIncome,month,year);
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setCategoryBudget(String category, double amount) throws BudgetExistsException,CategoryDoesnotExistsException{

        if(!categoryManager.categoryExists(category)){
            throw new CategoryDoesnotExistsException("Category " +category+ " doesn't exist. Please enter a valid category.");

        }

        if (!budgets.containsKey(category.toLowerCase())) {
            budgets.put(category.toLowerCase(), new Budget(category, amount));
        } else {
            throw new BudgetExistsException("Budget is already set for category " + category.toLowerCase() + ". If you want, you can edit it.");
        }

        storage.saveBudgetMonthDataToCSV(budgets,monthlyIncome,month,year);
    }

    public void deleteCategoryBudget(String category) throws BudgetNotFoundException {

        if (budgets.containsKey(category.toLowerCase())) {
            budgets.remove(category.toLowerCase());

        } else {
            throw new BudgetNotFoundException("No budget found for Category " + category.toLowerCase());
        }

        storage.saveBudgetMonthDataToCSV(budgets,monthlyIncome,month,year);
    }

    public void editCategoryBudget(String category, double newAmount) throws BudgetNotFoundException{

        if (budgets.containsKey(category.toLowerCase())) {
            Budget budget = budgets.get(category.toLowerCase());
            budget.setAmount(newAmount);

        } else {
            throw new BudgetNotFoundException("No budget found for Category " + category.toLowerCase());
        }

        storage.saveBudgetMonthDataToCSV(budgets,monthlyIncome,month,year);
    }



    public void showAllBudgets() {

        int index=1;

        for (Map.Entry<String, Budget> entry : budgets.entrySet()) {
            System.out.println(index + ". " +StringFormatter.capitalizeFirstLetter(entry.getKey()) + ": " + entry.getValue().getAmount());
            index++;
        }

    }


    public void showMonthlyIncome(){

        System.out.println("Income for " + StringFormatter.capitalizeFirstLetter(month.name()) + " " +year+ " : " + this.monthlyIncome);
    }

    public double getBudgetAmountForCategory(String category) throws BudgetNotFoundException{

        Budget budget=budgets.get(category.toLowerCase());

        if(budget!=null){
            return budget.getAmount();
        }
        else {
            throw new BudgetNotFoundException("No budget found for category " +category.toLowerCase());
        }
    }

    public double getTotalBudget(){
        return budgets.values().stream().mapToDouble(Budget::getAmount).sum();
    }


    public void checkBudgetLimit() throws BudgetExceededIncomeException {
        double totalBudget = getTotalBudget();
        if(totalBudget>monthlyIncome){
            throw new BudgetExceededIncomeException("Your total budget for " + StringFormatter.capitalizeFirstLetter(month.name()) + " is now " + totalBudget+ ". Which exceeds your monthly income of " +monthlyIncome+ ". You might want to double-check your plan!");
        }
    }


    public boolean budgetExists(String category){
        return budgets.containsKey(category.toLowerCase());
    }

}