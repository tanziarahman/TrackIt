import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoryManager {
    private ArrayList<Category> categories;
    private Scanner scanner;

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";

    public CategoryManager() {
        categories = new ArrayList<>();
        readFile();
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void addCategory(String category) throws CategoryExistsException {
        String formattedCategory = formatCategoryName(category);
        if (categoryExists(formattedCategory)) {
            throw new CategoryExistsException("Category " + StringFormatter.capitalizeFirstLetter(category) + " already exists");
        } else {
            categories.add(new Category(formattedCategory));
            writeFile();
        }
    }

    public void deleteCategoryWithSubCategories(String category) throws CategoryDoesnotExistsException {
        String formattedCategory = formatCategoryName(category);
        boolean categoryFound = categories.removeIf(ct -> ct.getType().equalsIgnoreCase(formattedCategory));

        if (!categoryFound) {
            throw new CategoryDoesnotExistsException("Category " + category + " does not exist.");
        }
        writeFile();
    }

    public void addCustomSubCategoryInCategory(String category, String subCategory) throws CategoryDoesnotExistsException {
        String formattedCategory = formatCategoryName(category);
        Category categoryToUpdate = categories.stream()
                .filter(c -> c.getType().equalsIgnoreCase(formattedCategory))
                .findFirst()
                .orElseThrow(() -> new CategoryDoesnotExistsException("Category " + category + " does not exist."));
        try {
            categoryToUpdate.addSubCategory(subCategory);
        } catch (SubCategoryExistsException e) {
            throw e;
        }
        writeFile();
    }

    public String showSubCategories(Category category) {
        return category.showSubCategories();
    }

    public String showCategories() {
        StringBuilder string = new StringBuilder();
        string.append("\n══════════════════════════════\n");
        string.append(PURPLE+"     Available Categories   \n"+RESET);
        string.append("══════════════════════════════\n");
        System.out.println();
        int index = 1;
        for (Category cp : categories) {
            string.append(index++).append(". ").append(StringFormatter.capitalizeFirstLetter(cp.getType())).append("\n");
        }
        return string.toString().trim();
    }

    public String showCategoriesWithSubCategories() {
        StringBuilder string = new StringBuilder();

        if (categories.isEmpty()) {

            string.append(RED+"  \nNo Categories Available\n"+RESET);

            return string.toString();
        }

        for (Category c : categories) {
            string.append("\n══════════════════════════════\n");
            string.append(PURPLE+" Category: ").append(StringFormatter.capitalizeFirstLetter(c.getType())).append("\n"+RESET);
            string.append("══════════════════════════════\n");

            List<String> subcategories = c.getSubcategories();
            if (subcategories.isEmpty()) {
                string.append("No subcategories available.\n");
            } else {
                int index = 1;
                for (String subCategory : subcategories) {
                    string.append(index++).append(". ").append(StringFormatter.capitalizeFirstLetter(subCategory)).append("\n");
                }
            }

            string.append(CYAN+"==============================\n"+RESET);
        }
        return string.toString();
    }

    public boolean categoryExists(String category) {
        return categories.stream().anyMatch(c -> c.getType().equalsIgnoreCase(category));
    }

    public void readFile() {
        String csvFile = "Categories and Subcategories.csv";
        String line;
        String csvSeparator = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            while ((line = br.readLine()) != null) {
                String[] data = line.split(csvSeparator);
                if (data.length > 0) {
                    String categoryName = formatCategoryName(data[0]);
                    Category category = new Category(categoryName);
                    for (int i = 1; i < data.length; i++) {
                        category.addSubCategory(data[i].trim());
                    }
                    categories.add(category);
                }
            }
        } catch (IOException | SubCategoryExistsException e) {
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

    public void deleteSubCategory(String categoryName, String subCategory) throws CategoryDoesnotExistsException, SubCategoryDoesNotExistException {
        Category category = getCategoryByName(categoryName);

        if (category == null) {
            throw new CategoryDoesnotExistsException("Category <" + categoryName + "> does not exist.");
        }

        category.deleteSubCategory(subCategory);
        writeFile();
    }

    private Category getCategoryByName(String categoryName) {
        for (Category category : categories) {
            if (category.getType().equalsIgnoreCase(categoryName)) {
                return category;
            }
        }
        return null; 
    }

    private String formatCategoryName(String category) {
        return category.trim().toLowerCase();
    }
}
