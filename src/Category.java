import java.util.ArrayList;

public class Category {
    private String type;
    private ArrayList<String> subCategories;


    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";

    public Category(String type) {
        this.type = type;
        subCategories = new ArrayList<>();
    }

    public ArrayList<String> getSubcategories() {
        return subCategories;
    }


    public void addSubCategory(String subCategory) throws SubCategoryExistsException {
        if (subCategoryExists(subCategory)) {
            System.out.println();
            throw new SubCategoryExistsException(RED+"Sub Category " + StringFormatter.capitalizeFirstLetter(subCategory) + " already exists under " + StringFormatter.capitalizeFirstLetter(getType()) + " category."+RED);

        }
        subCategories.add(subCategory);
    }

    public String getType() {
        return type;
    }

    public String showSubCategories() {
        StringBuilder string = new StringBuilder("\n══════════════════════════════\n");
        string.append(PURPLE+"           ").append(StringFormatter.capitalizeFirstLetter(getType())).append("\n"+RESET);
        string.append("══════════════════════════════\n");

        if (subCategories.isEmpty()) {
            System.out.println();
            string.append(RED+"No subcategories available.\n"+RESET);
        } else {
            int index = 1;
            for (String subCategory : subCategories) {
                string.append(index++).append(". ").append(subCategory).append("\n");
            }
        }
        return string.toString().trim();
    }

    public boolean subCategoryExists(String subCategory) {
        return subCategories.stream()
                .anyMatch(sc -> sc.equalsIgnoreCase(subCategory));
    }
    public void deleteSubCategory(String subCategory) throws SubCategoryDoesNotExistException {
        boolean removed = subCategories.removeIf(sc -> sc.equalsIgnoreCase(subCategory));
        if (!removed) {
            throw new SubCategoryDoesNotExistException("Sub Category <" + subCategory + "> does not exist under <<" + getType() + ">> category.");
        }
    }
}