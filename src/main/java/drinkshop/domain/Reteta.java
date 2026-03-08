package drinkshop.domain;

import java.util.List;

/**
 * A preparation recipe. Added a {@code name} field required by the
 * functional spec (defect A03 – Reteta must have a name).
 */
public class Reteta {

    private int id;
    private String name;
    private List<IngredientReteta> ingrediente;

    public Reteta(int id, String name, List<IngredientReteta> ingrediente) {
        this.id = id;
        this.name = name;
        this.ingrediente = ingrediente;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public List<IngredientReteta> getIngrediente() { return ingrediente; }

    public void setIngrediente(List<IngredientReteta> ingrediente) { this.ingrediente = ingrediente; }

    @Override
    public String toString() {
        return "Reteta{id=" + id + ", name=" + name + ", ingrediente=" + ingrediente + '}';
    }
}

