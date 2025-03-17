
import java.lang.module.ResolutionException;
import java.util.Scanner;

public class CategoryUI {
    private CategoryManager categoryManager;
    private final Scanner scanner = new Scanner(System.in);


    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String CYAN = "\u001B[36m";
    private static final String PURPLE = "\u001B[35m";

    public CategoryUI(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public void showCategoryUI() {
        while (true) {

            System.out.println();
            System.out.println(CYAN+"\n\uD83D\uDCC2 Category Management"+RESET);
            System.out.println();
            System.out.println(GREEN+"[1] View all categories");
            System.out.println("[2] Add a new category");
            System.out.println("[3] Delete a category");
            System.out.println("[4] Add a subcategory");
            System.out.println("[5] Delete a subcategory");
            System.out.println("[6] View subcategories of a category");
            System.out.println("[7] View categories with subcategories");
            System.out.println("[8] Return to Main Menu"+RESET);
            System.out.println();
            System.out.print(YELLOW+"Enter your choice (1-8): "+RESET);

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();

                scanner.nextLine();

                switch (choice) {
                    case 1:
                        System.out.println();
                        System.out.println(categoryManager.showCategories());
                        promptBack();
                        break;

                    case 2:
                        while (true) {
                            System.out.println();
                            System.out.println(categoryManager.showCategories());
                            System.out.println();
                            System.out.print(CYAN + "Enter the name of the new category (or type 'back' to return): " + RESET);
                            String category = scanner.nextLine();
                            if (category.equalsIgnoreCase("back")) break;
                            try {
                                categoryManager.addCategory(category);
                                System.out.println();
                                System.out.println(GREEN + "Category added successfully." + RESET);
                                promptBack();
                                break;
                            } catch (CategoryExistsException e) {
                                System.out.println();
                                System.out.println(RED + e.getMessage() + RESET);
                            }
                        }
                        break;

                    case 3:
                        while (true) {
                            System.out.println();
                            System.out.println(categoryManager.showCategories());
                            System.out.println();
                            System.out.print(CYAN + "Enter the number of the category to delete (or type 'back' to return): " + RESET);
                            String input = scanner.nextLine();
                            if (input.equalsIgnoreCase("back")) break;
                            try {
                                int index = Integer.parseInt(input);
                                if (index < 1 || index > categoryManager.getCategories().size()) {
                                    System.out.println();
                                    System.out.println(RED + "Invalid category number." + RESET);
                                    continue;
                                }
                                Category categoryToDelete = categoryManager.getCategories().get(index - 1);
                                categoryManager.deleteCategoryWithSubCategories(categoryToDelete.getType());
                                System.out.println();
                                System.out.println(GREEN + "Category deleted successfully." + RESET);
                                promptBack();
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println();
                                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
                            } catch (CategoryDoesnotExistsException e) {
                                System.out.println();
                                System.out.println(RED + e.getMessage() + RESET);
                            }
                        }
                        break;

                    case 4:
                        while (true) {
                            System.out.println();
                            System.out.println(categoryManager.showCategories());
                            System.out.println();
                            System.out.print(CYAN + "Enter the number of the category to add a subcategory (or type 'back' to return): " + RESET);
                            String input = scanner.nextLine();
                            if (input.equalsIgnoreCase("back")) break;
                            try {
                                int index = Integer.parseInt(input);
                                if (index < 1 || index > categoryManager.getCategories().size()) {
                                    System.out.println();
                                    System.out.println(RED + "Invalid category number." + RESET);
                                    continue;
                                }

                                while (true) {

                                    System.out.println();
                                    System.out.print(CYAN + "Enter the subcategory name (or type 'back' to return): " + RESET);
                                    String subCategoryName = scanner.nextLine();

                                    if (subCategoryName.equalsIgnoreCase("back")) {
                                        break;
                                    }


                                    try {
                                        categoryManager.addCustomSubCategoryInCategory
                                                (categoryManager.getCategories().get(index - 1).getType(), subCategoryName);
                                        System.out.println();
                                        System.out.println(GREEN + "Sub-category added successfully." + RESET);
                                        promptBack();
                                        break;

                                    } catch (SubCategoryExistsException e) {
                                        System.out.println();
                                        System.out.println(RED + e.getMessage() + RESET);
                                        continue;
                                    }
                                }
                            }
                            catch (NumberFormatException e) {
                                System.out.println();
                                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
                                continue;
                            } catch (CategoryDoesnotExistsException e) {
                                System.out.println();
                                System.out.println(RED + e.getMessage() + RESET);
                            }

                            break;
                        }
                        break;

                    case 5:
                        while (true) {
                            System.out.println();
                            System.out.println(categoryManager.showCategories());
                            System.out.println();
                            System.out.print(CYAN + "\nEnter the number of the category to delete a subcategory (or type 'back' to return): " + RESET);
                            String input = scanner.nextLine();
                            if (input.equalsIgnoreCase("back")) break;
                            try {
                                int index = Integer.parseInt(input);
                                if (index < 1 || index > categoryManager.getCategories().size()) {
                                    System.out.println();
                                    System.out.println(RED + "Invalid category number." + RESET);
                                    continue;
                                }

                                Category category = categoryManager.getCategories().get(index - 1);


                                if (category.getSubcategories().isEmpty()) {
                                    System.out.println();
                                    System.out.println(category.showSubCategories());
                                    promptBack();
                                    break;
                                }


                                while (true) {

                                    System.out.println();
                                    System.out.println(category.showSubCategories());
                                    System.out.print(CYAN + "Enter the number of the subcategory to delete (or type 'back' to return): " + RESET);
                                    String subInput = scanner.nextLine();

                                    if (subInput.equalsIgnoreCase("back")) break;

                                    try {
                                        int subIndex = Integer.parseInt(subInput);
                                        if (subIndex < 1 || subIndex > category.getSubcategories().size()) {
                                            System.out.println();
                                            System.out.println(RED + "Invalid subcategory number." + RESET);
                                            continue;
                                        }
                                        String subCategoryToDelete = category.getSubcategories().get(subIndex - 1);
                                        categoryManager.deleteSubCategory(category.getType(), subCategoryToDelete);
                                        System.out.println();
                                        System.out.println(GREEN + "Sub-category deleted successfully." + RESET);
                                        promptBack();
                                        break;
                                    } catch (NumberFormatException e) {
                                        System.out.println();
                                        System.out.println(RED + "Invalid input. Please enter a number." + RESET);
                                    } catch (CategoryDoesnotExistsException | SubCategoryDoesNotExistException e) {
                                        System.out.println();
                                        System.out.println(RED + e.getMessage() + RESET);
                                    }
                                }
                                break;

                            } catch (NumberFormatException e) {
                                System.out.println();
                                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
                            }
                        }
                        break;

                    case 6:
                        while (true) {
                            System.out.println();
                            System.out.println(categoryManager.showCategories());
                            System.out.println();
                            System.out.print(CYAN + "Enter the number of the category to see subcategories (or type 'back' to return): " + RESET);
                            String input = scanner.nextLine();
                            if (input.equalsIgnoreCase("back")) break;
                            try {
                                int index = Integer.parseInt(input);
                                if (index < 1 || index > categoryManager.getCategories().size()) {
                                    System.out.println();
                                    System.out.println(RED + "Invalid category number." + RESET);
                                    continue;
                                }
                                System.out.println();
                                System.out.println(categoryManager.showSubCategories(
                                        categoryManager.getCategories().get(index - 1)));
                                promptBack();
                                break;
                            } catch (NumberFormatException e) {
                                System.out.println();
                                System.out.println(RED + "Invalid input. Please enter a number." + RESET);
                            }
                        }
                        break;

                    case 7:
                        System.out.println();
                        System.out.println(categoryManager.showCategoriesWithSubCategories());
                        promptBack();
                        break;
                    case 8:
                        try {
                            categoryManager.writeFile();
                        } catch (Exception e) {
                            System.out.println();
                            System.out.println(RED + "Error saving data: " + e.getMessage() + RESET);
                        }
                        return;
                    default:
                        System.out.println();
                        System.out.println(RED + "Invalid choice! please try again." + RESET);
                        break;
                }
            }
            else {
                System.out.println();
                System.out.println(RED + "⚠ Invalid choice! Try again." + RESET);
                scanner.nextLine();
            }
        }
    }

    private void promptBack() {
        System.out.println();
        System.out.print(BLUE+"Press enter to continue..."+RESET);
        scanner.nextLine();
    }
}