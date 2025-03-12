import java.util.Scanner;

public class ReportUI {
    private SavingsManager savingsManager;

    public ReportUI(SavingsManager savingsManager){
        this.savingsManager = savingsManager;
    }

    public void showReport(){
        Scanner sc = new Scanner(System.in);
        System.out.println(savingsManager.showAllSavings());
        System.out.println("\nPress Enter to return to Main Menu");
        sc.nextLine();
    }
}