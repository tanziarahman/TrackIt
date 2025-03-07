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
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";

    public IncomeUI(BudgetManager budgetManager,Month month,Year year,Scanner scanner){
        this.budgetManager=budgetManager;
        this.month=month;
        this.year=year;
        this.scanner=scanner;
    }

    public void setIncome(){

        if(budgetManager.getMonthlyIncome()==0){
            double amount=getValidAmount(CYAN + "Please set up your monthly income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " +year + " to get started: " +RESET);
            budgetManager.SetMonthlyIncome(amount);
            System.out.println(GREEN + "✅ Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year + "set to BDT " +amount + " successfully" +RESET);
        }
    }

    public void viewMonthlyIncome(){
        System.out.println(CYAN + "Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year + ": BDT " +budgetManager.getMonthlyIncome() +RESET);
    }


    public void editIncome(){
        System.out.println(CYAN + "Current Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year + ": BDT " +budgetManager.getMonthlyIncome() +RESET);
        System.out.println();
        double amount=getValidAmount(CYAN + "Enter new amount (In BDT): " +RESET);
        budgetManager.SetMonthlyIncome(amount);
        System.out.println(GREEN + "✅ Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year + "updated to BDT " +amount + " successfully" +RESET);
    }

    public void addToIncome(){
        System.out.println(CYAN + "Current Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year + ": BDT " +budgetManager.getMonthlyIncome() +RESET);
        System.out.println();
        double amount=getValidAmount(CYAN+ "Enter the amount you want to add (In BDT): " +RESET);
        budgetManager.addToIncome(amount);
        System.out.println(GREEN + "✅ Income for " +StringFormatter.capitalizeFirstLetter(month.name()) + " " + year + "updated to BDT " +budgetManager.getMonthlyIncome() + " successfully" +RESET);
    }




    public void manageIncome(){

        while (true) {

            IncomeMenu();

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                scanner.nextLine();

                switch (choice) {
                    case 1:
                        viewMonthlyIncome();
                        break;
                    case 2:
                        editIncome();
                        break;
                    case 3:
                        addToIncome();
                        break;
                    case 4:
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


    public void IncomeMenu(){

        System.out.println(CYAN + "\n💰 Income Management" + RESET);
        System.out.println(GREEN + "[1] View Your Monthly Income");
        System.out.println("[2] Edit Income Amount");
        System.out.println("[3] Add to your Existing Income");
        System.out.println("[4] Go Back to Main Menu" + RESET);
        System.out.println();
        System.out.print(YELLOW + "Choose an option: " + RESET);
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
                System.out.println(RED + "⚠ Invalid input! Please enter a valid amount." +RESET);
            }
        }
    }

}
