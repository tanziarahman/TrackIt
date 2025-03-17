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
    private static final String BLUE = "\u001B[34m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";


    public BudgetUI(BudgetManager budgetManager,CategoryManager categoryManager,Month month,Year year,Scanner scanner){
        this.budgetManager=budgetManager;
        this.categoryManager=categoryManager;
        this.month=month;
        this.year=year;
        this.scanner=scanner;
    }


    public void setBudget(){

        System.out.println();
        System.out.println(BLUE+ "Please choose from the following category list to set a budget:" +RESET);
        System.out.println();

        System.out.println(categoryManager.showCategories());

        System.out.println();
        int choice=getUserCategoryChoice(CYAN+ "Enter the number of your chosen category (or type 'back' to return): " +RESET);

        if (choice==-1){
            return;
        }

        String category= categoryManager.getCategories().get(choice-1).getType();

        if (budgetManager.budgetExists(category)){
            System.out.println();
            System.out.println(RED+ "Budget is already set for category " + StringFormatter.capitalizeFirstLetter(category) + ". If you want, you can edit it." +RESET);
            return;
        }

        System.out.println();
        double amount=getValidAmount(CYAN+ "Enter budget amount (In BDT or type 'back' to return): " +RESET);

        if (amount==-1){
            return;
        }

        try {
            budgetManager.setCategoryBudget(category,amount);
            System.out.println();
            System.out.println(GREEN+ "Budget for category " +StringFormatter.capitalizeFirstLetter(category)+ " with amount BDT " +amount+ " set successfully." +RESET);
            try {
                if (budgetManager.getMonthlyIncome()>0){
                    budgetManager.checkBudgetLimit();
                }
            } catch (BudgetExceededIncomeException e) {
                System.out.println();
                System.out.println(YELLOW + e.getMessage() + RESET);
            }
        } catch (BudgetExistsException | CategoryDoesnotExistsException e) {
            System.out.println();
            System.out.println(RED + e.getMessage() + RESET);
        }

        System.out.println();
        System.out.println(BLUE + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }


    public void editBudget() {

        System.out.println();
        if (budgetManager.getBudgets().isEmpty()) {
            System.out.println(RED+ "No budgets available for " +StringFormatter.capitalizeFirstLetter(month.name())+ " " +year.getValue());
        }
        else {

            showBudgets();

            System.out.println();
            String category = getCategoryFromUserInput(CYAN + "Enter the ID of the budget you want to edit (or type 'back' to return): " + RESET);

            if (category == null) {
                return;
            }

            try {
                System.out.println();
                double amount = getValidAmount(CYAN + "Enter new amount (In BDT or type 'back' to return): " + RESET);

                if (amount==-1){
                    return;
                }

                budgetManager.editCategoryBudget(category, amount);
                System.out.println();
                System.out.println(GREEN + "Budget for category " + StringFormatter.capitalizeFirstLetter(category) + " updated to BDT " + amount + "." + RESET);
                try {
                    budgetManager.checkBudgetLimit();
                } catch (BudgetExceededIncomeException e) {
                    System.out.println();
                    System.out.println(YELLOW + e.getMessage() + RESET);
                }
            } catch (BudgetNotFoundException e) {
                System.out.println();
                System.out.println(RED + e.getMessage() + RESET);
            }
        }

        System.out.println();
        System.out.println(BLUE + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }


    public void deleteBudget() {

        System.out.println();

        if (budgetManager.getBudgets().isEmpty()) {
            System.out.println(RED+ "No budgets available for " +StringFormatter.capitalizeFirstLetter(month.name())+ " " +year.getValue());
        }
        else {
            showBudgets();
            System.out.println();

            String category = getCategoryFromUserInput(CYAN + "Enter the number of the budget you want to delete (or type 'back' to return): " + RESET);

            if (category == null) {
                return;
            }

            try {
                budgetManager.deleteCategoryBudget(category);
                System.out.println();
                System.out.println(GREEN + "Budget for category " + StringFormatter.capitalizeFirstLetter(category) + " deleted successfully." + RESET);
            } catch (BudgetNotFoundException e) {
                System.out.println();
                System.out.println(RED + e.getMessage() + RESET);
            }
        }

        System.out.println();
        System.out.println(BLUE + "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    public void showBudgets(){

        System.out.println();
        if (budgetManager.getBudgets().isEmpty()) {
            System.out.println(RED+ "No budgets available for " +StringFormatter.capitalizeFirstLetter(month.name())+ " " +year.getValue());
        }
        else {
            System.out.println(PURPLE+ "Budgets for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " +year.getValue() + ":" +RESET);
            System.out.println();
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
                        System.out.println();
                        System.out.println(BLUE+ "Press Enter to continue..." + RESET);
                        scanner.nextLine();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println();
                        System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                }
            } else {
                System.out.println();
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                scanner.nextLine();
            }

        }
    }


    public void budgetMenu(){

        System.out.println();
        System.out.println(CYAN + "\n💰 Budget Management ["+ StringFormatter.capitalizeFirstLetter(month.name())+ " "+ year.getValue()+ "]" + RESET);
        System.out.println();
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

            String input=scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return -1;
            }

            try {
                int choice = Integer.parseInt(input);

                if (choice==0){
                    return -1;
                }
                if (choice >= 1 && choice <= categoryManager.getCategories().size()) {
                    return choice;
                } else {
                    System.out.println();
                    System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                    System.out.println();
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                System.out.println();
            }
        }
    }


    public double getValidAmount(String prompt) {

        double amount;

        while (true) {

            System.out.print(prompt);

            String input=scanner.nextLine();

            if (input.equalsIgnoreCase("back")) {
                return -1;
            }

            try {

                amount = Double.parseDouble(input);

                if (amount >0 ) {
                    return amount;
                }
                else {
                    System.out.println();
                    System.out.println(RED + "⚠ Invalid input! Please enter a valid amount." +RESET);
                }


            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "⚠ Invalid input! Please enter a valid amount." +RESET);
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


            System.out.print(prompt);
            int choice;

            String input=scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                return null;
            }

            try {
                choice = Integer.parseInt(input);

                if (choice < 1 || choice > categoryArray.length) {
                    System.out.println();
                    System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                    System.out.println();
                    continue;
                }

            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                System.out.println();
                continue;
            }

            return categoryArray[choice - 1];
        }
    }
}
