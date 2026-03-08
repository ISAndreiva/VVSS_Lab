package drinkshop.service;

import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;
import drinkshop.service.validator.TipValidator;
import drinkshop.service.validator.ValidationException;

import java.util.List;

/**
 * Service for managing drink types. Fully supports add, update and delete
 * (defect A03 - types must be manageable runtime objects, not hardcoded enums).
 */
public class TipService {

    private final Repository<Integer, TipBautura> tipRepo;
    private final TipValidator validator;

    public TipService(Repository<Integer, TipBautura> tipRepo,
                      TipValidator validator) {
        this.tipRepo = tipRepo;
        this.validator = validator;
    }

    public void add(TipBautura tip) {
        validator.validate(tip);
        tipRepo.save(tip);
    }

    /** Updates the name in-place so Product references stay consistent. */
    public void update(TipBautura updated) {
        TipBautura existing = tipRepo.findOne(updated.getId());
        if (existing == null)
            throw new ValidationException("Tipul cu ID " + updated.getId() + " nu exista!");
        existing.setName(updated.getName());
        validator.validate(existing);
        tipRepo.update(existing);
    }

    public void delete(int id) {
        tipRepo.delete(id);
    }

    public List<TipBautura> getAll() {
        return tipRepo.findAll();
    }

    public TipBautura findById(int id) {
        return tipRepo.findOne(id);
    }

    public int nextId() {
        return tipRepo.findAll().stream()
                .mapToInt(TipBautura::getId).max().orElse(0) + 1;
    }
}
