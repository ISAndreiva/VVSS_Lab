package drinkshop.service.validator;

import drinkshop.domain.Stoc;

public class StocValidator implements Validator<Stoc> {

    @Override
    public void validate(Stoc stoc) {
        String errors = "";

        if (stoc.getId() <= 0)
            errors += "ID invalid!\n";

        if (stoc.getIngredient() == null)
            errors += "Ingredient invalid!\n";

        if (stoc.getCantitate() < 0)
            errors += "Cantitate negativa!\n";

        if (stoc.getStocMinim() < 0)
            errors += "Stoc minim negativ!\n";

        // NOTE: stoc sub minim is a business alert (Observer pattern), not a
        // data-integrity error, so that check was intentionally removed here.

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}