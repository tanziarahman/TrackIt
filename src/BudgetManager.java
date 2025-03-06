import java.io.*;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class BudgetManager {

    CategoryManager categoryManager;
    private Map<String, Budget> budgets;
    private double monthlyIncome;
    private Month month;
    private Year year;


    public BudgetManager(CategoryManager categoryManager,Month month,Year year){
        this.categoryManager = categoryManager;
        this.month=month;
        this.year=year;
        budgets = new HashMap<>();
        monthlyIncome = 0.0;
        loadMonthBudgetData();
    }

    public  Map<String,Budget> getBudgets(){
        return budgets;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void SetMonthlyIncome( double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
        saveBudgetMonthDataToCSV();
    }


    public void addToIncome(double additionalIncome) {
        monthlyIncome += additionalIncome;
        saveBudgetMonthDataToCSV();
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

        saveBudgetMonthDataToCSV();
    }

    public void deleteCategoryBudget(String category) throws BudgetNotFoundException {

        if (budgets.containsKey(category.toLowerCase())) {
            budgets.remove(category.toLowerCase());

        } else {
            throw new BudgetNotFoundException("No budget found for Category " + category.toLowerCase());
        }

        saveBudgetMonthDataToCSV();
    }

    public void editCategoryBudget(String category, double newAmount) throws BudgetNotFoundException{

        if (budgets.containsKey(category.toLowerCase())) {
            Budget budget = budgets.get(category.toLowerCase());
            budget.setAmount(newAmount);

        } else {
            throw new BudgetNotFoundException("No budget found for Category " + category.toLowerCase());
        }

        saveBudgetMonthDataToCSV();
    }


    public void loadMonthBudgetData() {

        budgets.clear();
        monthlyIncome = 0.0;


        String fileName = month.name().toLowerCase() +year.getValue() +  "Budget.csv";
        File file = new File(fileName);

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase("Income")) {
                    this.monthlyIncome = Double.parseDouble(data[1]);
                } else {
                    budgets.put(data[0], new Budget(data[0], Double.parseDouble(data[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading data for " + month.name().toLowerCase());
        }
    }


    public void saveBudgetMonthDataToCSV() {

        String fileName = month.name().toLowerCase() +year.getValue() + "Budget.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Income," + this.monthlyIncome);
            writer.newLine();
            for (Budget budget : budgets.values()) {
                writer.write(budget.getCategory().toLowerCase() + "," + budget.getAmount());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving data for " + month.name().toLowerCase());
        }
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
