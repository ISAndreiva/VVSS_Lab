package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.Validator;
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
 * Step 1 – unit test for S (ProductService).
 * Both V (ProductValidator) and R (Repository) are mocked.
 * Scenario 1: V <--- S ---> R, top-down breadth first.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Step 1 – Unit test ProductService (mock V + mock R)")
@Tag("integration")
class ProductServiceUnitTest {

    private static final CategorieBautura CATEGORY = new CategorieBautura(1, "Cafea");
    private static final TipBautura TYPE = new TipBautura(1, "Calda");

    @Mock
    private Repository<Integer, Product> mockRepo;

    @Mock
    private Validator<Product> mockValidator;

    private ProductService service;

    @BeforeEach
    void setUp() {
        service = new ProductService(mockRepo, mockValidator);
    }

    /**
     * TC1 – F01: addProduct with valid data.
     * Validator mock passes silently → repo.save must be called once.
     */
    @Test
    @DisplayName("TC1 – F01: addProduct valid → repo.save called")
    void tc1_addProduct_valid_saveCalled() {
        // Arrange
        Product expected = new Product(1, "Cafea", 5.0, CATEGORY, TYPE);
        when(mockRepo.save(any(Product.class))).thenReturn(expected);

        // Act
        service.addProduct(1, "Cafea", 5.0, CATEGORY, TYPE);

        // Assert + Verify
        verify(mockValidator, times(1)).validate(any(Product.class));
        verify(mockRepo, times(1)).save(any(Product.class));
    }

    /**
     * TC2 – F01: addProduct with invalid data.
     * Validator mock throws ValidationException → repo.save must NOT be called.
     */
    @Test
    @DisplayName("TC2 – F01: addProduct invalid → validator throws, repo.save NOT called")
    void tc2_addProduct_invalid_saveNotCalled() {
        // Arrange – validator rejects
        doThrow(new ValidationException("ID invalid!\n"))
                .when(mockValidator).validate(any(Product.class));

        // Act + Assert
        assertThrows(ValidationException.class,
                () -> service.addProduct(-1, "Cafea", 5.0, CATEGORY, TYPE));

        // Verify repo was never touched
        verify(mockRepo, never()).save(any(Product.class));
    }

    /**
     * TC3 – F02: updateProduct when product exists.
     * Validator mock passes → repo.update must be called once.
     */
    @Test
    @DisplayName("TC3 – F02: updateProduct existing → repo.update called")
    void tc3_updateProduct_existing_updateCalled() {
        // Arrange – product exists
        Product existing = new Product(1, "Cafea", 5.0, CATEGORY, TYPE);
        when(mockRepo.findOne(1)).thenReturn(existing);

        // Act
        service.updateProduct(1, "Cafea Noua", 6.0, CATEGORY, TYPE);

        // Assert + Verify
        verify(mockValidator, times(1)).validate(any(Product.class));
        verify(mockRepo, times(1)).update(any(Product.class));
    }

    /**
     * TC4 – F02: updateProduct when product does NOT exist.
     * Service throws IllegalArgumentException → neither validate nor update called.
     */
    @Test
    @DisplayName("TC4 – F02: updateProduct not found → exception, repo.update NOT called")
    void tc4_updateProduct_notFound_exceptionThrown() {
        // Arrange – product does not exist
        when(mockRepo.findOne(99)).thenReturn(null);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(99, "Cafea", 5.0, CATEGORY, TYPE));

        // Verify neither validate nor update was called
        verify(mockValidator, never()).validate(any(Product.class));
        verify(mockRepo, never()).update(any(Product.class));
    }

    @Test
    @DisplayName("Unified tests")
    void step1_allTests()
    {
        tc1_addProduct_valid_saveCalled();
        reset(mockRepo, mockValidator); setUp();
        tc2_addProduct_invalid_saveNotCalled();
        reset(mockRepo, mockValidator); setUp();
        tc3_updateProduct_existing_updateCalled();
        reset(mockRepo, mockValidator); setUp();
        tc4_updateProduct_notFound_exceptionThrown();
    }
}
