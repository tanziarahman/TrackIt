public class Savings {
    private String category;
    private double amount;
    public Savings(String category, double amount){
        this.category = category;
        this.amount = amount;
    }
    public  String getCategory(){
        return category;
    }
    public double getAmount(){
        return  amount;
    }
    public void setAmount(double am){
        this.amount = am;
    }
}