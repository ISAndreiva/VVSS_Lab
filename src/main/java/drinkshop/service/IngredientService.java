package drinkshop.service;

import drinkshop.domain.Ingredient;
import drinkshop.repository.Repository;
import drinkshop.service.validator.IngredientValidator;
import drinkshop.service.validator.ValidationException;

import java.util.List;

/**
 * Service for managing ingredients (defect A10 - Ingredient entity).
 */
public class IngredientService {

    private final Repository<Integer, Ingredient> ingredientRepo;
    private final IngredientValidator validator;

    public IngredientService(Repository<Integer, Ingredient> ingredientRepo,
                              IngredientValidator validator) {
        this.ingredientRepo = ingredientRepo;
        this.validator = validator;
    }

    public void add(Ingredient ingredient) {
        validator.validate(ingredient);
        ingredientRepo.save(ingredient);
    }

    /** Updates denomination in-place so IngredientReteta/Stoc references stay consistent. */
    public void update(Ingredient updated) {
        Ingredient existing = ingredientRepo.findOne(updated.getId());
        if (existing == null)
            throw new ValidationException("Ingredientul cu ID " + updated.getId() + " nu exista!");
        existing.setDenumire(updated.getDenumire());
        validator.validate(existing);
        ingredientRepo.update(existing);
    }

    public void delete(int id) {
        ingredientRepo.delete(id);
    }

    public List<Ingredient> getAll() {
        return ingredientRepo.findAll();
    }

    public Ingredient findById(int id) {
        return ingredientRepo.findOne(id);
    }

    public Ingredient findByName(String name) {
        return ingredientRepo.findAll().stream()
                .filter(i -> i.getDenumire().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    public int nextId() {
        return ingredientRepo.findAll().stream()
                .mapToInt(Ingredient::getId).max().orElse(0) + 1;
    }
}
