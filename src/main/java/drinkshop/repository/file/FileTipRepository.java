package drinkshop.repository.file;

import drinkshop.domain.TipBautura;

/** File repository for TipBautura entities (defect A03). */
public class FileTipRepository
        extends FileAbstractRepository<Integer, TipBautura> {

    public FileTipRepository(String fileName) {
        super(fileName);
        loadFromFile();
    }

    @Override
    protected Integer getId(TipBautura entity) {
        return entity.getId();
    }

    @Override
    protected TipBautura extractEntity(String line) {
        String[] elems = line.split(",");
        int id = Integer.parseInt(elems[0]);
        String name = elems[1];
        return new TipBautura(id, name);
    }

    @Override
    protected String createEntityAsString(TipBautura entity) {
        return entity.getId() + "," + entity.getName();
    }
}
