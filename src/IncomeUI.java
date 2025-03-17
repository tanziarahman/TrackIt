import java.time.Month;
import java.time.Year;
import java.util.Scanner;

public class IncomeUI {

    private BudgetManager budgetManager;
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

    public IncomeUI(BudgetManager budgetManager,Month month,Year year,Scanner scanner){
        this.budgetManager=budgetManager;
        this.month=month;
        this.year=year;
        this.scanner=scanner;
    }

    public void setIncome(){

        System.out.println();
        System.out.println(PURPLE + "Current monthly income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " +year.getValue() + ": " +budgetManager.getMonthlyIncome() +RESET);
        System.out.println();

        double amount=getValidAmount(CYAN + "Enter amount (In BDT or type 'back' to return): " +RESET);

        if (amount==-1){
            return;
        }

        budgetManager.SetMonthlyIncome(amount);

        System.out.println();
        System.out.println(GREEN + "Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year.getValue() + " set to BDT " +amount + " successfully." +RESET);

        System.out.println();
        System.out.println(BLUE+ "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    public void viewMonthlyIncome(){
        System.out.println();
        System.out.println(PURPLE + "Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year.getValue() + ": BDT " +budgetManager.getMonthlyIncome() +RESET);

        System.out.println();
        System.out.println(BLUE+ "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }

    public void addToIncome(){
        System.out.println();
        System.out.println(PURPLE + "Current monthly Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year.getValue() + ": BDT " +budgetManager.getMonthlyIncome() +RESET);
        System.out.println();
        double amount=getValidAmount(CYAN+ "Enter the amount you want to add (In BDT or type 'back' to return): " +RESET);
        if (amount==-1){
            return;
        }
        budgetManager.addToIncome(amount);
        System.out.println();
        System.out.println(GREEN + "Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year.getValue() + " updated to BDT " +budgetManager.getMonthlyIncome() + " successfully" +RESET);

        System.out.println();
        System.out.println(BLUE+ "Press Enter to continue..." + RESET);
        scanner.nextLine();
    }




    public void manageIncome(){

        while (true) {

            IncomeMenu();

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                scanner.nextLine();

                switch (choice) {
                    case 1:
                        setIncome();
                        break;
                    case 2:
                        viewMonthlyIncome();
                        break;
                    case 3:
                        addToIncome();
                        break;
                    case 4:
                        return;

                    default:
                        System.out.println();
                        System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                        System.out.println();
                }
            } else {
                System.out.println();
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                System.out.println();
                scanner.nextLine();
            }

        }
    }


    public void IncomeMenu(){

        System.out.println();
        System.out.println(CYAN + "\n💰 Income Management ["+ StringFormatter.capitalizeFirstLetter(month.name())+ " "+ year.getValue()+ "]" + RESET);
        System.out.println();
        System.out.println(GREEN + "[1] Set/Edit Monthly Income");
        System.out.println("[2] View Monthly Income");
        System.out.println("[3] Add to Existing Income");
        System.out.println("[4] Go Back to Main Menu" + RESET);
        System.out.println();
        System.out.print(YELLOW + "Choose an option: " + RESET);
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

                if (amount >=0 ) {
                    return amount;
                }
                else {
                    System.out.println();
                    System.out.println(RED + "⚠ Invalid input! Please enter a valid amount." +RESET);
                    System.out.println();
                }

            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "⚠ Invalid input! Please enter a valid amount." +RESET);
                System.out.println();
            }
        }
    }

}

