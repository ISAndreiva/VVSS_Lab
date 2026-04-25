package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
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

    private void reset() {
        repo = new InMemoryProductRepository();
        service = new ProductService(repo, new ProductValidator());
    }

    private void resetWithExisting() {
        reset();
        repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
    }

    // -------------------------------------------------------------------------

    @Test
    void ecpValid() {
        // TC1_ECP addProduct: id=0, price=10
        reset();
        service.addProduct(0, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(0));

        // TC2_ECP addProduct: id=10, price=15
        reset();
        service.addProduct(10, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(10));

        // TC1_ECP updateProduct: valid id, valid name, valid price
        resetWithExisting();
        service.updateProduct(1, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE);
        Product updated = repo.findOne(1);
        assertNotNull(updated);
        assertEquals("Nume Valid", updated.getNume());
        assertEquals(15.0, updated.getPret());
    }

    @Test
    void ecpInvalid() {
        // TC3_ECP addProduct: id=-5
        reset();
        ValidationException ex3 = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\n", ex3.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC4_ECP addProduct: price=0
        reset();
        ValidationException ex4 = assertThrows(ValidationException.class,
                () -> service.addProduct(0, "Nume Valid", 0.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex4.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC5_ECP addProduct: price=-10
        reset();
        ValidationException ex5 = assertThrows(ValidationException.class,
                () -> service.addProduct(0, "Nume Valid", -10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex5.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC6_ECP addProduct: price=100000
        reset();
        ValidationException ex6 = assertThrows(ValidationException.class,
                () -> service.addProduct(0, "Nume Valid", 100000.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex6.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC7_ECP addProduct: id=-5, price=0
        reset();
        ValidationException ex7 = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", 0.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\nPret invalid!\n", ex7.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC8_ECP addProduct: id=-5, price=-10
        reset();
        ValidationException ex8 = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", -10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\nPret invalid!\n", ex8.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC9_ECP addProduct: id=-5, price=100000
        reset();
        ValidationException ex9 = assertThrows(ValidationException.class,
                () -> service.addProduct(-5, "Nume Valid", 100000.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\nPret invalid!\n", ex9.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC2_ECP updateProduct: id not in repo
        resetWithExisting();
        IllegalArgumentException upEx2 = assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(100, "Nume Valid", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Entity does not exist in repository", upEx2.getMessage());
        assertEquals("Initial", repo.findOne(EXISTING_ID).getNume());

        // TC3_ECP updateProduct: blank name
        resetWithExisting();
        ValidationException upEx3 = assertThrows(ValidationException.class,
                () -> service.updateProduct(1, "", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Numele nu poate fi gol!\n", upEx3.getMessage());
        assertEquals("Initial", repo.findOne(EXISTING_ID).getNume());

        // TC4_ECP updateProduct: id not in repo, blank name
        resetWithExisting();
        IllegalArgumentException upEx4 = assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(100, "", 15.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Entity does not exist in repository", upEx4.getMessage());
        assertEquals("Initial", repo.findOne(EXISTING_ID).getNume());
    }

    @Test
    void bvaValid() {
        // TC1_BVA addProduct: id=0, price=10
        reset();
        service.addProduct(0, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(0));

        // TC2_BVA addProduct: id=1, price=10
        reset();
        service.addProduct(1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(1));

        // TC4_BVA addProduct: id=Integer.MAX_VALUE
        reset();
        service.addProduct(Integer.MAX_VALUE, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(Integer.MAX_VALUE));

        // TC5_BVA addProduct: id=Integer.MAX_VALUE-1
        reset();
        service.addProduct(Integer.MAX_VALUE - 1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(Integer.MAX_VALUE - 1));

        // TC9_BVA addProduct: price just above 0
        reset();
        service.addProduct(1, "Nume Valid", 0.0 + 0.000001, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(1));

        // TC10_BVA addProduct: price=10000
        reset();
        service.addProduct(1, "Nume Valid", 10000.0, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(1));

        // TC11_BVA addProduct: price just below 10000
        reset();
        service.addProduct(1, "Nume Valid", 10000.0 - 0.000001, VALID_CATEGORY, VALID_TYPE);
        assertEquals(1, repo.findAll().size());
        assertNotNull(repo.findOne(1));

        // TC1_BVA updateProduct: valid id, valid name, valid price
        resetWithExisting();
        service.updateProduct(1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE);
        Product up1 = repo.findOne(1);
        assertNotNull(up1);
        assertEquals("Nume Valid", up1.getNume());
        assertEquals(10.0, up1.getPret());

        // TC3_BVA updateProduct: single-char name
        resetWithExisting();
        service.updateProduct(1, "N", 10.0, VALID_CATEGORY, VALID_TYPE);
        Product up3 = repo.findOne(1);
        assertNotNull(up3);
        assertEquals("N", up3.getNume());

        // TC5_BVA updateProduct: 99-char name
        resetWithExisting();
        String name99 = "N".repeat(99);
        service.updateProduct(1, name99, 10.0, VALID_CATEGORY, VALID_TYPE);
        Product up5 = repo.findOne(1);
        assertNotNull(up5);
        assertEquals(name99, up5.getNume());

        // TC6_BVA updateProduct: 98-char name
        resetWithExisting();
        String name98 = "N".repeat(98);
        service.updateProduct(1, name98, 10.0, VALID_CATEGORY, VALID_TYPE);
        Product up6 = repo.findOne(1);
        assertNotNull(up6);
        assertEquals(name98, up6.getNume());
    }

    @Test
    void bvaInvalid() {
        // TC3_BVA addProduct: id=-1
        reset();
        ValidationException ex3 = assertThrows(ValidationException.class,
                () -> service.addProduct(-1, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\n", ex3.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC6_BVA addProduct: overflow id (Integer.MIN_VALUE due to cast)
        reset();
        int overflowId = (int) ((long) Integer.MAX_VALUE + 1L);
        ValidationException ex6 = assertThrows(ValidationException.class,
                () -> service.addProduct(overflowId, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("ID invalid!\n", ex6.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC7_BVA addProduct: price=0
        reset();
        ValidationException ex7 = assertThrows(ValidationException.class,
                () -> service.addProduct(1, "Nume Valid", 0.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex7.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC8_BVA addProduct: price just below 0
        reset();
        ValidationException ex8 = assertThrows(ValidationException.class,
                () -> service.addProduct(1, "Nume Valid", 0.0 - 0.000001, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex8.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC12_BVA addProduct: price just above 10000
        reset();
        ValidationException ex12 = assertThrows(ValidationException.class,
                () -> service.addProduct(1, "Nume Valid", 10000.0 + 0.000001, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Pret invalid!\n", ex12.getMessage());
        assertEquals(0, repo.findAll().size());

        // TC2_BVA updateProduct: id not in repo
        resetWithExisting();
        IllegalArgumentException upEx2 = assertThrows(IllegalArgumentException.class,
                () -> service.updateProduct(100, "Nume Valid", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Entity does not exist in repository", upEx2.getMessage());
        assertEquals("Initial", repo.findOne(EXISTING_ID).getNume());

        // TC4_BVA updateProduct: invalid name "?"
        resetWithExisting();
        ValidationException upEx4 = assertThrows(ValidationException.class,
                () -> service.updateProduct(1, "?", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Numele nu poate fi gol!\n", upEx4.getMessage());
        assertEquals("Initial", repo.findOne(EXISTING_ID).getNume());

        // TC7_BVA updateProduct: blank name
        resetWithExisting();
        ValidationException upEx7 = assertThrows(ValidationException.class,
                () -> service.updateProduct(1, "", 10.0, VALID_CATEGORY, VALID_TYPE));
        assertEquals("Numele nu poate fi gol!\n", upEx7.getMessage());
        assertEquals("Initial", repo.findOne(EXISTING_ID).getNume());
    }

    // -------------------------------------------------------------------------

    private static final class InMemoryProductRepository extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }
}
