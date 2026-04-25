package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("bbt")
class ProductServiceEcpBvaTest {

    private static final int EXISTING_ID = 1;
    private static final CategorieBautura VALID_CATEGORY = new CategorieBautura(1, "Categorie1");
    private static final TipBautura VALID_TYPE = new TipBautura(1, "Tip1");

    private ProductService service;
    private Repository<Integer, Product> repo;

    @BeforeEach
    void setUp() {
        repo = new InMemoryProductRepository();
        service = new ProductService(repo, new ProductValidator());
    }

    // -------------------------------------------------------------------------
    // F01 - addProduct - ECP
    // -------------------------------------------------------------------------

    @Test
    void addProductEcp_TC1() {
        int initialSize = repo.findAll().size();
        service.addProduct(0, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(0));
    }

    @Test
    void addProductEcp_TC2() {
        int initialSize = repo.findAll().size();
        service.addProduct(10, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(10));
    }

    @Test
    void addProductEcp_TC3() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductEcp_TC4() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(0, "Nume Valid", 0.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductEcp_TC5() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(0, "Nume Valid", -10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductEcp_TC6() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(0, "Nume Valid", 100000.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductEcp_TC7() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", 0.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\nPret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductEcp_TC8() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", -10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\nPret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductEcp_TC9() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", 100000.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\nPret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    // -------------------------------------------------------------------------
    // F01 - addProduct - BVA
    // -------------------------------------------------------------------------

    @Test
    void addProductBva_TC1() {
        int initialSize = repo.findAll().size();
        service.addProduct(0, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(0));
    }

    @Test
    void addProductBva_TC2() {
        int initialSize = repo.findAll().size();
        service.addProduct(1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(1));
    }

    @Test
    void addProductBva_TC3() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(-1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductBva_TC4() {
        int initialSize = repo.findAll().size();
        service.addProduct(Integer.MAX_VALUE, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(Integer.MAX_VALUE));
    }

    @Test
    void addProductBva_TC5() {
        int initialSize = repo.findAll().size();
        service.addProduct(Integer.MAX_VALUE - 1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(Integer.MAX_VALUE - 1));
    }

    @Test
    void addProductBva_TC6() {
        int initialSize = repo.findAll().size();
        int overflowId = (int) ((long) Integer.MAX_VALUE + 1L);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(overflowId, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductBva_TC7() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(1, "Nume Valid", 0.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductBva_TC8() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(1, "Nume Valid", 0.0 - 0.000001, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    @Test
    void addProductBva_TC9() {
        int initialSize = repo.findAll().size();
        service.addProduct(1, "Nume Valid", 0.0 + 0.000001, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(1));
    }

    @Test
    void addProductBva_TC10() {
        int initialSize = repo.findAll().size();
        service.addProduct(1, "Nume Valid", 10000.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(1));
    }

    @Test
    void addProductBva_TC11() {
        int initialSize = repo.findAll().size();
        service.addProduct(1, "Nume Valid", 10000.0 - 0.000001, VALID_CATEGORY, VALID_TYPE);
        assertEquals(initialSize + 1, repo.findAll().size());
        assertNotNull(repo.findOne(1));
    }

    @Test
    void addProductBva_TC12() {
        int initialSize = repo.findAll().size();
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.addProduct(1, "Nume Valid", 10000.0 + 0.000001, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex.getMessage());
        assertEquals(initialSize, repo.findAll().size());
    }

    // -------------------------------------------------------------------------
    // F02 - updateProduct - ECP
    // -------------------------------------------------------------------------

    @Test
    void updateProductEcp_TC1() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        service.updateProduct(1, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE);
        Product updated = repo.findOne(1);
        assertNotNull(updated);
        assertEquals("Nume Valid", updated.getNume());
        assertEquals(15.0, updated.getPret());
    }

    @Test
    void updateProductEcp_TC2() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        Product previous = repo.findOne(EXISTING_ID);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(100, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Entity does not exist in repository", ex.getMessage());
        assertEquals(previous.getNume(), repo.findOne(EXISTING_ID).getNume());
        assertEquals(previous.getPret(), repo.findOne(EXISTING_ID).getPret());
    }

    @Test
    void updateProductEcp_TC3() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        Product previous = repo.findOne(EXISTING_ID);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.updateProduct(1, "", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Numele nu poate fi gol!\n", ex.getMessage());
        assertEquals(previous.getNume(), repo.findOne(EXISTING_ID).getNume());
        assertEquals(previous.getPret(), repo.findOne(EXISTING_ID).getPret());
    }

    @Test
    void updateProductEcp_TC4() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        Product previous = repo.findOne(EXISTING_ID);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(100, "", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Entity does not exist in repository", ex.getMessage());
        assertEquals(previous.getNume(), repo.findOne(EXISTING_ID).getNume());
        assertEquals(previous.getPret(), repo.findOne(EXISTING_ID).getPret());
    }

    // -------------------------------------------------------------------------
    // F02 - updateProduct - BVA
    // -------------------------------------------------------------------------

    @Test
    void updateProductBva_TC1() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        service.updateProduct(1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        Product updated = repo.findOne(1);
        assertNotNull(updated);
        assertEquals("Nume Valid", updated.getNume());
        assertEquals(10.0, updated.getPret());
    }

    @Test
    void updateProductBva_TC2() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        Product previous = repo.findOne(EXISTING_ID);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(100, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Entity does not exist in repository", ex.getMessage());
        assertEquals(previous.getNume(), repo.findOne(EXISTING_ID).getNume());
        assertEquals(previous.getPret(), repo.findOne(EXISTING_ID).getPret());
    }

    @Test
    void updateProductBva_TC3() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        service.updateProduct(1, "N", 10.0, VALID_CATEGORY, VALID_TYPE);
        Product updated = repo.findOne(1);
        assertNotNull(updated);
        assertEquals("N", updated.getNume());
        assertEquals(10.0, updated.getPret());
    }

    @Test
    void updateProductBva_TC4() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        Product previous = repo.findOne(EXISTING_ID);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.updateProduct(1, "?", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Numele nu poate fi gol!\n", ex.getMessage());
        assertEquals(previous.getNume(), repo.findOne(EXISTING_ID).getNume());
        assertEquals(previous.getPret(), repo.findOne(EXISTING_ID).getPret());
    }

    @Test
    void updateProductBva_TC5() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        String longName = "N".repeat(99);
        service.updateProduct(1, longName, 10.0, VALID_CATEGORY, VALID_TYPE);
        Product updated = repo.findOne(1);
        assertNotNull(updated);
        assertEquals(longName, updated.getNume());
        assertEquals(10.0, updated.getPret());
    }

    @Test
    void updateProductBva_TC6() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        String longName = "N".repeat(98);
        service.updateProduct(1, longName, 10.0, VALID_CATEGORY, VALID_TYPE);
        Product updated = repo.findOne(1);
        assertNotNull(updated);
        assertEquals(longName, updated.getNume());
        assertEquals(10.0, updated.getPret());
    }

    @Test
    void updateProductBva_TC7() {
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        Product previous = repo.findOne(EXISTING_ID);
        ValidationException ex = assertThrows(ValidationException.class,
                () -> service.updateProduct(1, "", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Numele nu poate fi gol!\n", ex.getMessage());
        assertEquals(previous.getNume(), repo.findOne(EXISTING_ID).getNume());
        assertEquals(previous.getPret(), repo.findOne(EXISTING_ID).getPret());
    }

    // -------------------------------------------------------------------------

    private static final class InMemoryProductRepository extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }
}
