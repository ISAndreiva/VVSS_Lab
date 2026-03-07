package drinkshop.service;

import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;
import drinkshop.service.validator.RetetaValidator;

import java.util.List;

/** Calls the validator on every mutation (defect A04). */
public class RetetaService {

    private final Repository<Integer, Reteta> retetaRepo;
    private final RetetaValidator validator;

    public RetetaService(Repository<Integer, Reteta> retetaRepo,
                         RetetaValidator validator) {
        this.retetaRepo = retetaRepo;
        this.validator = validator;
    }

    public void addReteta(Reteta r) {
        validator.validate(r);
        retetaRepo.save(r);
    }

    public void updateReteta(Reteta r) {
        validator.validate(r);
        retetaRepo.update(r);
    }

    public void deleteReteta(int id) {
        retetaRepo.delete(id);
    }

    public Reteta findById(int id) {
        return retetaRepo.findOne(id);
    }

    public List<Reteta> getAll() {
        return retetaRepo.findAll();
    }
}