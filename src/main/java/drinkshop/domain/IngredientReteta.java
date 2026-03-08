package drinkshop.domain;

/**
 * An ingredient entry inside a recipe, referencing an Ingredient entity
 * (defect A10 – ingredients identified by entity, not by raw String).
 */
public class IngredientReteta {

    private Ingredient ingredient;
    private double cantitate;

    public IngredientReteta(Ingredient ingredient, double cantitate) {
        this.ingredient = ingredient;
        this.cantitate = cantitate;
    }

    public Ingredient getIngredient() { return ingredient; }

    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    /** Convenience proxy kept for UI cell-value bindings. */
    public String getDenumire() { return ingredient.getDenumire(); }

    public double getCantitate() { return cantitate; }

    public void setCantitate(double cantitate) { this.cantitate = cantitate; }

    @Override
    public String toString() {
        return ingredient.getDenumire() + "," + cantitate;
    }
}