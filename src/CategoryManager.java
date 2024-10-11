import java.util.ArrayList;
import java.util.Arrays;

public class CategoryManager {
    private ArrayList<Category> categories;
    public CategoryManager(){
        categories = new ArrayList<>(Arrays.asList(new Category("Housing")));
    }
    public void addCategory(String category){
        for(Category c:categories) {
            if (category == c.getType()) {
                System.out.println("Category <<" + category + ">> already exists!");
                return;
            }
        }
        categories.add(new Category(category));
    }
    public void addCustomSubCategory(String category, String subCategory){
        for(Category c:categories){
            if(category==c.getType()){
                for(String sc:c.getSubcategories()){
                    if(sc==subCategory){
                        System.out.println("Sub Category <"+subCategory+"> already exists in <<"+category+">>");
                        return;
                    }
                }
                c.addSubCategory(subCategory);
                return;
            }
        }
        System.out.println("Category <<"+category+">> doesn't exist. So, sub category <"+subCategory+"> cannot be added.");
    }
    public void showSubCategories(String category){
        for(Category c:categories){
            if(c.getType()==category){
                int count=1;
                System.out.println("Sub categories under <<"+category+">> are:");
                for(String subCategory: c.getSubcategories()) {
                    System.out.println(count + ". " + subCategory);
                    count++;
                }
                return;
            }
        }
        System.out.println("The category you're trying to access is not available.");
    }
    public void showCategories(){
        for(Category c:categories){
            System.out.println("- "+c.getType());
        }
    }
    public void showCategoriesWithSubCategories(){
        for(Category c: categories){
            System.out.println("<<"+c.getType()+">>");
            for(String subCategories:c.getSubcategories()){
                System.out.println("- "+subCategories);
            }
        }
    }
}
