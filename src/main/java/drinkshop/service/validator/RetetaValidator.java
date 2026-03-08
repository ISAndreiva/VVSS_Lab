package drinkshop.service.validator;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;

import java.util.List;

/** Fixed NPE bug on null ingredient list and removed AtomicReference antipattern. */
public class RetetaValidator implements Validator<Reteta> {

    @Override
    public void validate(Reteta reteta) {
        String errors = "";

        if (reteta.getId() <= 0)
            errors += "Product ID invalid!\n";

        if (reteta.getName() == null || reteta.getName().isBlank())
            errors += "Numele retetei nu poate fi gol!\n";

        List<IngredientReteta> ingrediente = reteta.getIngrediente();
        if (ingrediente == null || ingrediente.isEmpty()) {
            errors += "Reteta nu are ingrediente!\n";
        } else {
            for (IngredientReteta entry : ingrediente) {
                if (entry.getIngredient() == null)
                    errors += "Ingredient null in reteta!\n";
                else if (entry.getCantitate() <= 0)
                    errors += "[" + entry.getDenumire() + "] cantitate negativa sau zero!\n";
            }
        }

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
