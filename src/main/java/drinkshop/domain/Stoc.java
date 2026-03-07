package drinkshop.domain;

/**
 * Stock entry for an ingredient, now referencing an Ingredient entity
 * (defect A10 – consistent linkage instead of fragile String matching).
 */
public class Stoc {

    private int id;
    private Ingredient ingredient;
    private double cantitate;
    private double stocMinim;

    public Stoc(int id, Ingredient ingredient, double cantitate, double stocMinim) {
        this.id = id;
        this.ingredient = ingredient;
        this.cantitate = cantitate;
        this.stocMinim = stocMinim;
    }

    public int getId() { return id; }

    public Ingredient getIngredient() { return ingredient; }

    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }

    public double getCantitate() { return cantitate; }

    public void setCantitate(double cantitate) { this.cantitate = cantitate; }

    public double getStocMinim() { return stocMinim; }

    public void setStocMinim(double stocMinim) { this.stocMinim = stocMinim; }

    public boolean isSubMinim() { return cantitate < stocMinim; }

    @Override
    public String toString() {
        return ingredient.getDenumire() + " (" + cantitate + " / minim: " + stocMinim + ")";
    }
}