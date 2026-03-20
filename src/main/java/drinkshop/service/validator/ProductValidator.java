package drinkshop.service.validator;

import drinkshop.domain.Product;

public class ProductValidator implements Validator<Product> {

    private static final double MAX_PRICE = 10000.0;

    @Override
    public void validate(Product product) {
        String errors = "";

        if (product.getId() < 0)
            errors += "ID invalid!\n";

        String name = product.getNume();
        boolean invalidName = name == null || name.isBlank() || !name.matches("[A-Za-z0-9 .-]+");
        if (invalidName)
            errors += "Numele nu poate fi gol!\n";

        if (product.getPret() <= 0 || product.getPret() > MAX_PRICE)
            errors += "Pret invalid!\n";

        if (product.getCategorie() == null)
            errors += "Categoria nu poate fi nula!\n";

        if (product.getTip() == null)
            errors += "Tipul nu poate fi nul!\n";

        if (!errors.isEmpty())
            throw new ValidationException(errors);
    }
}
