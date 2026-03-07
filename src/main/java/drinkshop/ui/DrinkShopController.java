package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.service.DrinkShopService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * UI controller. All business state (current order) has been moved to the
 * service layer (defect A08). Categories, types and ingredients are loaded
 * dynamically from their services (defect A03).
 */
public class DrinkShopController {

    private DrinkShopService service;

    // ---- PRODUCT ----
    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> colProdId;
    @FXML private TableColumn<Product, String> colProdName;
    @FXML private TableColumn<Product, Double> colProdPrice;
    @FXML private TableColumn<Product, CategorieBautura> colProdCategorie;
    @FXML private TableColumn<Product, TipBautura> colProdTip;
    @FXML private TextField txtProdName, txtProdPrice;
    @FXML private ComboBox<CategorieBautura> comboProdCategorie;
    @FXML private ComboBox<TipBautura> comboProdTip;

    // ---- RETETE ----
    @FXML private TableView<Reteta> retetaTable;
    @FXML private TableColumn<Reteta, Integer> colRetetaId;
    @FXML private TableColumn<Reteta, String> colRetetaDesc;
    @FXML private TableView<IngredientReteta> newRetetaTable;
    @FXML private TableColumn<IngredientReteta, String> colNewIngredName;
    @FXML private TableColumn<IngredientReteta, Double> colNewIngredCant;
    @FXML private TextField txtNewIngredName, txtNewIngredCant, txtRetetaName;

    // ---- CURRENT ORDER ----
    @FXML private TableView<OrderItem> currentOrderTable;
    @FXML private TableColumn<OrderItem, String> colOrderProdName;
    @FXML private TableColumn<OrderItem, Integer> colOrderQty;
    @FXML private ComboBox<Integer> comboQty;
    @FXML private Label lblOrderTotal;
    @FXML private TextArea txtReceipt;
    @FXML private Label lblTotalRevenue;

    // ---- CATEGORIE MANAGEMENT ----
    @FXML private TableView<CategorieBautura> categorieTable;
    @FXML private TableColumn<CategorieBautura, Integer> colCategorieId;
    @FXML private TableColumn<CategorieBautura, String> colCategorieName;
    @FXML private TextField txtCategorieName;

    // ---- TIP MANAGEMENT ----
    @FXML private TableView<TipBautura> tipTable;
    @FXML private TableColumn<TipBautura, Integer> colTipId;
    @FXML private TableColumn<TipBautura, String> colTipName;
    @FXML private TextField txtTipName;

    // ---- INGREDIENT MANAGEMENT ----
    @FXML private TableView<Ingredient> ingredientTable;
    @FXML private TableColumn<Ingredient, Integer> colIngredientId;
    @FXML private TableColumn<Ingredient, String> colIngredientName;
    @FXML private TextField txtIngredientName;

    // ---- STOC ----
    @FXML private TableView<Stoc> stocTable;
    @FXML private TableColumn<Stoc, String> colStocIngredient;
    @FXML private TableColumn<Stoc, Double> colStocCantitate;
    @FXML private TableColumn<Stoc, Double> colStocMinim;

    // ---- Observable lists ----
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private final ObservableList<Reteta> retetaList = FXCollections.observableArrayList();
    private final ObservableList<IngredientReteta> newRetetaList = FXCollections.observableArrayList();
    private final ObservableList<OrderItem> currentOrderItems = FXCollections.observableArrayList();
    private final ObservableList<CategorieBautura> categorieList = FXCollections.observableArrayList();
    private final ObservableList<TipBautura> tipList = FXCollections.observableArrayList();
    private final ObservableList<Ingredient> ingredientList = FXCollections.observableArrayList();
    private final ObservableList<Stoc> stocList = FXCollections.observableArrayList();

    // ---- Lifecycle ----

    public void setService(DrinkShopService service) {
        this.service = service;
        // Register stock-alert observer (defect A06)
        service.addStocObserver(stoc -> {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Stoc Sub Minim");
            alert.setHeaderText("Stocul pentru " + stoc.getIngredient().getDenumire() + " este sub minim!");
            alert.setContentText("Disponibil: " + stoc.getCantitate() + ", Minim: " + stoc.getStocMinim());
            alert.showAndWait();
        });
        initData();
    }

    @FXML
    private void initialize() {
        // Products
        colProdId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colProdName.setCellValueFactory(new PropertyValueFactory<>("nume"));
        colProdPrice.setCellValueFactory(new PropertyValueFactory<>("pret"));
        colProdCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colProdTip.setCellValueFactory(new PropertyValueFactory<>("tip"));
        productTable.setItems(productList);

        StringConverter<CategorieBautura> catConv = new StringConverter<>() {
            public String toString(CategorieBautura c) { return c == null ? "-- Toate --" : c.getName(); }
            public CategorieBautura fromString(String s) { return null; }
        };
        comboProdCategorie.setConverter(catConv);

        StringConverter<TipBautura> tipConv = new StringConverter<>() {
            public String toString(TipBautura t) { return t == null ? "-- Toate --" : t.getName(); }
            public TipBautura fromString(String s) { return null; }
        };
        comboProdTip.setConverter(tipConv);

        // Retete
        colRetetaId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colRetetaDesc.setCellValueFactory(data -> {
            Reteta r = data.getValue();
            String desc = "[" + r.getName() + "] " + r.getIngrediente().stream()
                    .map(i -> i.getDenumire() + " (" + i.getCantitate() + ")")
                    .collect(Collectors.joining(", "));
            return new SimpleStringProperty(desc);
        });
        retetaTable.setItems(retetaList);

        colNewIngredName.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        colNewIngredCant.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        newRetetaTable.setItems(newRetetaList);

        // Current order
        colOrderProdName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getProduct().getNume()));
        colOrderQty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        currentOrderTable.setItems(currentOrderItems);
        comboQty.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        // Categorie
        colCategorieId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colCategorieName.setCellValueFactory(new PropertyValueFactory<>("name"));
        categorieTable.setItems(categorieList);

        // Tip
        colTipId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTipName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tipTable.setItems(tipList);

        // Ingredient
        colIngredientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colIngredientName.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        ingredientTable.setItems(ingredientList);

        // Stoc
        colStocIngredient.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getIngredient().getDenumire()));
        colStocCantitate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        colStocMinim.setCellValueFactory(new PropertyValueFactory<>("stocMinim"));
        stocTable.setItems(stocList);
    }

    private void initData() {
        productList.setAll(service.getAllProducts());
        retetaList.setAll(service.getAllRetete());
        categorieList.setAll(service.getAllCategorii());
        tipList.setAll(service.getAllTipuri());
        ingredientList.setAll(service.getAllIngredienti());
        stocList.setAll(service.getAllStocuri());

        // Populate combo boxes from service
        comboProdCategorie.getItems().clear();
        comboProdCategorie.getItems().add(null);          // null = "all"
        comboProdCategorie.getItems().addAll(service.getAllCategorii());

        comboProdTip.getItems().clear();
        comboProdTip.getItems().add(null);
        comboProdTip.getItems().addAll(service.getAllTipuri());

        lblTotalRevenue.setText("Daily Revenue: " + service.getDailyRevenue());
        refreshCurrentOrder();
    }

    // ---- Product handlers ----

    @FXML
    private void onAddProduct() {
        Reteta r = retetaTable.getSelectionModel().getSelectedItem();
        if (r == null) { showError("Selectati o reteta pentru noul produs."); return; }
        if (service.getAllProducts().stream().anyMatch(p -> p.getId() == r.getId())) {
            showError("Exista deja un produs cu aceasta reteta."); return;
        }
        CategorieBautura cat = comboProdCategorie.getValue();
        TipBautura tip = comboProdTip.getValue();
        if (cat == null || tip == null) { showError("Selectati o categorie si un tip."); return; }
        try {
            Product p = new Product(r.getId(), txtProdName.getText(),
                    Double.parseDouble(txtProdPrice.getText()), cat, tip);
            service.addProduct(p);
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML
    private void onUpdateProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        CategorieBautura cat = comboProdCategorie.getValue();
        TipBautura tip = comboProdTip.getValue();
        if (cat == null || tip == null) { showError("Selectati o categorie si un tip."); return; }
        try {
            service.updateProduct(selected.getId(), txtProdName.getText(),
                    Double.parseDouble(txtProdPrice.getText()), cat, tip);
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML
    private void onDeleteProduct() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        service.deleteProduct(selected.getId());
        initData();
    }

    @FXML private void onFilterCategorie() {
        productList.setAll(service.filtreazaDupaCategorie(comboProdCategorie.getValue()));
    }
    @FXML private void onFilterTip() {
        productList.setAll(service.filtreazaDupaTip(comboProdTip.getValue()));
    }

    // ---- Reteta handlers ----

    @FXML
    private void onAddNewIngred() {
        String name = txtNewIngredName.getText().trim();
        Ingredient ingredient = service.findIngredientByName(name);
        if (ingredient == null) {
            showError("Ingredientul '" + name + "' nu este inregistrat. Adaugati-l mai intai.");
            return;
        }
        try {
            newRetetaList.add(new IngredientReteta(ingredient,
                    Double.parseDouble(txtNewIngredCant.getText())));
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML
    private void onDeleteNewIngred() {
        IngredientReteta sel = newRetetaTable.getSelectionModel().getSelectedItem();
        if (sel != null) newRetetaList.remove(sel);
    }

    @FXML
    private void onAddNewReteta() {
        try {
            Reteta r = new Reteta(service.nextRetetaId(),
                    txtRetetaName.getText(), new ArrayList<>(newRetetaList));
            service.addReteta(r);
            newRetetaList.clear();
            txtRetetaName.clear();
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML
    private void onClearNewRetetaIngredients() {
        newRetetaList.clear();
        txtNewIngredName.clear();
        txtNewIngredCant.clear();
    }

    // ---- Current order handlers ----

    @FXML
    private void onAddOrderItem() {
        Product selected = productTable.getSelectionModel().getSelectedItem();
        Integer qty = comboQty.getValue();
        if (selected == null) { showError("Selecteaza un produs din lista."); return; }
        if (qty == null) { showError("Selecteaza cantitatea."); return; }
        try {
            service.addItemToCurrentOrder(new OrderItem(selected, qty));
            refreshCurrentOrder();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML
    private void onDeleteOrderItem() {
        OrderItem sel = currentOrderTable.getSelectionModel().getSelectedItem();
        if (sel != null) {
            service.removeItemFromCurrentOrder(sel);
            refreshCurrentOrder();
        }
    }

    @FXML
    private void onFinalizeOrder() {
        try {
            Order finalized = service.finalizeCurrentOrder();
            txtReceipt.setText(service.generateReceipt(finalized));
            refreshCurrentOrder();
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    private void refreshCurrentOrder() {
        currentOrderItems.setAll(service.getCurrentOrderItems());
        lblOrderTotal.setText("Total: " + service.computeCurrentOrderTotal());
    }

    // ---- Categorie handlers ----

    @FXML private void onAddCategorie() {
        try {
            service.addCategorie(new CategorieBautura(
                    service.nextCategorieId(), txtCategorieName.getText()));
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML private void onUpdateCategorie() {
        CategorieBautura sel = categorieTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            service.updateCategorie(new CategorieBautura(sel.getId(), txtCategorieName.getText()));
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML private void onDeleteCategorie() {
        CategorieBautura sel = categorieTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            service.deleteCategorie(sel.getId());
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    // ---- Tip handlers ----

    @FXML private void onAddTip() {
        try {
            service.addTip(new TipBautura(service.nextTipId(), txtTipName.getText()));
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML private void onUpdateTip() {
        TipBautura sel = tipTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            service.updateTip(new TipBautura(sel.getId(), txtTipName.getText()));
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML private void onDeleteTip() {
        TipBautura sel = tipTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            service.deleteTip(sel.getId());
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    // ---- Ingredient handlers ----

    @FXML private void onAddIngredient() {
        try {
            service.addIngredient(new Ingredient(
                    service.nextIngredientId(), txtIngredientName.getText()));
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML private void onUpdateIngredient() {
        Ingredient sel = ingredientTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            service.updateIngredient(new Ingredient(sel.getId(), txtIngredientName.getText()));
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    @FXML private void onDeleteIngredient() {
        Ingredient sel = ingredientTable.getSelectionModel().getSelectedItem();
        if (sel == null) return;
        try {
            service.deleteIngredient(sel.getId());
            initData();
        } catch (Exception e) { showError(e.getMessage()); }
    }

    // ---- Export / Revenue ----

    @FXML private void onExportOrdersCsv() { service.exportCsv("orders.csv"); }
    @FXML private void onDailyRevenue() {
        lblTotalRevenue.setText("Daily Revenue: " + service.getDailyRevenue());
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        alert.showAndWait();
    }
}
