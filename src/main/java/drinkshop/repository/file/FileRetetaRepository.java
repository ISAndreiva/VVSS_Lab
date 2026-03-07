package drinkshop.repository.file;

import drinkshop.domain.Ingredient;
import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Persists recipes with a name field (defect A03) and ingredient IDs
 * (defect A10 - Ingredient entity instead of raw Strings).
 * Format: id,name,ingredientId:cantitate,...
 */
public class FileRetetaRepository
        extends FileAbstractRepository<Integer, Reteta> {

    private final Repository<Integer, Ingredient> ingredientRepo;

    public FileRetetaRepository(String fileName,
                                 Repository<Integer, Ingredient> ingredientRepo) {
        super(fileName);
        this.ingredientRepo = ingredientRepo;
        loadFromFile();
    }

    @Override
    protected Integer getId(Reteta entity) {
        return entity.getId();
    }

    @Override
    protected Reteta extractEntity(String line) {
        String[] elems = line.split(",");
        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        List<IngredientReteta> ingrediente = new ArrayList<>();
        for (int i = 2; i < elems.length; i++) {
            String[] parts = elems[i].split(":");
            int ingredientId = Integer.parseInt(parts[0]);
            double qty = Double.parseDouble(parts[1]);
            Ingredient ingredient = ingredientRepo.findOne(ingredientId);
            if (ingredient == null) {
                throw new IllegalArgumentException(
                        "Ingredientul cu ID " + ingredientId + " nu exista");
            }
            ingrediente.add(new IngredientReteta(ingredient, qty));
        }
        return new Reteta(id, name, ingrediente);
    }

    @Override
    protected String createEntityAsString(Reteta entity) {
        String ingrediente = entity.getIngrediente().stream()
                .map(e -> e.getIngredient().getId() + ":" + e.getCantitate())
                .collect(Collectors.joining(","));
        return entity.getId() + "," + entity.getName() + "," + ingrediente;
    }
}
