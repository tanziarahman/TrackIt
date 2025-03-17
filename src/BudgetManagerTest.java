import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Month;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class BudgetManagerTest {

    private BudgetManager budgetManager;
    private CategoryManager categoryManager;
    private IBudgetStorage storage;
    private Month month;
    private Year year;


    @BeforeEach
    void setUp() {
        categoryManager = new CategoryManager();
        storage = new BudgetCsvStorage();
        month = Month.MARCH;
        year = Year.of(2026);

        budgetManager = new BudgetManager(categoryManager, month, year, storage);
    }


    @Test
    void testInitialBudgetsAreEmpty() {
        assertTrue(budgetManager.getBudgets().isEmpty());
    }

    @Test
    void testSetMonthlyIncome() {
        budgetManager.SetMonthlyIncome(7000.0);
        assertEquals(7000.0, budgetManager.getMonthlyIncome());
    }

    @Test
    void testAddToIncome() {
        budgetManager.addToIncome(1000.0);
        assertEquals(8000.0, budgetManager.getMonthlyIncome());
    }

    @Test
    void testSetCategoryBudget_success() throws Exception {
        categoryManager.addCategory("Shopping");

        budgetManager.setCategoryBudget("Shopping", 2000.0);
        assertTrue(budgetManager.budgetExists("Shopping"));
        assertEquals(2000.0, budgetManager.getBudgetAmountForCategory("Shopping"));
    }

    @Test
    void testSetCategoryBudget_Failure_BudgetAlreadyExists() {
        categoryManager.addCategory("Shopping");

        assertThrows(BudgetExistsException.class, () -> {
            budgetManager.setCategoryBudget("Shopping", 2500.0);
        });
    }


    @Test
    void testDeleteCategoryBudget_Success() throws Exception {

        budgetManager.deleteCategoryBudget("shopping");
        assertFalse(budgetManager.budgetExists("shopping"));
    }

    @Test
    void testEditCategoryBudget_Success() throws Exception {

        categoryManager.addCategory("Health");

        budgetManager.setCategoryBudget("Health", 2000.0);

        budgetManager.editCategoryBudget("Health", 2500.0);
        assertEquals(2500.0, budgetManager.getBudgetAmountForCategory("Health"));
    }

    @Test
    void testGetTotalBudget()  {
        assertEquals(2500.0, budgetManager.getTotalBudget());
    }

    @Test
    void testCheckBudgetLimit_NoException() throws Exception {
        categoryManager.addCategory("shopping");
        budgetManager.setCategoryBudget("shopping", 9000.0);
        assertThrows(BudgetExceededIncomeException.class, () -> {
            budgetManager.checkBudgetLimit();
        });
    }
}