import java.io.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BudgetManager {

    CategoryManager categoryManager;
    private Map<String, Budget> budgets;
    private double monthlyIncome;

    public BudgetManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
        budgets = new HashMap<>();
        monthlyIncome = 0.0;
    }

    public CategoryManager getCategoryManager() {
        return categoryManager;
    }

    public void SetMonthlyIncome( Month month,double monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
        saveBudgetMonthDataToCSV(month);
    }

    public void editIncome( Month month,double newMonthlyIncome) {
        monthlyIncome = newMonthlyIncome;
        saveBudgetMonthDataToCSV(month);
    }

    public void addToIncome(double additionalIncome, Month month) {
        monthlyIncome += additionalIncome;
        saveBudgetMonthDataToCSV(month);
    }

    public double getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setCategoryBudget(String category, double amount, Month month) throws BudgetExistsException {

        if (!budgets.containsKey(category.toLowerCase())) {
            budgets.put(category.toLowerCase(), new Budget(category, amount));
        } else {
            throw new BudgetExistsException("Budget is already set for category " + category.toLowerCase() + ". If you want, you can edit it." + "\n");
        }

        saveBudgetMonthDataToCSV(month);
    }

    public void deleteCategoryBudget(String category, Month month) throws BudgetNotFoundException {

        if (budgets.containsKey(category.toLowerCase())) {
            budgets.remove(category.toLowerCase());
            // System.out.println("Budget for category " +category.toLowerCase()+ " has been deleted.");
        } else {
            throw new BudgetNotFoundException("No budget found for Category " + category.toLowerCase());
        }

        saveBudgetMonthDataToCSV(month);
    }

    public void editCategoryBudget(String category, double newAmount, Month month) throws BudgetNotFoundException, IOException {

        if (budgets.containsKey(category.toLowerCase())) {
            Budget budget = budgets.get(category.toLowerCase());
            budget.setAmount(newAmount);
            // System.out.println("Budget for category " +category.toLowerCase()+ "updated to " +newAmount);
        } else {
            throw new BudgetNotFoundException("No budget found for Category " + category.toLowerCase());
        }

        saveBudgetMonthDataToCSV(month);
    }


    public void loadMonthBudgetData(Month month) {
        String fileName = month.name().toLowerCase() + "Budget.csv";
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


    public void saveBudgetMonthDataToCSV(Month month) {

        String fileName = month.name().toLowerCase() + "Budget.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Income," + this.monthlyIncome);
            writer.newLine();
            for (Budget budget : budgets.values()) {
                writer.write(budget.getCategory().toLowerCase() + "," + budget.getAmount());
                writer.newLine();
            }
            System.out.println("Data for " + month.name().toLowerCase() + " saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data for " + month.name().toLowerCase());
        }
    }


    public void showAllBudgets(Month month) {
        //loadMonthBudgetData(month);


        if (budgets.isEmpty() && monthlyIncome == 0) {
            System.out.println("No data found for " + month.name().toLowerCase());
            return;
        }


        System.out.println("Income for " + month.name().toLowerCase() + ": " + this.monthlyIncome);
        System.out.println();
        System.out.println("Budgets for " + month.name().toLowerCase() + ":");
        for (Map.Entry<String, Budget> entry : budgets.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue().getAmount());

        }


        if (budgets.isEmpty()) {
            System.out.println("No budgets set for " + month.name().toLowerCase() + ".");
        }
    }


}
