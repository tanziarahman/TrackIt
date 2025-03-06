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
