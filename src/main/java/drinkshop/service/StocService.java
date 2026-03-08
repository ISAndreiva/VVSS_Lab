package drinkshop.service;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;
import drinkshop.service.validator.StocValidator;
import drinkshop.service.validator.ValidationException;

import java.util.ArrayList;
import java.util.List;

/**
 * <ul>
 *   <li>defect A04 – validator called on every mutation.</li>
 *   <li>defect A05 – uses {@link ValidationException} instead of
 *       {@link IllegalStateException}.</li>
 *   <li>defect A06 – Observer pattern: registered {@link StocObserver}
 *       instances are notified after every consumption.</li>
 *   <li>defect A10 – ingredient matching uses entity ID, not String.</li>
 * </ul>
 */
public class StocService {

    private final Repository<Integer, Stoc> stocRepo;
    private final StocValidator validator;
    private final List<StocObserver> observers = new ArrayList<>();

    public StocService(Repository<Integer, Stoc> stocRepo,
                       StocValidator validator) {
        this.stocRepo = stocRepo;
        this.validator = validator;
    }

    public void addObserver(StocObserver observer) {
        observers.add(observer);
    }

    public List<Stoc> getAll() {
        return stocRepo.findAll();
    }

    public void add(Stoc s) {
        validator.validate(s);
        stocRepo.save(s);
    }

    public void update(Stoc s) {
        validator.validate(s);
        stocRepo.update(s);
    }

    public void delete(int id) {
        stocRepo.delete(id);
    }

    public boolean areSuficient(Reteta reteta) {
        for (IngredientReteta e : reteta.getIngrediente()) {
            int ingredientId = e.getIngredient().getId();
            double necesar = e.getCantitate();

            double disponibil = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().getId() == ingredientId)
                    .mapToDouble(Stoc::getCantitate)
                    .sum();

            if (disponibil < necesar) return false;
        }
        return true;
    }

    public void consuma(Reteta reteta) {
        if (!areSuficient(reteta))
            throw new ValidationException("Stoc insuficient pentru reteta.");

        for (IngredientReteta e : reteta.getIngrediente()) {
            int ingredientId = e.getIngredient().getId();
            double ramas = e.getCantitate();

            List<Stoc> matching = stocRepo.findAll().stream()
                    .filter(s -> s.getIngredient().getId() == ingredientId)
                    .toList();

            for (Stoc s : matching) {
                if (ramas <= 0) break;
                double deScazut = Math.min(s.getCantitate(), ramas);
                s.setCantitate(s.getCantitate() - deScazut);
                ramas -= deScazut;
                stocRepo.update(s);
            }
        }

        // Notify observers about any stock items that just dropped below minimum
        stocRepo.findAll().stream()
                .filter(Stoc::isSubMinim)
                .forEach(s -> observers.forEach(obs -> obs.onStocSubMinim(s)));
    }
}