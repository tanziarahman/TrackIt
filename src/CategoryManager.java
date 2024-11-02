import java.util.ArrayList;
import java.util.Arrays;

public class CategoryManager {
    private ArrayList<Category> categories;
    public CategoryManager(){
        categories = new ArrayList<>();
    }
    public ArrayList getCategories(){
        return categories;
    }
    public void addCategory(String category) {
        if (categoryExists(category)) {
            try {
                throw new CategoryExistsException("Category <<" + category + ">> already exists");
            }
            catch (CategoryExistsException e) {
                e.printStackTrace();
            }
        }
        else{
            categories.add(new Category(category));
        }
    }

    public void addCustomSubCategoryInCategory(String category, String subCategory){
        if (!categoryExists(category)) {
            try {
                throw new CategoryDoesnotExistsException("Category <<" + category + ">> doesn't exists. So, sub-category <"+subCategory+"> can't be added.");
            }
            catch (CategoryDoesnotExistsException e) {
                e.printStackTrace();
            }
        }
        else {
            categories.stream().filter(c -> c.getType().equals(category)).findFirst().ifPresent(c -> c.addSubCategory(subCategory));
        }
    }
    public String showSubCategories(String category){
        if(!categoryExists(category)){
            try {
                throw new CategoryDoesnotExistsException("Category <<"+category+">> doesn't exists.");
            }
            catch (CategoryDoesnotExistsException e) {
                e.printStackTrace();
            }
        }
        Category matchingCategory = categories.stream().filter(c -> c.getType().equals(category)).findFirst().get();
        return matchingCategory.showSubCategories();
    }
    public String showCategories(){
        StringBuilder string = new StringBuilder();
        for(Category cp : categories){
            string.append("- "+cp.getType()+"\n");
        }
        return string.toString().trim();
    }
    public String showCategoriesWithSubCategories(){
        StringBuilder string = new StringBuilder();
        for(Category c : categories){
            string.append("<<"+c.getType()+">>\n");
            if(c.getSubcategories().isEmpty()){
                string.append("** No sub-categories available under category "+ c.getType() +" **\n");
            }
            else{
                for (int i = 0; i < c.getSubcategories().size(); i++) {
                    string.append((i + 1) + ". " + c.getSubcategories().get(i) + "\n");
                }
            }
        }
        return string.toString().trim();
    }
    public boolean categoryExists(String category){
        boolean exists = categories.stream().anyMatch(c -> category.equals(c.getType()));
        return exists;
    }
}
