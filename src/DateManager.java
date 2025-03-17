import java.io.*;
import java.time.Month;
import java.time.Year;
import java.util.Scanner;

class DateManager {
    private static final String DATE_FILE = "date.txt";
    private Month month;
    private Year year;


    private static final String CYAN = "\u001B[36m";
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";
    public DateManager() {
        loadDate();
    }

    private void loadDate() {
        File file = new File(DATE_FILE);
        if (file.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    String[] parts = line.split(",");
                    month = Month.valueOf(parts[0]);
                    year = Year.of(Integer.parseInt(parts[1]));
                    return;
                }
            } catch (IOException e) {
                System.out.println(RED+ "Error loading file."+RESET);
            }
        }
        takeUserDateInput();
    }

    public void takeUserDateInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println();
                System.out.print(CYAN+ "Select a month: "+RESET);
                month = Month.valueOf(scanner.next().toUpperCase());

                System.out.println();
                System.out.print(CYAN+ "Select a year: "+RESET);
                int inputYear = scanner.nextInt();
                year = Year.of(inputYear);

                saveDate();
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(RED + "Invalid input. Please enter valid values."+RESET);
                scanner.nextLine();
            }
        }
    }

    private void saveDate() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATE_FILE))) {
            bw.write(month + "," + year);
        } catch (IOException e) {
            System.out.println(RED+ "Error saving date."+RESET);
        }
    }

    public void changeDate() {
        takeUserDateInput();
        saveDate();
        loadDate();
    }

    public Month getMonth() {
        return month;
    }

    public Year getYear() {
        return year;
    }
}