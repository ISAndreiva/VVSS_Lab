package drinkshop.domain;

/**
 * Entity representing a drink category (e.g. CLASSIC_COFFEE, TEA).
 * Replaces the previous enum so users can add, update and delete categories
 * at runtime (defect A03 – CategorieBautura/TipBautura must be manageable objects).
 */
public class CategorieBautura {

    private int id;
    private String name;

    public CategorieBautura(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    @Override
    public String toString() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategorieBautura)) return false;
        return id == ((CategorieBautura) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}