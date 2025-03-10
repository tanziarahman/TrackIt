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
        StringBuilder string = new StringBuilder();
        string.append("\n========================\n");
        string.append("      "+getType()).append("\n");
        string.append("========================\n");

        // If there are no subcategories, display a message
        if (subCategories.isEmpty()) {
            string.append("No subcategories available.\n");
        } else {
            int index = 1;
            for (String subCategory : subCategories) {
                string.append(index++).append(". ").append(subCategory).append("\n");
            }
        }

        return string.toString().trim();
    }
    public boolean subCategoryExists(String subCategory){
        if(subCategories.contains(subCategory)){
            return true;
        }
        return false;
    }
}
