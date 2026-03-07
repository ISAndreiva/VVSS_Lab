package drinkshop.service.validator;

import drinkshop.domain.Ingredient;

/** Validates an Ingredient entity (defect A10). */
public class IngredientValidator implements Validator<Ingredient> {

    @Override
    public void validate(Ingredient ingredient) {
        String errors = "";

        if (ingredient.getId() <= 0)
            errors += "ID ingredient invalid!\n";

        if (ingredient.getDenumire() == null || ingredient.getDenumire().isBlank())
            errors += "Denumirea ingredientului nu poate fi goala!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
