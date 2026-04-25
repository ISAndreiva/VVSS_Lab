package drinkshop.receipt;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * WBT (White-Box Testing) tests for ReceiptGenerator.generate(Order, List<Product>).
 *
 * Control Flow Graph path coverage based on the 5 basis paths (CC = 5):
 *   F02_P01 : 1 – 2(F) – 12                                    o == null
 *   F02_P02 : 1 – 2(T) – 3 – 4(F) – 12                        products == null
 *   F02_P03 : 1 – 2(T) – 3 – 4(T) – 5 – 6(F) – 8 – 9(F) – 11 – 12   both collections empty
 *   F02_P04 : 1 – 2(T) – 3 – 4(T) – 5 – 6(F) – 8 – 9(T) – 10 – … items loop, no products
 *   F02_P05 : 1 – 2(T) – 3 – 4(T) – 5 – 6(T) – 7 – 6(F) – 8 – 9(F) – 11 – 12  products loop only
 */
@DisplayName("WBT - F02 GenerareBon: ReceiptGenerator.generate()")
@Tag("wbt")
class ReceiptGeneratorWbtTest {

    // ── shared test fixtures ──────────────────────────────────────────────────

    private static final LocalDate TEST_DATE = LocalDate.of(2026, 4, 1);
    private static final CategorieBautura CAT = new CategorieBautura(1, "Bauturi_Calde");
    private static final TipBautura TIP = new TipBautura(1, "Cafea");

    /** Valid product – id=1, price=3.5 */
    private static final Product CAFEA =
            new Product(1, "Cafea", 3.5, CAT, TIP);

    /** Product with invalid id=-1 (not present in products list) */
    private static final Product CAFEA_CU_LAPTE =
            new Product(-1, "Cafea cu lapte", 4.5, CAT, TIP);

    // ── Valid paths ───────────────────────────────────────────────────────────

    /**
     * Covers paths F02_P03, F02_P04, F02_P05 (all non-exception paths).
     *
     * TC04 – Path P03: empty products + empty items → header/footer only
     * TC05 – Path P05: non-empty products + empty items → header/footer only
     * TC03 – Paths P04+P05: valid inputs → full receipt (TestLink P233-60)
     */
    @Test
    @DisplayName("F02 – Valid paths (P03, P04, P05): generate() returns correct receipt")
    void validCases() {
        // TC04 – Path P03: empty products, empty items
        Order o1 = new Order(5, TEST_DATE);
        String expected03 =
                "===== BON FISCAL =====\n" +
                        "Comanda #5\n" +
                        "---------------------\n" +
                        "TOTAL: 0.0 RON\n" +
                        "=====================\n";
        assertEquals(expected03, ReceiptGenerator.generate(o1, List.of()));

        // TC05 – Path P05: non-empty products, empty items
        Order o2 = new Order(5, TEST_DATE);
        assertEquals(expected03, ReceiptGenerator.generate(o2, List.of(CAFEA)));

        // TC03 – Paths P04+P05: matching products and items → full receipt
        Order o3 = new Order(5, TEST_DATE, List.of(new OrderItem(CAFEA, 2)), 7.0);
        String expected04 =
                "===== BON FISCAL =====\n" +
                        "Comanda #5\n" +
                        "Cafea: 3.5 x 2 = 7.0 RON\n" +
                        "---------------------\n" +
                        "TOTAL: 7.0 RON\n" +
                        "=====================\n";
        assertEquals(expected04, ReceiptGenerator.generate(o3, List.of(CAFEA)));
    }

    // ── Invalid paths ─────────────────────────────────────────────────────────

    /**
     * Covers paths F02_P01, F02_P02 and the exception variant of F02_P04.
     *
     * TC01 – Path P01: o == null → NullPointerException
     * TC02 – Path P02: products == null → NullPointerException
     * TC06 – P04 exception: item product not in map → NullPointerException (TestLink P233-61)
     */
    @Test
    @DisplayName("F02 – Invalid paths (P01, P02, P04 exception): generate() throws NullPointerException")
    void invalidCases() {
        // TC01 – Path P01: null order
        assertThrows(NullPointerException.class,
                () -> ReceiptGenerator.generate(null, List.of()));

        // TC02 – Path P02: null products
        Order o1 = new Order(6, TEST_DATE);
        assertThrows(NullPointerException.class,
                () -> ReceiptGenerator.generate(o1, null));

        // TC06 – Path P04 exception: item with id=-1 missing from products map
        Order o2 = new Order(6, TEST_DATE);
        o2.addItem(new OrderItem(CAFEA, 1));
        o2.addItem(new OrderItem(CAFEA_CU_LAPTE, 1));
        o2.setTotalPrice(8.0);
        assertThrows(NullPointerException.class,
                () -> ReceiptGenerator.generate(o2, List.of(CAFEA)));
    }
}