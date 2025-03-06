import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    private ArrayList<Category> categories;
    public CategoryManager(){
        categories = new ArrayList<>();
        readFile();
    }

    public ArrayList<Category> getCategories(){
        return categories;
    }

    public void addCategory(String category) throws CategoryExistsException {
        if (categoryExists(category)) {
            throw new CategoryExistsException("Category <<" + category + ">> already exists");
        } else {
            categories.add(new Category(category));
        }
    }

    public void addCustomSubCategoryInCategory(String category, String subCategory) throws CategoryDoesnotExistsException {
        if (!categoryExists(category)) {
            throw new CategoryDoesnotExistsException("Category <<" + category + ">> doesn't exist. So, sub-category <" + subCategory + "> can't be added.");
        }
        else {
            categories.stream().filter(c -> c.getType().equals(category)).findFirst().ifPresent(c -> c.addSubCategory(subCategory));
        }
    }

    public String showSubCategories(String category) throws CategoryDoesnotExistsException {
        if (!categoryExists(category)) {
            throw new CategoryDoesnotExistsException("Category <<" + category + ">> doesn't exist.");
        }

        Category matchingCategory = categories.stream()
                .filter(c -> c.getType().equals(category))
                .findFirst()
                .orElseThrow(() -> new CategoryDoesnotExistsException("Category <<" + category + ">> doesn't exist."));

        List<String> subcategories = matchingCategory.getSubcategories();

        // Formatting the output
        StringBuilder string = new StringBuilder();
        string.append("\n========================\n");
        string.append("        Category: ").append(category).append("\n");
        string.append("========================\n");

        if (subcategories.isEmpty()) {
            string.append("No subcategories available.\n");
        } else {
            int index = 1;
            for (String subCategory : subcategories) {
                string.append(index++).append(". ").append(subCategory).append("\n");
            }
        }

        string.append("========================\n");
        return string.toString();
    }

    public String showCategories(){
        StringBuilder string = new StringBuilder();
        string.append("\n========================\n");
        string.append("  Available Categories   \n");
        string.append("========================\n");
        int index = 1;
        for (Category cp : categories) {
            string.append(index++).append(". ").append(cp.getType()).append("\n");
        }
        return string.toString().trim();
    }

    public String showCategoriesWithSubCategories() {
        StringBuilder string = new StringBuilder();

        if (categories.isEmpty()) {
            string.append("\n========================\n");
            string.append("   No Categories Available\n");
            string.append("========================\n");
            return string.toString();
        }

        for (Category c : categories) {
            string.append("\n========================\n");
            string.append("        Category: ").append(c.getType()).append("\n");
            string.append("========================\n");

            List<String> subcategories = c.getSubcategories();
            if (subcategories.isEmpty()) {
                string.append("No subcategories available.\n");
            } else {
                int index = 1;
                for (String subCategory : subcategories) {
                    string.append(index++).append(". ").append(subCategory).append("\n");
                }
            }
            string.append("========================\n");
        }
        return string.toString();
    }

    public boolean categoryExists(String category){
        boolean exists = categories.stream().anyMatch(c -> category.equals(c.getType()));
        return exists;
    }

    public void readFile(){
        String csvFile = "Categories and Subcategories.csv";
        String line;
        String csvSeparator = ",";
        try(BufferedReader br = new BufferedReader(new FileReader(csvFile))){
            while((line=br.readLine())!=null){
                String[] data = line.split(csvSeparator);
                if(data.length>0){
                    String categoryName = data[0];
                    Category category = new Category(categoryName);
                    for (int i = 1; i < data.length; i++) {
                        category.addSubCategory(data[i].trim());
                    }
                    categories.add(category);
                }
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void writeFile() {
        String csvFile = "Categories and Subcategories.csv";

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFile))) {
            for (Category category : categories) {
                StringBuilder line = new StringBuilder(category.getType());
                for (String subCategory : category.getSubcategories()) {
                    line.append(",").append(subCategory.trim());
                }
                bw.write(line.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }
}
