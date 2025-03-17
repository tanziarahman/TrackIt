import java.util.Scanner;

public class ReportUI {
    private Report report;

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";

    public ReportUI(Report report){
        this.report = report;
    }

    public void showReport(){
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println(report.showReport());
        System.out.println(BLUE + "\nPress Enter to continue..." +RESET);
        sc.nextLine();
    }
}
