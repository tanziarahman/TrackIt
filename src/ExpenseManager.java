import java.io.*;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class ExpenseManager {
    private Month month;
    private BudgetManager budgetManager;
    private TransactionManager transactionManager;
    private List<Expense> expenses;

    public ExpenseManager(Month month, BudgetManager budgetManager, TransactionManager transactionManager){
        this.month = month;
        this.budgetManager = budgetManager;
        this.transactionManager = transactionManager;
        this.expenses = new ArrayList<>();
        calculateExpense();
    }
    public Month getMonth(){
        return month;
    }

    public void readExpensesFile(){
        String m = month.toString();
        String f = m.toLowerCase();
        String csvFile = f+"expenses.csv";
        String line;
        String csvSeparator = ",";
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            while((line=br.readLine())!=null){
                String[] data = line.split(csvSeparator);
                if(data.length>0){
                    String c = data[0];
                    String a = data[1];
                    double am = Double.parseDouble(a);
                    expenses.add(new Expense(c,am));
                }
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void writeExpensesToFile() {
        String m = month.toString();
        String f = m.toLowerCase();
        String csvFile = f + "expenses.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (Expense expense : expenses) {
                String line = expense.getCategory() + "," + expense.getAmount();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    private void calculateExpense(){
        for(Transaction tr : transactionManager.getTransactions()){
            String ct = tr.getCategory();
            double am = tr.getAmount();
            Expense exp = new Expense(ct,am);
            expenses.add(exp);
        }
    }

    public String showExpenses(){
        StringBuilder st = new StringBuilder();
        String m = this.month.toString();
        st.append("Expenses of " + m + ":\n");
        int count = 1;
        for(Expense exp: expenses){
            String a = String.valueOf(exp.getAmount());
            String c = String.valueOf(count);
            st.append(c +". " + exp.getCategory() + " : " + a + "\n");
            count++;
        }
        return  st.toString().trim();
    }
}

