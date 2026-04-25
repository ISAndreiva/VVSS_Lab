package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Step 2 – integrate V (real ProductValidator) with S (ProductService).
 * R (Repository) is still mocked.
 * Scenario 1: V <--- S ---> R, top-down breadth first.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Step 2 – Integration S + real V (mock R)")
@Tag("integration")
class ProductServiceIntegrationStep2Test {

    private static final CategorieBautura CATEGORY = new CategorieBautura(1, "Cafea");
    private static final TipBautura TYPE = new TipBautura(1, "Calda");

    @Mock
    private Repository<Integer, Product> mockRepo;

    private ProductService service;

    @BeforeEach
    void setUp() {
        // Real validator – V is integrated here
        service = new ProductService(mockRepo, new ProductValidator());
    }

    /**
     * TC1 – F01: addProduct with valid data.
     * Real validator passes → mock repo.save called.
     */
    @Test
    @DisplayName("TC1 – F01: addProduct valid → real validator passes, repo.save called")
    void tc1_addProduct_valid_saveCalled() {
        // Arrange
        Product expected = new Product(1, "Cafea", 5.0, CATEGORY, TYPE);
        when(mockRepo.save(any(Product.class))).thenReturn(expected);

        // Act
        service.addProduct(1, "Cafea", 5.0, CATEGORY, TYPE);

        // Assert + Verify
        verify(mockRepo, times(1)).save(any(Product.class));
    }

    /**
     * TC2 – F01: addProduct with invalid id (negative).
     * Real validator throws ValidationException → mock repo.save NOT called.
     */
    @Test
    @DisplayName("TC2 – F01: addProduct invalid id → real validator throws, repo.save NOT called")
    void tc2_addProduct_invalidId_validationException() {
        // Act + Assert – real validator rejects id < 0
        assertThrows(ValidationException.class,
                () -> service.addProduct(-1, "Cafea", 5.0, CATEGORY, TYPE));

        // Verify repo was never touched
        verify(mockRepo, never()).save(any(Product.class));
    }

    /**
     * TC3 – F01: addProduct with invalid price (zero).
     * Real validator throws ValidationException → mock repo.save NOT called.
     */
    @Test
    @DisplayName("TC3 – F01: addProduct invalid price → real validator throws, repo.save NOT called")
    void tc3_addProduct_invalidPrice_validationException() {
        assertThrows(ValidationException.class,
                () -> service.addProduct(1, "Cafea", 0.0, CATEGORY, TYPE));

        verify(mockRepo, never()).save(any(Product.class));
    }

    /**
     * TC4 – F02: updateProduct existing with valid data.
     * Real validator passes → mock repo.update called.
     */
    @Test
    @DisplayName("TC4 – F02: updateProduct existing valid → real validator passes, repo.update called")
    void tc4_updateProduct_valid_updateCalled() {
        // Arrange – product exists
        Product existing = new Product(1, "Cafea", 5.0, CATEGORY, TYPE);
        when(mockRepo.findOne(1)).thenReturn(existing);

        // Act
        service.updateProduct(1, "Cafea Noua", 6.0, CATEGORY, TYPE);

        // Assert + Verify
        verify(mockRepo, times(1)).update(any(Product.class));
    }
}
