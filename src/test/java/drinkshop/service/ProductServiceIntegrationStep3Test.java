package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Step 3 – integrate R (real InMemoryProductRepository) with S + V.
 * All three components are real: S (ProductService), V (ProductValidator), R (InMemoryProductRepository).
 * Scenario 1: V <--- S ---> R, top-down breadth first.
 */
@DisplayName("Step 3 – Integration S + real V + real R")
@Tag("integration")
class ProductServiceIntegrationStep3Test {

    private static final CategorieBautura CATEGORY = new CategorieBautura(1, "Cafea");
    private static final TipBautura TYPE = new TipBautura(1, "Calda");

    private ProductService service;
    private InMemoryProductRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryProductRepository();
        service = new ProductService(repo, new ProductValidator());
    }

    /**
     * TC1 – F01: addProduct valid → product is actually saved in real repository.
     */
    @Test
    @DisplayName("TC1 – F01: addProduct valid → findById returns saved product")
    void tc1_addProduct_valid_productPersisted() {
        // Act
        service.addProduct(1, "Cafea", 5.0, CATEGORY, TYPE);

        // Assert – real repo has the product
        Product saved = service.findById(1);
        assertNotNull(saved);
        assertEquals(1, saved.getId());
        assertEquals("Cafea", saved.getNume());
        assertEquals(5.0, saved.getPret());
    }

    /**
     * TC2 – F01: addProduct with invalid data → ValidationException, real repo stays empty.
     */
    @Test
    @DisplayName("TC2 – F01: addProduct invalid → ValidationException, repo stays empty")
    void tc2_addProduct_invalid_repoEmpty() {
        // Act + Assert
        assertThrows(ValidationException.class,
                () -> service.addProduct(-1, "Cafea", 5.0, CATEGORY, TYPE));

        // Verify nothing was saved
        assertTrue(service.getAllProducts().isEmpty());
    }

    /**
     * TC3 – F02: updateProduct → updated values are reflected in real repository.
     */
    @Test
    @DisplayName("TC3 – F02: updateProduct → real repo reflects new values")
    void tc3_updateProduct_valid_changesReflected() {
        // Arrange – first save it
        service.addProduct(1, "Cafea", 5.0, CATEGORY, TYPE);

        // Act
        service.updateProduct(1, "Cafea Speciala", 7.5, CATEGORY, TYPE);

        // Assert
        Product updated = service.findById(1);
        assertNotNull(updated);
        assertEquals("Cafea Speciala", updated.getNume());
        assertEquals(7.5, updated.getPret());
    }

    /**
     * TC4 – F02: updateProduct for non-existent product → exception, real repo unchanged.
     */
    @Test
    @DisplayName("TC4 – F02: updateProduct not found → exception, repo unchanged")
    void tc4_updateProduct_notFound_exceptionAndRepoUnchanged() {
        // Arrange – save one product
        service.addProduct(1, "Cafea", 5.0, CATEGORY, TYPE);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(99, "Altceva", 3.0, CATEGORY, TYPE));

        // Repo still has only the original product
        assertEquals(1, service.getAllProducts().size());
        assertEquals("Cafea", service.findById(1).getNume());
    }

    // -------------------------------------------------------------------------
    // Helper – real in-memory repository (no file I/O)
    // -------------------------------------------------------------------------
    private static final class InMemoryProductRepository extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }
}
