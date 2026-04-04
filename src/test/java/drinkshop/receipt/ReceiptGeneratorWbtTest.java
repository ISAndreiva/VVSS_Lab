package drinkshop.receipt;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * WBT (White-Box Testing) tests for ReceiptGenerator.generate(Order, List&lt;Product&gt;).
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("unused")
class ReceiptGeneratorWbtTest {

    // ── shared test fixtures ──────────────────────────────────────────────────

    private static final LocalDate TEST_DATE = LocalDate.of(2026, 4, 1);
    private static final CategorieBautura CAT  = new CategorieBautura(1, "Bauturi_Calde");
    private static final TipBautura      TIP  = new TipBautura(1, "Cafea");

    /** Valid product – id=1, price=3.5 */
    private static final Product CAFEA =
            new Product(1, "Cafea", 3.5, CAT, TIP);

    /** Product with invalid id=-1 (not present in products list) */
    private static final Product CAFEA_CU_LAPTE =
            new Product(-1, "Cafea cu lapte", 4.5, CAT, TIP);

    // ── Path P01 & P02 – null-input guards ───────────────────────────────────

    @Nested
    @DisplayName("Null-input paths (F02_P01, F02_P02)")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class NullInputPaths {

        /**
         * F02_TC01 – Path F02_P01: 1 – 2(F) – 12
         * Decision node 2 (o == null) → False branch to exit.
         * Expected: NullPointerException.
         */
        @Test
        @org.junit.jupiter.api.Order(1)
        @DisplayName("F02_TC01 – Path P01: o == null → NullPointerException")
        void tc01_orderNull_throwsNPE() {
            assertThrows(NullPointerException.class,
                    () -> ReceiptGenerator.generate(null, List.of()));
        }

        /**
         * F02_TC02 – Path F02_P02: 1 – 2(T) – 3 – 4(F) – 12
         * Decision node 4 (products == null) → False branch to exit.
         * Expected: NullPointerException.
         */
        @Test
        @org.junit.jupiter.api.Order(2)
        @DisplayName("F02_TC02 – Path P02: products == null → NullPointerException")
        void tc02_productsNull_throwsNPE() {
            drinkshop.domain.Order o = new drinkshop.domain.Order(6, TEST_DATE);
            assertThrows(NullPointerException.class,
                    () -> ReceiptGenerator.generate(o, null));
        }
    }

    // ── Path P03 – both collections empty (neither loop executes) ────────────

    @Nested
    @DisplayName("Empty-collections path (F02_P03)")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class EmptyCollectionPath {

        /**
         * F02_TC04 – Path F02_P03: 1 – 2(T) – 3 – 4(T) – 5 – 6(F) – 8 – 9(F) – 11 – 12
         * Products list is empty → node 6 (products-loop condition) evaluates False immediately.
         * Order has no items → node 9 (items-loop condition) evaluates False immediately.
         * Expected: receipt header + footer only.
         */
        @Test
        @org.junit.jupiter.api.Order(3)
        @DisplayName("F02_TC04 – Path P03: empty products + empty items → empty receipt")
        void tc04_emptyProductsAndItems_emptyReceipt() {
            drinkshop.domain.Order o = new drinkshop.domain.Order(5, TEST_DATE); // 0 items, totalPrice defaults to 0.0
            String expected =
                    "===== BON FISCAL =====\n" +
                    "Comanda #5\n" +
                    "---------------------\n" +
                    "TOTAL: 0.0 RON\n" +
                    "=====================\n";

            String actual = ReceiptGenerator.generate(o, List.of());

            assertEquals(expected, actual);
        }
    }

    // ── Path P05 – products loop executes, items loop skipped ────────────────

    @Nested
    @DisplayName("Products-loop-only path (F02_P05)")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ProductsLoopOnlyPath {

        /**
         * F02_TC05 – Path F02_P05: 1 – 2(T) – 3 – 4(T) – 5 – 6(T) – 7 – 6(F) – 8 – 9(F) – 11 – 12
         * Products list is non-empty → node 6 (True) loops through it building mapProducts.
         * Order has no items → node 9 (False) skips the items loop.
         * Expected: receipt header + footer only (mapProducts is built but not used).
         */
        @Test
        @org.junit.jupiter.api.Order(4)
        @DisplayName("F02_TC05 – Path P05: non-empty products + empty items → header/footer only")
        void tc05_hasProductsNoItems_headerFooterOnly() {
            drinkshop.domain.Order o = new drinkshop.domain.Order(5, TEST_DATE); // 0 items
            String expected =
                    "===== BON FISCAL =====\n" +
                    "Comanda #5\n" +
                    "---------------------\n" +
                    "TOTAL: 0.0 RON\n" +
                    "=====================\n";

            String actual = ReceiptGenerator.generate(o, List.of(CAFEA));

            assertEquals(expected, actual);
        }
    }

    // ── Path P04+P05 – both loops execute ────────────────────────────────────

    @Nested
    @DisplayName("Both-loops path (F02_P04 + F02_P05) and exception variant")
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class BothLoopsPaths {

        /**
         * F02_TC03 – Paths F02_P04 + F02_P05 combined (TestLink P233-60).
         * Products list non-empty → node 6 (True) executes once (builds mapProducts).
         * Order has items with matching product IDs → node 9 (True) executes once.
         * Expected: full valid receipt.
         *
         * Pre-condition (from TestLink P233-60):
         *   Order id=5, date=2026-04-01,
         *   items=[OrderItem(Cafea, qty=2)], totalPrice=7.0
         */
        @Test
        @org.junit.jupiter.api.Order(5)
        @DisplayName("F02_TC03 – Paths P04+P05: valid inputs → full receipt (TestLink P233-60)")
        void tc03_matchingProductsAndItems_fullReceipt() {
            drinkshop.domain.Order o = new drinkshop.domain.Order(5, TEST_DATE, List.of(new OrderItem(CAFEA, 2)), 7.0);
            String expected =
                    "===== BON FISCAL =====\n" +
                    "Comanda #5\n" +
                    "Cafea: 3.5 x 2 = 7.0 RON\n" +
                    "---------------------\n" +
                    "TOTAL: 7.0 RON\n" +
                    "=====================\n";

            String actual = ReceiptGenerator.generate(o, List.of(CAFEA));

            assertEquals(expected, actual);
        }

        /**
         * F02_TC06 – Exception variant of path P04 (TestLink P233-61).
         * Products loop executes for Cafea (id=1).
         * Items loop processes first item (id=1) fine, then encounters item with product id=-1
         * which is absent from mapProducts → p == null → NullPointerException.
         *
         * Pre-condition (from TestLink P233-61):
         *   Order id=6, date=2026-04-01,
         *   items=[OrderItem(Cafea id=1, qty=1), OrderItem(CafeaCuLapte id=-1, qty=1)],
         *   totalPrice=8.0; products=[Cafea id=1] only.
         */
        @Test
        @org.junit.jupiter.api.Order(6)
        @DisplayName("F02_TC06 – P04 exception: item product not in map → NullPointerException (TestLink P233-61)")
        void tc06_itemProductMissingFromMap_throwsNPE() {
            drinkshop.domain.Order o = new drinkshop.domain.Order(6, TEST_DATE);
            o.addItem(new OrderItem(CAFEA, 1));
            o.addItem(new OrderItem(CAFEA_CU_LAPTE, 1));
            o.setTotalPrice(8.0);

            List<Product> products = List.of(CAFEA); // id=-1 not present

            assertThrows(NullPointerException.class,
                    () -> ReceiptGenerator.generate(o, products));
        }
    }
}
