package drinkshop.domain;

/**
 * Entity representing a raw ingredient used in recipes and tracked in stock.
 * Extracted from raw String references to fix defect A10.
 */
public class Ingredient {

    private int id;
    private String denumire;

    public Ingredient(int id, String denumire) {
        this.id = id;
        this.denumire = denumire;
    }

    public int getId() { return id; }

    public String getDenumire() { return denumire; }

    public void setDenumire(String denumire) { this.denumire = denumire; }

    @Override
    public String toString() { return denumire; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Ingredient)) return false;
        return id == ((Ingredient) o).id;
    }

    @Override
    public int hashCode() { return Integer.hashCode(id); }
}
