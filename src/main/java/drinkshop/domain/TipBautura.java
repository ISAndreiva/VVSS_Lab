package drinkshop.domain;

/**
 * Entity representing a drink type (e.g. BASIC, DAIRY).
 * Replaces the previous enum so users can add, update and delete types
 * at runtime (defect A03 – CategorieBautura/TipBautura must be manageable objects).
 */
public class TipBautura {

    private int id;
    private String name;

    public TipBautura(int id, String name) {
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
        if (!(o instanceof TipBautura)) return false;
        return id == ((TipBautura) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}