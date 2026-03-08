package drinkshop.service.validator;

import drinkshop.domain.TipBautura;

/** Validates a TipBautura entity (defect A03). */
public class TipValidator implements Validator<TipBautura> {

    @Override
    public void validate(TipBautura tip) {
        String errors = "";

        if (tip.getId() <= 0)
            errors += "ID tip invalid!\n";

        if (tip.getName() == null || tip.getName().isBlank())
            errors += "Numele tipului nu poate fi gol!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
