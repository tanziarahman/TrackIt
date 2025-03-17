import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.Calendar;

public class TransactionUI {
    private final TransactionManager transactionManager;
    private final BudgetManager budgetManager;
    private final CategoryManager categoryManager;
    private final Scanner scanner;

    private static final String RESET = "\u001B[0m";
    private static final String BLUE = "\u001B[34m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";

    public TransactionUI(TransactionManager transactionManager, BudgetManager budgetManager, CategoryManager categoryManager, Scanner scanner) {
        this.transactionManager = transactionManager;
        this.budgetManager = budgetManager;
        this.categoryManager = categoryManager;
        this.scanner = scanner;
    }

    public void manageTransactions() {
        while (true) {
            System.out.println();
            System.out.println(CYAN + "\n💸 Transaction Management ["
                    + StringFormatter.capitalizeFirstLetter(TransactionManager.getCurrentMonth().name())
                    + " " + TransactionManager.getCurrentYear().getValue() + "]" + RESET);
            System.out.println(GREEN + "[1] Add Transaction");
            System.out.println("[2] View Transactions");
            System.out.println("[3] Edit Transaction");
            System.out.println("[4] Delete Transaction");
            System.out.println("[5] Go Back to Main Menu" + RESET);
            System.out.println();
            System.out.print(YELLOW + "Choose an option: " + RESET);
            if (!scanner.hasNextInt()) {
                System.out.println();
                System.out.println(RED + " Invalid input! Please enter a number." + RESET);
                scanner.next();
                continue;
            }

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addTransaction();
                    System.out.println();
                    System.out.print(BLUE + "Press Enter to continue..." + RESET);
                    scanner.nextLine();
                    break;
                case 2:
                    viewTransactions();
                    System.out.println();
                    System.out.print(BLUE + "Press Enter to continue..." + RESET);
                    scanner.nextLine();
                    break;
                case 3:
                    editTransaction();
                    System.out.println();
                    System.out.print(BLUE + "Press Enter to continue..." + RESET);
                    scanner.nextLine();
                    break;
                case 4:
                    deleteTransaction();
                    System.out.println();
                    System.out.print(BLUE+ "Press Enter to continue..." + RESET);
                    scanner.nextLine();
                    break;
                case 5:
                    return;
                default:
                    System.out.println();
                    System.out.println(RED + " Invalid choice! Try again." + RESET);

            }
        }
    }

    private void addTransaction() {
        try {
            int categoryIndex = getUserCategoryChoice();
            if (categoryIndex == -1) return;

            String category = categoryManager.getCategories().get(categoryIndex).getType();
            if (!budgetManager.budgetExists(category)) {
                System.out.println();
                System.out.println(RED + " No budget exists for category: " + category +". Please set budget first to continue transactions."+ RESET);
                return;
            }

            int subCategoryIndex = getUserSubCategoryChoice(category);
            if (subCategoryIndex == -1) return;
            String subCategory = categoryManager.getCategories().get(categoryIndex).getSubcategories().get(subCategoryIndex);

            double amount = getValidAmount();
            if (amount == -1) return;

            Date date = getValidDate();
            if (date == null) return;

            System.out.println();
            System.out.print(CYAN + "Enter description (optional, press Enter to skip or write 'back' to return): " + RESET);
            String description = scanner.nextLine().trim();
            if (description.isEmpty()) {
                description = "No description";
            }
            if (description.equalsIgnoreCase("back")) return;
            System.out.println();

            transactionManager.addTransaction(amount, category, subCategory, date, description);
            System.out.println();
            System.out.println(GREEN + "Transaction added successfully!" + RESET);
            System.out.println();
        } catch (Exception e) {
            System.out.println();
            System.out.println(RED+ e.getMessage() + RESET);
        }

    }

    private void viewTransactions() {
        System.out.println();
        String summary = transactionManager.getTransactionsSummary();
        if (summary.isEmpty()) {
            System.out.println(RED + "No transactions found for the selected month and year." + RESET);
        } else {
            System.out.println("══════════════════════════════════════");
            System.out.println(PURPLE+"Transaction Summary For " +TransactionManager.getCurrentMonth()+ " "+TransactionManager.getCurrentYear()+RESET);
            System.out.println("══════════════════════════════════════");
            System.out.println();
            System.out.println(summary);
        }
    }

    private void editTransaction() {
        if (transactionManager.getTransactionsSummary().isEmpty()) {
            System.out.println();
            System.out.println(RED + "No transactions found for the selected month and year." + RESET);
            System.out.print(YELLOW + "Press enter to return to the menu..." + RESET);
            scanner.nextLine();
            return;
        } else {
            viewTransactions();
            while (true) {
                try {
                    System.out.println();
                    System.out.print(CYAN + "Enter transaction ID to edit (or type 'back' to return): " + RESET);
                    System.out.println();
                    String input = scanner.nextLine().trim();
                    if (input.equalsIgnoreCase("back")) return;

                    int id = Integer.parseInt(input);
                    if (!transactionManager.transactionExists(id)) {
                        System.out.println();
                        System.out.println(RED + " Transaction ID not found! Please enter a valid ID." + RESET);
                        continue;
                    }

                    int categoryIndex = getUserCategoryChoice();
                    if (categoryIndex == -1) return;
                    String category = categoryManager.getCategories().get(categoryIndex).getType();

                    if (!budgetManager.budgetExists(category)) {
                        System.out.println();
                        System.out.println(RED + " No budget exists for category: " + category + RESET);
                        return;
                    }

                    int subCategoryIndex = getUserSubCategoryChoice(category);
                    if (subCategoryIndex == -1) return;
                    String subCategory = categoryManager.getCategories().get(categoryIndex).getSubcategories().get(subCategoryIndex);

                    double amount = getValidAmount();
                    if (amount == -1) return;

                    Date date = getValidDate();
                    if (date == null) return;

                    System.out.println();
                    System.out.print(CYAN + "Enter description (optional, press Enter to skip or write 'back' to return): " + RESET);
                    String description = scanner.nextLine().trim();
                    if (description.isEmpty()) {
                        description = "No description";
                    }
                    if (description.equalsIgnoreCase("back")) return;

                    transactionManager.editTransaction(id, amount, category, subCategory, date, description);
                    System.out.println();
                    System.out.println(GREEN + "Transaction edited successfully!" + RESET);
                    break;

                } catch (NumberFormatException e) {
                    System.out.println(RED + "Invalid input! Please enter a valid transaction ID." + RESET);
                } catch (Exception e) {
                    System.out.println();
                    System.out.println(RED + e.getMessage() + RESET);
                }
            }
        }
    }

    private void deleteTransaction() {
        if (transactionManager.getTransactionsSummary().isEmpty()) {
            System.out.println();
            System.out.println(RED + "No transactions found for the selected month and year." + RESET);
            System.out.print(YELLOW + "Press enter to return to the menu..." + RESET);
            scanner.nextLine();
            return;
        }

        while (true) {
            try {
                viewTransactions();
                System.out.println();
                System.out.print(CYAN + "Enter transaction ID to delete (or type 'back' to return): " + RESET);
                String input = scanner.nextLine().trim();
                if (input.equalsIgnoreCase("back")) return;

                int id = Integer.parseInt(input);
                if (!transactionManager.transactionExists(id)) {
                    System.out.println();
                    System.out.println(RED + " Transaction ID not found! Please enter a valid ID." + RESET);
                    continue;
                }

                transactionManager.deleteTransaction(id);
                System.out.println();
                System.out.println(GREEN + "Transaction deleted successfully!" + RESET);
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Invalid input! Please enter a valid transaction ID." + RESET);
            }
            catch (Exception e) {
                System.out.println();
                System.out.println(RED + e.getMessage() + RESET);

            }
        }
    }

    private double getValidAmount() {
        while (true) {
            System.out.println();
            System.out.print(CYAN + "Enter transaction amount (or type 'back' to return): " + RESET);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("back")) return -1;

            try {
                double amount = Double.parseDouble(input);
                if (amount > 0) return amount;
                System.out.println();
                System.out.println(RED + "Invalid input! Please enter a valid amount." + RESET);

            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "Invalid input! Please enter a valid amount." + RESET);

            }
        }
    }

    private Date getValidDate() {
        while (true) {
            System.out.println();
            System.out.print(CYAN + "Enter date (YYYY-MM-DD) or press Enter to use today's date (or type 'back' to return): " + RESET);
            String dateString = scanner.nextLine().trim();

            if (dateString.equalsIgnoreCase("back")) return null;

            try {
                if (dateString.isEmpty()) {
                    Date today = new Date();
                    if (isValidDate(today)) {
                        System.out.println();
                        System.out.println(GREEN + "Using today's date: " + new SimpleDateFormat("yyyy-MM-dd").format(today) + RESET);
                        return today;
                    } else {
                        System.out.println();
                        System.out.println(RED + "Invalid date! Today's date is outside the selected month/year. Please enter a valid date or change date." + RESET);
                    }
                } else {
                    // Split date into parts and manually check if month and day are valid
                    String[] dateParts = dateString.split("-");
                    if (dateParts.length != 3) {
                        System.out.println();
                        System.out.println(RED +"Invalid date format! Please enter in (YYYY-MM-DD) format." + RESET);
                        continue;
                    }

                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int day = Integer.parseInt(dateParts[2]);

                    if (month < 1 || month > 12) {
                        System.out.println();
                        System.out.println(RED + "Invalid month! Month should be between 1 and 12." + RESET);
                        continue;
                    }

                    if (day < 1 || day > 31) {
                        System.out.println();
                        System.out.println(RED + "Invalid day! Day should be between 1 and 31, depending on the month." + RESET);
                        continue;
                    }

                    // Create date object
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false); // Ensures strict validation
                    Date date = sdf.parse(dateString);

                    if (isValidDate(date)) {
                        return date;
                    } else {
                        System.out.println();
                        System.out.println(RED +"Invalid date! Please enter a date within the selected month and year." + RESET);
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "Invalid numeric input! Please enter numbers only in (YYYY-MM-DD) format." + RESET);
            } catch (Exception e) {
                System.out.println();
                System.out.println(RED + "Invalid date format! Please enter again in (YYYY-MM-DD) format." + RESET);
            }
        }
    }


    private boolean isValidDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH) + 1 == TransactionManager.getCurrentMonth().getValue()
                && cal.get(Calendar.YEAR) == TransactionManager.getCurrentYear().getValue();
    }
    private int getUserCategoryChoice() {
        List<Category> categories = categoryManager.getCategories();
        if (categories.isEmpty()) {
            System.out.println();
            System.out.println(RED + "No categories available. Please add categories first." + RESET);
            return -1;
        }
        System.out.println();
        System.out.println(BLUE + "\nSelect a Category from the following list:" + RESET);
        for (int i = 0; i < categories.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + categories.get(i).getType());
        }
        while (true) {
            System.out.println();
            System.out.print(CYAN + "Enter category number (or type 'back' to return): " + RESET);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("back")) return -1;

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= categories.size()) return choice - 1;
                else System.out.println();System.out.println(RED + "Invalid choice! Select a valid number." + RESET);
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "Invalid input! Please enter a valid number." + RESET);
            }
        }
    }

    private int getUserSubCategoryChoice(String category) {
        Category selectedCategory = categoryManager.getCategories().stream()
                .filter(c -> c.getType().equals(category))
                .findFirst()
                .orElse(null);

        if (selectedCategory == null || selectedCategory.getSubcategories().isEmpty()) {
            System.out.println();
            System.out.println(RED + " No sub-categories available. Please add sub-categories first." + RESET);
            return -1;
        }

        List<String> subCategories = selectedCategory.getSubcategories();
        System.out.println();
        System.out.println(BLUE + "\nSelect a Sub-Category from " + category + ":" + RESET);
        for (int i = 0; i < subCategories.size(); i++) {
            System.out.println("[" + (i + 1) + "] " + subCategories.get(i));
        }
        while (true) {
            System.out.println();
            System.out.print(CYAN + "Enter sub-category number (or type 'back' to return): " + RESET);
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("back")) return -1;

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= subCategories.size()) {
                    return choice - 1;
                } else {
                    System.out.println();
                    System.out.println(RED + "Invalid choice! Select a valid number." + RESET);
                }
            } catch (NumberFormatException e) {
                System.out.println();
                System.out.println(RED + "Invalid input! Please enter a valid number." + RESET);
            }
        }
    }

}

