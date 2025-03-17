import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.Year;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TransactionManagerTest {
    private TransactionManager transactionManager;
    private TransactionStorage transactionStorage;

    @BeforeEach
    void setUp() throws IOException {
        transactionStorage = new FileTransactionStorage();
        transactionManager = new TransactionManager(transactionStorage, null, Month.JANUARY, Year.of(2024));
    }

    @Test
    void testAddTransaction_Success() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-10");
        Transaction transaction = transactionManager.addTransaction(100.0, "Food", "Groceries", date, "Bought vegetables");

        assertNotNull(transaction);
        assertEquals(1, transaction.getTransactionID());
        assertEquals(100.0, transaction.getAmount());
        assertEquals("Food", transaction.getCategory());
        assertEquals("Groceries", transaction.getSubCategory());
        assertEquals("Bought vegetables", transaction.getDescription());
    }

    @Test
    void testAddTransaction_InvalidDate() {
        assertThrows(IOException.class, () -> {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-02-10"); // Outside set month/year
            transactionManager.addTransaction(100.0, "Food", "Groceries", date, "Bought vegetables");
        });
    }

    @Test
    void testEditTransaction_Success() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-10");
        transactionManager.addTransaction(100.0, "Food", "Groceries", date, "Bought vegetables");

        Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-15");
        transactionManager.editTransaction(1, 150.0, "Food", "Dinner", newDate, "Dinner with family");

        Transaction editedTransaction = transactionManager.getTransactions().get(0);
        assertEquals(150.0, editedTransaction.getAmount());
        assertEquals("Dinner", editedTransaction.getSubCategory());
        assertEquals("Dinner with family", editedTransaction.getDescription());
    }

    @Test
    void testEditTransaction_NotFound() {
        assertThrows(IOException.class, () -> {
            Date newDate = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-15");
            transactionManager.editTransaction(1, 150.0, "Food", "Dinner", newDate, "Dinner with family");
        });
    }

    @Test
    void testDeleteTransaction_Success() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-10");
        transactionManager.addTransaction(100.0, "Food", "Groceries", date, "Bought vegetables");

        transactionManager.deleteTransaction(1);
        assertEquals(0, transactionManager.getTransactions().size());
    }

    @Test
    void testDeleteTransaction_NotFound() {
        assertThrows(IOException.class, () -> transactionManager.deleteTransaction(1));
    }

    @Test
    void testGetTransactionsSummary() throws Exception {
        Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-10");
        Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-15");

        transactionManager.addTransaction(100.0, "Food", "Groceries", date1, "Bought vegetables");
        transactionManager.addTransaction(200.0, "Food", "Dinner", date2, "Dinner with friends");

        String summary = transactionManager.getTransactionsSummary();
        assertTrue(summary.contains("Groceries"));
        assertTrue(summary.contains("Dinner"));
    }

    @Test
    void testTransactionExists() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-10");
        transactionManager.addTransaction(100.0, "Food", "Groceries", date, "Bought vegetables");

        assertTrue(transactionManager.transactionExists(1));
        assertFalse(transactionManager.transactionExists(2));
    }

    @Test
    void testTotalTransactionInCategory() throws Exception {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2024-01-10");
        transactionManager.addTransaction(100.0, "Food", "Groceries", date, "Bought vegetables");
        transactionManager.addTransaction(50.0, "Food", "Snacks", date, "Bought snacks");

        assertEquals(150.0, transactionManager.totalTransactionInACategory("Food"));
    }
}

