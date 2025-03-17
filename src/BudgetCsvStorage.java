import java.io.*;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

public class BudgetCsvStorage implements IBudgetStorage {
    @Override
    public Map<String, Budget> loadMonthBudgetData(Month month, Year year) {

        Map<String,Budget> budgets=new HashMap<>();
        budgets.clear();


        String fileName = month.name().toLowerCase() +year.getValue() +  "Budget.csv";
        File file = new File(fileName);

        if (!file.exists()) {
            return new HashMap<>();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (!data[0].equalsIgnoreCase("Income")) {
                    budgets.put(data[0], new Budget(data[0], Double.parseDouble(data[1])));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading data for " + month.name().toLowerCase());
        }
        return budgets;
    }


    @Override
    public double loadMonthlyIncome(Month month,Year year) {
        String fileName = month.name().toLowerCase() + year.getValue() + "Budget.csv";
        File file = new File(fileName);
        double monthlyIncome=0.0;

        if (!file.exists()) {
            return 0.0;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equalsIgnoreCase("Income")) {
                    monthlyIncome = Double.parseDouble(data[1]);
                }
            }
        }catch(IOException e){
            System.out.println("Error reading data for " + month.name().toLowerCase());
        }

        return monthlyIncome;

    }

    @Override
    public void saveBudgetMonthDataToCSV(Map<String,Budget> budgets,double monthlyIncome,Month month, Year year) {

        String fileName = month.name().toLowerCase() +year.getValue() + "Budget.csv";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Income," + monthlyIncome);
            writer.newLine();
            for (Budget budget : budgets.values()) {
                writer.write(budget.getCategory().toLowerCase() + "," + budget.getAmount());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Error saving data for " + month.name().toLowerCase());
        }
    }
}