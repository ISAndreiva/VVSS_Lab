package drinkshop.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ProductTest {

    Product product;
    CategorieBautura catJuice;
    CategorieBautura catSmoothie;
    TipBautura tipWaterBased;
    TipBautura tipBasic;

    @BeforeEach
    void setUp() {
        catJuice     = new CategorieBautura(1, "JUICE");
        catSmoothie  = new CategorieBautura(2, "SMOOTHIE");
        tipWaterBased = new TipBautura(1, "WATER_BASED");
        tipBasic      = new TipBautura(2, "BASIC");
        product = new Product(100, "Limonada", 10.0, catJuice, tipWaterBased);
    }

    @AfterEach
    void tearDown() {
        product = null;
    }

    @Test
    void getId() {
        assert 100 == product.getId();
    }

    @Test
    void getNume() {
        assert "Limonada".equals(product.getNume());
    }

    @Test
    void getPret() {
        assert 10.0 == product.getPret();
    }

    @Test
    void getCategorie() {
        assert catJuice.equals(product.getCategorie());
    }

    @Test
    void setCategorie() {
        product.setCategorie(catSmoothie);
        assert catSmoothie.equals(product.getCategorie());
    }

    @Test
    void getTip() {
        assert tipWaterBased.equals(product.getTip());
    }

    @Test
    void setTip() {
        product.setTip(tipBasic);
        assert tipBasic.equals(product.getTip());
    }

    @Test
    void setNume() {
        product.setNume("newLimonada");
        assert "newLimonada".equals(product.getNume());
    }

    @Test
    void setPret() {
        product.setPret(10.05);
        assert 10.05 == product.getPret();
    }

    @Test
    void testToString() {
        System.out.println(product.toString());
        assert "Limonada (JUICE, WATER_BASED) - 10.0 lei".equals(product.toString());
    }
}