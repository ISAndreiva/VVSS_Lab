package drinkshop.ui;

import drinkshop.domain.*;
import drinkshop.repository.Repository;
import drinkshop.repository.file.*;
import drinkshop.service.*;
import drinkshop.service.validator.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Application entry point. Initialises repositories and services in strict
 * dependency order (defect A02 – all sub-services are created externally and
 * injected into the facade constructor).
 */
public class DrinkShopApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        // 1. Lookup / reference repositories (no cross-dependencies)
        Repository<Integer, CategorieBautura> categorieRepo =
                new FileCategorieRepository("data/categorii.txt");
        Repository<Integer, TipBautura> tipRepo =
                new FileTipRepository("data/tipuri.txt");
        Repository<Integer, Ingredient> ingredientRepo =
                new FileIngredientRepository("data/ingrediente.txt");

        // 2. Entity repositories that depend on lookup repos
        Repository<Integer, Product> productRepo =
                new FileProductRepository("data/products.txt", categorieRepo, tipRepo);
        Repository<Integer, Order> orderRepo =
                new FileOrderRepository("data/orders.txt", productRepo);
        Repository<Integer, Reteta> retetaRepo =
                new FileRetetaRepository("data/retete.txt", ingredientRepo);
        Repository<Integer, Stoc> stocRepo =
                new FileStocRepository("data/stocuri.txt", ingredientRepo);

        // 3. Services (injected validators)
        ProductService productService =
                new ProductService(productRepo, new ProductValidator());
        OrderService orderService =
                new OrderService(orderRepo, new OrderValidator(), new OrderItemValidator());
        RetetaService retetaService =
                new RetetaService(retetaRepo, new RetetaValidator());
        StocService stocService =
                new StocService(stocRepo, new StocValidator());
        CategorieService categorieService =
                new CategorieService(categorieRepo, new CategorieValidator());
        TipService tipService =
                new TipService(tipRepo, new TipValidator());
        IngredientService ingredientService =
                new IngredientService(ingredientRepo, new IngredientValidator());
        DailyReportService dailyReport = new DailyReportService(orderRepo);

        // 4. Facade
        DrinkShopService service = new DrinkShopService(
                productService, orderService, retetaService, stocService,
                categorieService, tipService, ingredientService, dailyReport);

        // 5. Load UI
        FXMLLoader loader = new FXMLLoader(getClass().getResource("drinkshop.fxml"));
        Scene scene = new Scene(loader.load());

        DrinkShopController controller = loader.getController();
        controller.setService(service);

        stage.setTitle("Coffee Shop Management");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}