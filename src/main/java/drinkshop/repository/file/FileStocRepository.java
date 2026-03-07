package drinkshop.repository.file;

import drinkshop.domain.Ingredient;
import drinkshop.domain.Stoc;
import drinkshop.repository.Repository;

/**
 * Persists stock entries using ingredient IDs rather than raw Strings
 * (defect A10).
 * Format: id;ingredientId;cantitate;stocMinim
 */
public class FileStocRepository
        extends FileAbstractRepository<Integer, Stoc> {

    private final Repository<Integer, Ingredient> ingredientRepo;

    public FileStocRepository(String fileName,
                               Repository<Integer, Ingredient> ingredientRepo) {
        super(fileName);
        this.ingredientRepo = ingredientRepo;
        loadFromFile();
    }

    @Override
    protected Integer getId(Stoc entity) {
        return entity.getId();
    }

    @Override
    protected Stoc extractEntity(String line) {
        String[] elems = line.split(";");
        int id = Integer.parseInt(elems[0]);
        int ingredientId = Integer.parseInt(elems[1]);
        double cantitate = Double.parseDouble(elems[2]);
        double stocMinim = Double.parseDouble(elems[3]);
        Ingredient ingredient = ingredientRepo.findOne(ingredientId);
        if (ingredient == null) {
            throw new IllegalArgumentException(
                    "Ingredientul cu ID " + ingredientId + " nu exista in stoc");
        }
        return new Stoc(id, ingredient, cantitate, stocMinim);
    }

    @Override
    protected String createEntityAsString(Stoc entity) {
        return entity.getId() + ";" +
                entity.getIngredient().getId() + ";" +
                entity.getCantitate() + ";" +
                entity.getStocMinim();
    }
}