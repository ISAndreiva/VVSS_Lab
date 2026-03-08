package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.service.validator.ProductValidator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Calls the validator on every mutation (defect A04).
 * Uses null instead of a sentinel ALL enum value for "show all" filtering
 * (defect A03 – CategorieBautura/TipBautura no longer have an ALL constant).
 */
public class ProductService {

    private final Repository<Integer, Product> productRepo;
    private final ProductValidator validator;

    public ProductService(Repository<Integer, Product> productRepo,
                          ProductValidator validator) {
        this.productRepo = productRepo;
        this.validator = validator;
    }

    public void addProduct(Product p) {
        validator.validate(p);
        productRepo.save(p);
    }

    public void updateProduct(int id, String name, double price,
                               CategorieBautura categorie, TipBautura tip) {
        Product updated = new Product(id, name, price, categorie, tip);
        validator.validate(updated);
        productRepo.update(updated);
    }

    public void deleteProduct(int id) {
        productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product findById(int id) {
        return productRepo.findOne(id);
    }

    /** Pass {@code null} to retrieve all products (replaces old ALL sentinel). */
    public List<Product> filterByCategorie(CategorieBautura categorie) {
        if (categorie == null) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getCategorie().getId() == categorie.getId())
                .collect(Collectors.toList());
    }

    /** Pass {@code null} to retrieve all products (replaces old ALL sentinel). */
    public List<Product> filterByTip(TipBautura tip) {
        if (tip == null) return getAllProducts();
        return getAllProducts().stream()
                .filter(p -> p.getTip().getId() == tip.getId())
                .collect(Collectors.toList());
    }

    public int nextId() {
        return productRepo.findAll().stream()
                .mapToInt(Product::getId).max().orElse(0) + 1;
    }
}