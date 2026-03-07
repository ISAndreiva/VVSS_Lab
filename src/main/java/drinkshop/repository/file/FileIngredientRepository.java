package drinkshop.repository.file;

import drinkshop.domain.Ingredient;

/** File repository for Ingredient entities (defect A10). */
public class FileIngredientRepository
        extends FileAbstractRepository<Integer, Ingredient> {

    public FileIngredientRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(Ingredient entity) {
        return entity.getId();
    }

    @Override
    protected Ingredient extractEntity(String line) {
        String[] elems = line.split(",");
        int id = Integer.parseInt(elems[0]);
        String denumire = elems[1];
        return new Ingredient(id, denumire);
    }

    @Override
    protected String createEntityAsString(Ingredient entity) {
        return entity.getId() + "," + entity.getDenumire();
    }
}
