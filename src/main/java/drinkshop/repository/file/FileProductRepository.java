package drinkshop.repository.file;

import drinkshop.domain.CategorieBautura;
import drinkshop.domain.Product;
import drinkshop.domain.TipBautura;
import drinkshop.repository.Repository;

/**
 * Reads category and type by name from their own repositories so that
 * CategorieBautura and TipBautura are proper managed entities (defect A03).
 */
public class FileProductRepository
        extends FileAbstractRepository<Integer, Product> {

    private final Repository<Integer, CategorieBautura> categorieRepo;
    private final Repository<Integer, TipBautura> tipRepo;

    public FileProductRepository(String fileName,
                                  Repository<Integer, CategorieBautura> categorieRepo,
                                  Repository<Integer, TipBautura> tipRepo) {
        super(fileName);
        this.categorieRepo = categorieRepo;
        this.tipRepo = tipRepo;
        loadFromFile();
    }

    @Override
    protected Integer getId(Product entity) {
        return entity.getId();
    }

    @Override
    protected Product extractEntity(String line) {
        String[] elems = line.split(",");
        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        double price = Double.parseDouble(elems[2]);
        String categorieName = elems[3];
        String tipName = elems[4];

        CategorieBautura categorie = categorieRepo.findAll().stream()
                .filter(c -> c.getName().equals(categorieName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Categoria '" + categorieName + "' nu exista in fisierul de categorii"));

        TipBautura tip = tipRepo.findAll().stream()
                .filter(t -> t.getName().equals(tipName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Tipul '" + tipName + "' nu exista in fisierul de tipuri"));

        return new Product(id, name, price, categorie, tip);
    }

    @Override
    protected String createEntityAsString(Product entity) {
        return entity.getId() + "," +
                entity.getNume() + "," +
                entity.getPret() + "," +
                entity.getCategorie().getName() + "," +
                entity.getTip().getName();
    }
}