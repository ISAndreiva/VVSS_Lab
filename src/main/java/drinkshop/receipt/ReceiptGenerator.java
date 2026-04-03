package drinkshop.receipt;

import drinkshop.domain.Order;
import drinkshop.domain.OrderItem;
import drinkshop.domain.Product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptGenerator {
    private ReceiptGenerator() {
        /* This utility class should not be instantiated */
    }



    public static String generate(Order o, List<Product> products) {
        if(o == null) {
            throw new NullPointerException();
        }

        if(products == null) {
            throw new NullPointerException();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("===== BON FISCAL =====\n").append("Comanda #").append(o.getId()).append("\n");

        Map<Integer, Product> mapProducts = new HashMap<Integer, Product>();

        for(Product product : products) {
            mapProducts.put(product.getId(), product);
        }

        for (OrderItem i : o.getItems()) {
            Product p = mapProducts.get(i.getProduct().getId());
            sb.append(p.getNume()).append(": ").append(p.getPret()).append(" x ").append(i.getQuantity()).append(" = ").append(i.getTotal()).append(" RON\n");
        }
        
        sb.append("---------------------\nTOTAL: ").append(o.getTotalPrice()).append(" RON\n=====================\n");
        return sb.toString();
    }
}