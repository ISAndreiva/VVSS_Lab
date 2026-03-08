package drinkshop.service.validator;

import drinkshop.domain.CategorieBautura;

/** Validates a CategorieBautura entity (defect A03). */
public class CategorieValidator implements Validator<CategorieBautura> {

    @Override
    public void validate(CategorieBautura categorie) {
        String errors = "";

        if (categorie.getId() <= 0)
            errors += "ID categorie invalid!\n";

        if (categorie.getName() == null || categorie.getName().isBlank())
            errors += "Numele categoriei nu poate fi gol!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
