import java.util.ArrayList;

public class Category {
    private String type;
    private ArrayList<String> subCategories;
    public Category(String type){
        this.type = type;
        subCategories = new ArrayList<>();
    }
    public ArrayList<String> getSubcategories(){
        return subCategories;
    }
    public void addSubCategory(String subCategory){
        subCategories.add(subCategory);
    }
    public String getType(){
        return type;
    }
}
