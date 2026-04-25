package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.AbstractRepository;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("ECP + BVA for ProductService (F01/F02)")
@Tag("bbt")
class ProductServiceEcpBvaTest {

    private static final int EXISTING_ID = 1;
    private static final CategorieBautura VALID_CATEGORY = new CategorieBautura(1, "Categorie1");
    private static final TipBautura VALID_TYPE = new TipBautura(1, "Tip1");

    private ProductService service;
    private Repository<Integer, Product> repo;

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("F01 - addProduct")
    class AddProductTests {

        @BeforeEach
        void setUpAddSuite() {
            repo = new InMemoryProductRepository();
            service = new ProductService(repo, new ProductValidator());
        }

        Stream<Arguments> ecpCases() {
            return Stream.of(
                    Arguments.of("TC1_ECP", 0, "Nume Valid", 10.0, true, ""),
                    Arguments.of("TC2_ECP", 10, "Nume Valid", 15.0, true, ""),
                    Arguments.of("TC3_ECP", -5, "Nume Valid", 15.0, false, "ID invalid!\n"),
                    Arguments.of("TC4_ECP", 0, "Nume Valid", 0.0, false, "Pret invalid!\n"),
                    Arguments.of("TC5_ECP", 0, "Nume Valid", -10.0, false, "Pret invalid!\n"),
                    Arguments.of("TC6_ECP", 0, "Nume Valid", 100000.0, false, "Pret invalid!\n"),
                    Arguments.of("TC7_ECP", -5, "Nume Valid", 0.0, false, "ID invalid!\nPret invalid!\n"),
                    Arguments.of("TC8_ECP", -5, "Nume Valid", -10.0, false, "ID invalid!\nPret invalid!\n"),
                    Arguments.of("TC9_ECP", -5, "Nume Valid", 100000.0, false, "ID invalid!\nPret invalid!\n")
            );
        }

        Stream<Arguments> bvaCases() {
            return Stream.of(
                    Arguments.of("TC1_BVA", 0, "Nume Valid", 10.0, true, ""),
                    Arguments.of("TC2_BVA", 1, "Nume Valid", 10.0, true, ""),
                    Arguments.of("TC3_BVA", -1, "Nume Valid", 10.0, false, "ID invalid!\n"),
                    Arguments.of("TC4_BVA", Integer.MAX_VALUE, "Nume Valid", 10.0, true, ""),
                    Arguments.of("TC5_BVA", Integer.MAX_VALUE - 1, "Nume Valid", 10.0, true, ""),
                    Arguments.of("TC6_BVA", (int) ((long) Integer.MAX_VALUE + 1L), "Nume Valid", 10.0, false, "ID invalid!\n"),
                    Arguments.of("TC7_BVA", 1, "Nume Valid", 0.0, false, "Pret invalid!\n"),
                    Arguments.of("TC8_BVA", 1, "Nume Valid", 0.0 - 0.000001, false, "Pret invalid!\n"),
                    Arguments.of("TC9_BVA", 1, "Nume Valid", 0.0 + 0.000001, true, ""),
                    Arguments.of("TC10_BVA", 1, "Nume Valid", 10000.0, true, ""),
                    Arguments.of("TC11_BVA", 1, "Nume Valid", 10000.0 - 0.000001, true, ""),
                    Arguments.of("TC12_BVA", 1, "Nume Valid", 10000.0 + 0.000001, false, "Pret invalid!\n")
            );
        }

        @ParameterizedTest(name = "addProductEcp")
        @MethodSource("ecpCases")
        @DisplayName("ECP cases for addProduct")
        void addProductEcp(String tc,
                           int id,
                           String nume,
                           double pret,
                           boolean shouldSucceed,
                           String expectedError) {
            assertAddCase(id, nume, pret, shouldSucceed, expectedError);
        }

        @ParameterizedTest(name = "addProductBva")
        @MethodSource("bvaCases")
        @DisplayName("BVA cases for addProduct")
        void addProductBva(String tc,
                           int id,
                           String nume,
                           double pret,
                           boolean shouldSucceed,
                           String expectedError) {
            assertAddCase(id, nume, pret, shouldSucceed, expectedError);
        }

        private void assertAddCase(int id,
                                   String nume,
                                   double pret,
                                   boolean shouldSucceed,
                                   String expectedError) {
            // Arrange
            int initialSize = repo.findAll().size();

            // Act + Assert
            if (shouldSucceed) {
                service.addProduct(id, nume, pret, VALID_CATEGORY, VALID_TYPE);
                assertEquals(initialSize + 1, repo.findAll().size());
                assertNotNull(repo.findOne(id));
            } else {
                ValidationException ex = assertThrows(
                        ValidationException.class,
                        () -> service.addProduct(id, nume, pret, VALID_CATEGORY, VALID_TYPE)
                );
                assertEquals(expectedError, ex.getMessage());
                assertEquals(initialSize, repo.findAll().size());
            }
        }
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    @DisplayName("F02 - updateProduct")
    class UpdateProductTests {

        @BeforeEach
        void setUpUpdateSuite() {
            repo = new InMemoryProductRepository();
            service = new ProductService(repo, new ProductValidator());
            repo.save(new Product(EXISTING_ID, "Initial", 10.0, VALID_CATEGORY, VALID_TYPE));
        }

        Stream<Arguments> ecpCases() {
            return Stream.of(
                    Arguments.of("TC1_ECP", 1, "Nume Valid", 15.0, true, null, ""),
                    Arguments.of("TC2_ECP", 100, "Nume Valid", 15.0, false, IllegalArgumentException.class,"Entity does not exist in repository"),
                    Arguments.of("TC3_ECP", 1, "", 15.0, false, ValidationException.class,"Numele nu poate fi gol!\n"),
                    Arguments.of("TC4_ECP", 100, "", 15.0, false, IllegalArgumentException.class,"Entity does not exist in repository")
            );
        }

        Stream<Arguments> bvaCases() {
            return Stream.of(
                    Arguments.of("TC1_BVA", 1, "Nume Valid", 10.0, true, null, ""),
                    Arguments.of("TC2_BVA", 100, "Nume Valid", 10.0, false, IllegalArgumentException.class,"Entity does not exist in repository"),
                    Arguments.of("TC3_BVA", 1, "N", 10.0, true, null, ""),
                    Arguments.of("TC4_BVA", 1, "?", 10.0, false, ValidationException.class,"Numele nu poate fi gol!\n"),
                    Arguments.of("TC5_BVA", 1, "N".repeat(99), 10.0, true, null, ""),
                    Arguments.of("TC6_BVA", 1, "N".repeat(98), 10.0, true, null, ""),
                    Arguments.of("TC7_BVA", 1, "", 10.0, false, ValidationException.class,"Numele nu poate fi gol!\n")
            );
        }

        @ParameterizedTest(name = "updateProductEcp")
        @MethodSource("ecpCases")
        @DisplayName("ECP cases for updateProduct")
        void updateProductEcp(String tc,
                              int id,
                              String nume,
                              double pret,
                              boolean shouldSucceed,
                              Class<? extends Throwable> expectedException,
                              String expectedMessage) {
            assertUpdateCase(id, nume, pret, shouldSucceed, expectedException, expectedMessage);
        }

        @ParameterizedTest(name = "updateProductBva")
        @MethodSource("bvaCases")
        @DisplayName("BVA cases for updateProduct")
        void updateProductBva(String tc,
                              int id,
                              String nume,
                              double pret,
                              boolean shouldSucceed,
                              Class<? extends Throwable> expectedException,
                              String expectedMessage) {
            assertUpdateCase(id, nume, pret, shouldSucceed, expectedException, expectedMessage);
        }

        private void assertUpdateCase(int id,
                                      String nume,
                                      double pret,
                                      boolean shouldSucceed,
                                      Class<? extends Throwable> expectedException,
                                      String expectedMessage) {
            // Arrange
            Product previous = repo.findOne(EXISTING_ID);

            // Act + Assert
            if (shouldSucceed) {
                service.updateProduct(id, nume, pret, VALID_CATEGORY, VALID_TYPE);
                Product updated = repo.findOne(id);
                assertNotNull(updated);
                assertEquals(nume, updated.getNume());
                assertEquals(pret, updated.getPret());
            } else {
                Throwable ex = assertThrows(
                        expectedException,
                        () -> service.updateProduct(id, nume, pret, VALID_CATEGORY, VALID_TYPE)
                );
                assertEquals(expectedMessage, ex.getMessage());
                assertEquals(previous.getNume(), repo.findOne(EXISTING_ID).getNume());
                assertEquals(previous.getPret(), repo.findOne(EXISTING_ID).getPret());
            }
        }
    }

    private static final class InMemoryProductRepository extends AbstractRepository<Integer, Product> {
        @Override
        protected Integer getId(Product entity) {
            return entity.getId();
        }
    }
}
