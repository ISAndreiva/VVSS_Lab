package drinkshop.service;

import drinkshop.domain.*;
import drinkshop.export.CsvExporter;
import drinkshop.receipt.ReceiptGenerator;

import java.util.List;

/**
 * <strong>Facade</strong> that provides a single, unified API for the
 * DrinkShop application and coordinates between all domain services.
 * All sub-services are injected through the constructor (defect A02 – no more
 * tight coupling via {@code new} inside the class). The class also exposes
 * current-order management so all related business state stays in the service
 * layer and not in the UI controller (defect A08).
 *
 * @see ProductService
 * @see OrderService
 * @see RetetaService
 * @see StocService
 * @see CategorieService
 * @see TipService
 * @see IngredientService
 * @see DailyReportService
 */
public class DrinkShopService {

    private final ProductService productService;
    private final OrderService orderService;
    private final RetetaService retetaService;
    private final StocService stocService;
    private final CategorieService categorieService;
    private final TipService tipService;
    private final IngredientService ingredientService;
    private final DailyReportService report;

    public DrinkShopService(
            ProductService productService,
            OrderService orderService,
            RetetaService retetaService,
            StocService stocService,
            CategorieService categorieService,
            TipService tipService,
            IngredientService ingredientService,
            DailyReportService report) {
        this.productService = productService;
        this.orderService = orderService;
        this.retetaService = retetaService;
        this.stocService = stocService;
        this.categorieService = categorieService;
        this.tipService = tipService;
        this.ingredientService = ingredientService;
        this.report = report;
    }

    // ---- Product ----

    public void addProduct(Product p) { productService.addProduct(p); }
    public void updateProduct(int id, String name, double price,
                               CategorieBautura cat, TipBautura tip) {
        productService.updateProduct(id, name, price, cat, tip);
    }
    public void deleteProduct(int id) { productService.deleteProduct(id); }
    public List<Product> getAllProducts() { return productService.getAllProducts(); }
    public int nextProductId() { return productService.nextId(); }
    public List<Product> filtreazaDupaCategorie(CategorieBautura c) {
        return productService.filterByCategorie(c);
    }
    public List<Product> filtreazaDupaTip(TipBautura t) {
        return productService.filterByTip(t);
    }

    // ---- Order (persisted) ----

    public List<Order> getAllOrders() { return orderService.getAllOrders(); }

    // ---- Current order (in-progress, defect A08) ----

    public void addItemToCurrentOrder(OrderItem item) {
        orderService.addItemToCurrentOrder(item);
    }
    public void removeItemFromCurrentOrder(OrderItem item) {
        orderService.removeItemFromCurrentOrder(item);
    }
    public List<OrderItem> getCurrentOrderItems() {
        return orderService.getCurrentOrderItems();
    }
    public double computeCurrentOrderTotal() {
        return orderService.computeCurrentOrderTotal();
    }
    public Order finalizeCurrentOrder() {
        return orderService.finalizeCurrentOrder();
    }

    // ---- Receipt & Export ----

    public String generateReceipt(Order o) {
        return ReceiptGenerator.generate(o, productService.getAllProducts());
    }
    public double getDailyRevenue() { return report.getTotalRevenue(); }
    public void exportCsv(String path) {
        CsvExporter.exportOrders(productService.getAllProducts(),
                orderService.getAllOrders(), path);
    }

    // ---- Reteta ----

    public List<Reteta> getAllRetete() { return retetaService.getAll(); }
    public void addReteta(Reteta r) { retetaService.addReteta(r); }
    public void updateReteta(Reteta r) { retetaService.updateReteta(r); }
    public void deleteReteta(int id) { retetaService.deleteReteta(id); }
    public int nextRetetaId() {
        return retetaService.getAll().stream()
                .mapToInt(Reteta::getId).max().orElse(0) + 1;
    }

    // ---- Stock ----

    public List<Stoc> getAllStocuri() { return stocService.getAll(); }
    public void addStocObserver(StocObserver obs) { stocService.addObserver(obs); }
    public void comandaProdus(Product produs) {
        Reteta reteta = retetaService.findById(produs.getId());
        if (reteta == null)
            throw new drinkshop.service.validator.ValidationException(
                    "Nu exista reteta pentru produsul: " + produs.getNume());
        if (!stocService.areSuficient(reteta))
            throw new drinkshop.service.validator.ValidationException(
                    "Stoc insuficient pentru produsul: " + produs.getNume());
        stocService.consuma(reteta);
    }

    // ---- Categorie ----

    public List<CategorieBautura> getAllCategorii() { return categorieService.getAll(); }
    public void addCategorie(CategorieBautura c) { categorieService.add(c); }
    public void updateCategorie(CategorieBautura c) { categorieService.update(c); }
    /**
     * Deletes a category only if no product currently uses it
     * (defect A03 – deletion constraint).
     */
    public void deleteCategorie(int id) {
        boolean used = productService.getAllProducts().stream()
                .anyMatch(p -> p.getCategorie().getId() == id);
        if (used)
            throw new drinkshop.service.validator.ValidationException(
                    "Categoria este folosita de produse existente si nu poate fi stearsa.");
        categorieService.delete(id);
    }
    public int nextCategorieId() { return categorieService.nextId(); }

    // ---- Tip ----

    public List<TipBautura> getAllTipuri() { return tipService.getAll(); }
    public void addTip(TipBautura t) { tipService.add(t); }
    public void updateTip(TipBautura t) { tipService.update(t); }
    /**
     * Deletes a type only if no product currently uses it
     * (defect A03 – deletion constraint).
     */
    public void deleteTip(int id) {
        boolean used = productService.getAllProducts().stream()
                .anyMatch(p -> p.getTip().getId() == id);
        if (used)
            throw new drinkshop.service.validator.ValidationException(
                    "Tipul este folosit de produse existente si nu poate fi sters.");
        tipService.delete(id);
    }
    public int nextTipId() { return tipService.nextId(); }

    // ---- Ingredient ----

    public List<Ingredient> getAllIngredienti() { return ingredientService.getAll(); }
    public void addIngredient(Ingredient i) { ingredientService.add(i); }
    public void updateIngredient(Ingredient i) { ingredientService.update(i); }
    public Ingredient findIngredientByName(String name) {
        return ingredientService.findByName(name);
    }
    /**
     * Deletes an ingredient only if it is not referenced by any recipe or
     * stock entry.
     */
    public void deleteIngredient(int id) {
        boolean inReteta = retetaService.getAll().stream()
                .flatMap(r -> r.getIngrediente().stream())
                .anyMatch(ir -> ir.getIngredient().getId() == id);
        if (inReteta)
            throw new drinkshop.service.validator.ValidationException(
                    "Ingredientul este folosit intr-o reteta si nu poate fi sters.");
        boolean inStoc = stocService.getAll().stream()
                .anyMatch(s -> s.getIngredient().getId() == id);
        if (inStoc)
            throw new drinkshop.service.validator.ValidationException(
                    "Ingredientul are stoc inregistrat si nu poate fi sters.");
        ingredientService.delete(id);
    }
    public int nextIngredientId() { return ingredientService.nextId(); }
}