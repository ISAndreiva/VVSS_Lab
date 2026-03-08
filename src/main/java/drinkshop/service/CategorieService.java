package drinkshop.service;

import drinkshop.domain.CategorieBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.CategorieValidator;

import java.util.List;

/**
 * Service for managing drink categories. Fully supports add, update and delete
 * (defect A03 - categories must be manageable runtime objects, not hardcoded
 * enums).
 */
public class CategorieService {

    private final Repository<Integer, CategorieBautura> categorieRepo;
    private final CategorieValidator validator;

    public CategorieService(Repository<Integer, CategorieBautura> categorieRepo,
                             CategorieValidator validator) {
        this.categorieRepo = categorieRepo;
        this.validator = validator;
    }

    public void add(CategorieBautura categorie) {
        validator.validate(categorie);
        categorieRepo.save(categorie);
    }

    /**
     * Updates the name of an existing category in-place so that all Product
     * objects that hold a reference to it see the change immediately.
     */
    public void update(CategorieBautura updated) {
        CategorieBautura existing = categorieRepo.findOne(updated.getId());
        if (existing == null)
            throw new drinkshop.service.validator.ValidationException(
                    "Categoria cu ID " + updated.getId() + " nu exista!");
        existing.setName(updated.getName());
        validator.validate(existing);
        categorieRepo.update(existing);
    }

    public void delete(int id) {
        categorieRepo.delete(id);
    }

    public List<CategorieBautura> getAll() {
        return categorieRepo.findAll();
    }

    public CategorieBautura findById(int id) {
        return categorieRepo.findOne(id);
    }

    public int nextId() {
        return categorieRepo.findAll().stream()
                .mapToInt(CategorieBautura::getId).max().orElse(0) + 1;
    }
}
