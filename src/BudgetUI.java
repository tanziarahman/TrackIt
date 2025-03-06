import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BudgetUI {
    private BudgetManager budgetManager;
    private CategoryManager categoryManager;
    Scanner scanner;


    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";


    public BudgetUI(BudgetManager budgetManager,CategoryManager categoryManager,Scanner scanner){
        this.budgetManager=budgetManager;
        this.categoryManager=categoryManager;
        this.scanner=scanner;
    }


    public void setBudget(){

        System.out.println(CYAN+ "Please choose from the following category list to set a budget:" +RESET);
        System.out.println();
        categoryManager.showCategories();

        int choice=getUserCategoryChoice(CYAN+ "Enter the number of your chosen category: " +RESET);
        String category= categoryManager.getCategories().get(choice-1).getType();

        double amount=getValidAmount(CYAN+ "Enter budget amount (In BDT): " +RESET);

        try {
            budgetManager.setCategoryBudget(category,amount);
            System.out.println(GREEN+ "✅ Budget for category " +StringFormatter.capitalizeFirstLetter(category)+ " with amount BDT " +amount+ " set successfully." +RESET);
            try {
                budgetManager.checkBudgetLimit();
            } catch (BudgetExceededIncomeException e) {
                System.out.println(YELLOW + e.getMessage() + RESET);
            }
        } catch (BudgetExistsException | CategoryDoesnotExistsException e) {
            System.out.println(RED + "❌ " + e.getMessage() + RESET);
        }
    }


    public void editBudget(){

        String category=getCategoryFromUserInput(CYAN+ "Enter the number of the budget you want to edit: " +RESET);

        if (category==null){
            System.out.println("No budgets available right now.");
            return;
        }

        double amount=getValidAmount(CYAN+ "Enter new amount (In BDT): " +RESET);

        try {
            budgetManager.editCategoryBudget(category,amount);
            System.out.println(GREEN + "✅ Budget for category " +StringFormatter.capitalizeFirstLetter(category)+ " updated to BDT " +amount +"." +RESET);
            try {
                budgetManager.checkBudgetLimit();
            } catch (BudgetExceededIncomeException e) {
                System.out.println(YELLOW + e.getMessage() + RESET);
            }
        } catch (BudgetNotFoundException e) {
            System.out.println(RED + "❌ " + e.getMessage() + RESET);
        }
    }

    public int getUserCategoryChoice(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= 1 && choice <= categoryManager.getCategories().size()) {
                    return choice;
                } else {
                    System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
            }
        }
    }


    public double getValidAmount(String prompt) {

        double amount;

        while (true) {
            try {
                System.out.print(prompt);
                amount = Double.parseDouble(scanner.nextLine());

                if (amount >0 ) {
                    return amount;
                }
                else {
                    System.out.println(RED + "⚠ Invalid input.Please enter a valid amount." +RESET);
                }


            } catch (NumberFormatException e) {
                System.out.println(RED + "⚠ Invalid input! Please enter a valid numeric amount." +RESET);
            }
        }
    }




    public String getCategoryFromUserInput(String prompt) {

        while (true) {

            List<String> categoryList = new ArrayList<>();

            for (String key : budgetManager.getBudgets().keySet()) {
                categoryList.add(key);
            }
            String[] categoryArray = categoryList.toArray(new String[0]);

            if (categoryArray.length == 0) {
                return null;
            }

            budgetManager.showAllBudgets();

            System.out.print(prompt);
            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());

                if (choice < 1 || choice > categoryArray.length) {
                    System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                    continue;
                }

            } catch (NumberFormatException e) {
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                continue;
            }

            return categoryArray[choice - 1];
        }
    }
}
