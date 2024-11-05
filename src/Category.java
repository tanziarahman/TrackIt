import java.util.ArrayList;

public class Category {
    private String type;
    private ArrayList<String> subCategories;

    public Category(String type) {
        this.type = type;
        subCategories = new ArrayList<>();
    }

    public ArrayList<String> getSubcategories() {
        return subCategories;
    }
    public void addSubCategory(String subCategory) throws SubCategoryExistsException{
        if (subCategoryExists(subCategory)) {
            throw new SubCategoryExistsException("Sub Category <" + subCategory + "> already exists under <<" + getType() + ">> category");
        }
        else {
            subCategories.add(subCategory);
        }
    }
    public String getType() {
        return type;
    }
    public String showSubCategories(){
        StringBuilder subcategories = new StringBuilder();
        subcategories.append("<<"+getType()+">>\n");
        for (int i = 0; i < subCategories.size(); i++) {
            subcategories.append((i + 1) + ". " + subCategories.get(i) + "\n");
        }
        return subcategories.toString().trim();
    }
    public boolean subCategoryExists(String subCategory){
        if(subCategories.contains(subCategory)){
            return true;
        }
        return false;
    }
}
