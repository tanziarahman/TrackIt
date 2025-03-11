import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BudgetUI {
    private BudgetManager budgetManager;
    private CategoryManager categoryManager;
    private Month month;
    private Year year;
    Scanner scanner;


    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";


    public BudgetUI(BudgetManager budgetManager,CategoryManager categoryManager,Month month,Year year,Scanner scanner){
        this.budgetManager=budgetManager;
        this.categoryManager=categoryManager;
        this.month=month;
        this.year=year;
        this.scanner=scanner;
    }


    public void setBudget(){

        System.out.println(CYAN+ "Please choose from the following category list to set a budget:" +RESET);
        System.out.println();
        System.out.println(categoryManager.showCategories());

        int choice=getUserCategoryChoice(CYAN+ "Enter the number of your chosen category: " +RESET);
        String category= categoryManager.getCategories().get(choice-1).getType();

        if (budgetManager.budgetExists(category)){
            System.out.println(YELLOW+ "Budget is already set for category " + StringFormatter.capitalizeFirstLetter(category) + ". If you want, you can edit it." +RESET);
            return;
        }

        double amount=getValidAmount(CYAN+ "Enter budget amount (In BDT): " +RESET);

        try {
            budgetManager.setCategoryBudget(category,amount);
            System.out.println(GREEN+ "✅ Budget for category " +StringFormatter.capitalizeFirstLetter(category)+ " with amount BDT " +amount+ " set successfully." +RESET);
            try {
                if (budgetManager.getMonthlyIncome()>0){
                    budgetManager.checkBudgetLimit();
                }
            } catch (BudgetExceededIncomeException e) {
                System.out.println();
                System.out.println(YELLOW + e.getMessage() + RESET);
            }
        } catch (BudgetExistsException | CategoryDoesnotExistsException e) {
            System.out.println(RED + e.getMessage() + RESET);
        }
    }


    public void editBudget() {

        if (budgetManager.getBudgets().isEmpty()) {
            System.out.println("No budgets for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " +year.getValue() + "available for now.");
        }
        else {

            showBudgets();

            String category = getCategoryFromUserInput(CYAN + "Enter the number of the budget you want to edit: " + RESET);

            try {
                double amount = getValidAmount(CYAN + "Enter new amount (In BDT): " + RESET);
                budgetManager.editCategoryBudget(category, amount);
                System.out.println(GREEN + "✅ Budget for category " + StringFormatter.capitalizeFirstLetter(category) + " updated to BDT " + amount + "." + RESET);
                try {
                    budgetManager.checkBudgetLimit();
                } catch (BudgetExceededIncomeException e) {
                    System.out.println(YELLOW + e.getMessage() + RESET);
                }
            } catch (BudgetNotFoundException e) {
                System.out.println(RED + e.getMessage() + RESET);
            }
        }
    }


    public void deleteBudget() {

        if (budgetManager.getBudgets().isEmpty()) {
            System.out.println("No budgets for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " +year.getValue() + "available for now.");
        }
        else {
            showBudgets();

            String category = getCategoryFromUserInput(CYAN + "Enter the number of the budget you want to delete: " + RESET);

            /*if (category == null) {
                System.out.println("No budgets available right now.");
                return;
            }*/

            try {
                budgetManager.deleteCategoryBudget(category);
                System.out.println(GREEN + "✅ Budget for category " + StringFormatter.capitalizeFirstLetter(category) + " deleted successfully." + RESET);
            } catch (BudgetNotFoundException e) {
                System.out.println(RED + e.getMessage() + RESET);
            }
        }
    }

    public void showBudgets(){

        if (budgetManager.getBudgets().isEmpty()) {
            System.out.println("No budgets for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " +year.getValue() + "available for now.");
        }
        else {
            System.out.println(CYAN+ "Budgets for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " +year.getValue() + ":" +RESET);
            budgetManager.showAllBudgets();
        }
    }



    public void manageBudgets(){

        while (true) {

            budgetMenu();

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                scanner.nextLine();

                switch (choice) {
                    case 1:
                        setBudget();
                        break;
                    case 2:
                        editBudget();
                        break;
                    case 3:
                        deleteBudget();
                        break;
                    case 4:
                        showBudgets();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                        System.out.println();
                }
            } else {
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                scanner.nextLine();
            }

        }
    }


    public void budgetMenu(){

        System.out.println(CYAN + "\n💰 Budget Management" + RESET);
        System.out.println(GREEN + "[1] Set Budget");
        System.out.println("[2] Edit Budget");
        System.out.println("[3] Delete Budget");
        System.out.println("[4] View Budgets");
        System.out.println("[5] Go Back to Main Menu" + RESET);
        System.out.println();
        System.out.print(YELLOW + "Choose an option: " + RESET);
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
