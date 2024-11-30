import java.text.SimpleDateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaction {
    private int transactionID;
    private double amount;
    private String category;
    private String subCategory;
    private Date date;
    private String description;

    public Transaction(int transactionID, double amount, String category, String subCategory, Date date, String description) {
        this.transactionID = transactionID;
        this.amount = amount;
        this.category = category;
        this.subCategory = subCategory;
        this.date = date;
        this.description = description;
    }


    public int getTransactionID() { return transactionID; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public String getSubCategory() { return subCategory; }
    public Date getDate() { return date; }
    public String getDescription() { return description; }


    public void setAmount(double amount) { this.amount = amount; }
    public void setCategory(String category) { this.category = category; }
    public void setSubCategory(String subCategory) { this.subCategory = subCategory; }
    public void setDate(Date date) { this.date = date; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return transactionID + "," + amount + "," + category + "," + subCategory + "," + sdf.format(date) + "," + description;
    }

    public static Transaction fromString(String transactionString) throws Exception {
        transactionString = transactionString.trim();
        if (transactionString.isEmpty()) {
            throw new Exception("Empty transaction string provided.");
        }
        System.out.println("Parsing transactionString: " + transactionString);
        String[] parts = transactionString.split(",");
        if (parts.length != 6) {
            throw new Exception("Invalid input format. Expected 6 parts, but got " + parts.length + " in string: " + transactionString);
        }
        try {
            int transactionID = Integer.parseInt(parts[0].trim());
            double amount = Double.parseDouble(parts[1].trim());
            String category = parts[2].trim();
            String subCategory = parts[3].trim();
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(parts[4].trim());
            String description = parts[5].trim();
            return new Transaction(transactionID, amount, category, subCategory, date, description);
        } catch (NumberFormatException e) {
            throw new Exception("Invalid number format in transaction string: " + transactionString, e);
        } catch (Exception e) {
            throw new Exception("Invalid date format in transaction string. Expected yyyy-MM-dd: " + transactionString, e);
        }
    }



}
