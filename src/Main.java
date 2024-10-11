public class Main {
    public static void main(String[] args) {
        CategoryManager category = new CategoryManager();
        category.addCategory("Education");
        category.addCustomSubCategory("Education","rent");
        category.addCustomSubCategory("Education","stationary");
        category.addCustomSubCategory("Housing","utility");

        category.showCategoriesWithSubCategories();

    }
}