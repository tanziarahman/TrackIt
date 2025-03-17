import java.time.Month;
import java.time.Year;
import java.util.Scanner;

public class TrackIt {
    private static final Scanner scanner = new Scanner(System.in);

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";


    void run(){

        try {

            while (true) {

                DateManager dateManager = new DateManager();

                CategoryManager categoryManager = new CategoryManager();
                TransactionStorage transactionStorage = new FileTransactionStorage();
                IBudgetStorage budgetStorage = new BudgetCsvStorage();


                Month month = dateManager.getMonth();
                Year year = dateManager.getYear();


                BudgetManager budgetManager = new BudgetManager(categoryManager, month, year, budgetStorage);
                TransactionManager transactionManager = new TransactionManager(transactionStorage, budgetManager, month, year);
                ExpenseManager expenseManager=new ExpenseManager(month,year,budgetManager,transactionManager);
                SavingsManager savingsManager=new SavingsManager(month,year,budgetManager,expenseManager);
                Report report=new Report(expenseManager,savingsManager,budgetManager);


                CategoryUI categoryUI = new CategoryUI(categoryManager);
                IncomeUI incomeUI = new IncomeUI(budgetManager, month, year, scanner);
                BudgetUI budgetUI = new BudgetUI(budgetManager, categoryManager, month, year, scanner);
                TransactionUI transactionUI = new TransactionUI(transactionManager, budgetManager, categoryManager, scanner);
                ReportUI reportUI=new ReportUI(report);

                //new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                // System.out.print("\033[H\033[2J");
                //System.out.flush();



                System.out.println("\n" + BLUE +  "                                                                   ═════════════════════════════════════════");
                System.out.println(               "                                                                                   TRACK IT     ");
                System.out.println(               "                                                                   ═════════════════════════════════════════" + RESET);


                System.out.println(CYAN + "Month: " +StringFormatter.capitalizeFirstLetter(month.name()) );
                System.out.println("Year: " +year.getValue() +RESET);
                System.out.println();


                System.out.println("══════════════════════════════");
                System.out.println("         Main Menu");
                System.out.println("══════════════════════════════");

                // System.out.print("\033[H\033[2J");
                //System.out.flush();

                System.out.println(GREEN + "[1] Manage Categories");
                System.out.println("[2] Keep Track of Monthly Income");
                System.out.println("[3] Manage Budgets");
                System.out.println("[4] Manage Transactions");
                System.out.println("[5] Get An Overall monthly View");
                System.out.println("[6] Change date");
                System.out.println("[7] Exit" + RESET);
                System.out.print(YELLOW + "\nChoose an option: " + RESET);

                if (!scanner.hasNextInt()) {
                    System.out.println();
                    System.out.println(RED + "⚠ Invalid input! Please enter a number." + RESET);
                    scanner.next();
                    continue;
                }

                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        categoryUI.showCategoryUI();
                        break;
                    case 2:
                        incomeUI.manageIncome();
                        break;
                    case 3:
                        budgetUI.manageBudgets();
                        break;
                    case 4:
                        transactionUI.manageTransactions();
                        break;
                    case 5:
                        reportUI.showReport();
                        break;
                    case 6:
                        dateManager.changeDate();
                        System.out.println();
                        System.out.println(PURPLE+ "Date updated to " +StringFormatter.capitalizeFirstLetter(dateManager.getMonth().name())+" "+dateManager.getYear().getValue()+" successfully."+RESET);
                        System.out.println();
                        System.out.println();
                        System.out.println(BLUE+ "Press Enter to continue..." + RESET);
                        scanner.nextLine();
                        break;
                    case 7:
                        System.out.println();
                        System.out.println(PURPLE + "Exiting... Thank you for using Track It! " + RESET);
                        return;
                    default:
                        System.out.println();
                        System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                        System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println(RED + " Unexpected Error: " + e.getMessage() + RESET);
            System.out.println();
        }
    }

}